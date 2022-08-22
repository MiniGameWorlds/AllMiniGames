package com.worldbiomusic.allgames.games.solobattle;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.minigameworld.events.minigame.MiniGameExceptionEvent;
import com.minigameworld.events.minigame.MiniGamePlayerExceptionEvent;
import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.PlayerTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

/**
 * [Rules]<br>
 * - Get 1 score per 1 second if a player is having the item (task)<br>
 * - Can not pickup, drop items<br>
 * - Cancel all damage to a player<br>
 * - Players can steal item by hitting other players (firework)<br>
 * - Player holding item will start to glow when "glow-time" is left<br>
 * - Send player name with title and message when steals<br>
 * - When game starts, item will be given to random player<br>
 * - <br>
 * 
 * [Custom-data]<br>
 * - item<br>
 * - glow-time<br>
 * - finish-score<br>
 */
public class ItsMine extends SoloBattleMiniGame {

	private Player tagger;
	private Material item;
	private int glowTime;
	private int finishScore;

	public ItsMine() {
		super("ItsMine", 2, 10, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14414);

		setting().setIcon(Material.DIAMOND);

		customOption().set(Option.PVP, true);
		customOption().set(Option.FOOD_LEVEL_CHANGE, false);
		customOption().set(Option.COLOR, ChatColor.RED);
		customOption().set(Option.SCORE_NOTIFYING, false);

		registerTasks();
	}

	private void registerTasks() {
		taskManager().registerTask("plus-tagger-score", () -> {
			plusScore(tagger, 1);

			// check tagger score with finish score
			if (score(tagger) >= finishScore) {
				finishGame();
			}
		});

		taskManager().registerTask("glow-time-checker", () -> {
			checkGlowTime();
		});
	}

	private void checkGlowTime() {
		// notify
		if (Math.abs(leftPlayTime() - this.glowTime) < 1) {
			sendTitles(ChatColor.GOLD + "Glow Time", "");
			players().forEach(p -> PlayerTool.playSound(p, Sound.BLOCK_BELL_USE));
		}

		// set tagger glowing
		if (leftPlayTime() <= this.glowTime) {
			this.tagger.setGlowing(true);
		}

		// cancel glowing other players
		players().stream().filter(p -> p.isGlowing() && !tagger.equals(p)).forEach(p -> p.setGlowing(false));
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();
		data.put("item", Material.DIAMOND.name());
		data.put("glow-time", 30);
		data.put("finish-score", 100);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = customData();
		this.item = Material.valueOf((String) data.get("item"));
		this.glowTime = (int) data.get("glow-time");
		this.finishScore = (int) data.get("finish-score");
	}

	@GameEvent
	protected void onEntityPickupItemEvent(EntityPickupItemEvent e) {
		e.setCancelled(true);
	}

	@GameEvent
	protected void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@GameEvent
	protected void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		// cancel damage
		e.setDamage(0);

		// check event
		Entity damagerEntity = e.getDamager();

		if (!(damagerEntity instanceof Player && containsPlayer((Player) damagerEntity))) {
			return;
		}

		Player victim = (Player) e.getEntity();
		Player damager = (Player) damagerEntity;

		// check item is stolen
		if (victim.equals(this.tagger)) {
			stealItem(damager, victim);
		}
	}

	private void stealItem(Player newTagger, Player oldTagger) {
		// set new tagger
		setNewTagger(newTagger);

		// clear old tagger inv
		oldTagger.getInventory().clear();
	}

	private void spawnFirework(Location loc) {
		Firework firework = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fwm = firework.getFireworkMeta();
		Builder builder = FireworkEffect.builder();
		fwm.addEffect(builder.flicker(true).trail(true).withColor(org.bukkit.Color.ORANGE).build());
		fwm.setPower(2);
		firework.setFireworkMeta(fwm);
	}

	private void setNewTagger(Player tagger) {
		// change tagger
		this.tagger = tagger;

		// give item to new tagger
		tagger.getInventory().setItemInMainHand(new ItemStack(this.item));

		// notify
		sendMessages("Find " + ChatColor.RED + ChatColor.BOLD + tagger.getName());
		sendTitles(ChatColor.RED + tagger.getName(), "");

		// sound
		players().forEach(p -> PlayerTool.playSound(p, Sound.ENTITY_CHICKEN_EGG));

		// firework
		spawnFirework(tagger.getLocation());
	}

	@Override
	protected void onStart() {
		super.onStart();

		setNewTagger(randomPlayer());

		// tasks
		taskManager().runTaskTimer("plus-tagger-score", 0, 20);
		taskManager().runTaskTimer("glow-time-checker", 0, 10);

		// notify finish sore
		sendMessages(ChatColor.GREEN + "Finish score: " + ChatColor.GOLD + this.finishScore);
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Steal item by hit and run away from the other players!");
	}

	@Override
	protected void onException(MiniGameExceptionEvent exception) {
		super.onException(exception);

		// set a new tagger if tagger left the game
		if (exception instanceof MiniGamePlayerExceptionEvent) {
			MiniGamePlayerExceptionEvent e = (MiniGamePlayerExceptionEvent) exception;
			if (this.tagger == null || this.tagger.equals(e.getPlayer()))
				setNewTagger(randomPlayer());
		}
	}

}
