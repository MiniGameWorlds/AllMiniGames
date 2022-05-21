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
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.PlayerTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.customevents.minigame.MiniGameExceptionEvent;
import com.worldbiomusic.minigameworld.customevents.minigame.MiniGamePlayerExceptionEvent;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

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

		getSetting().setIcon(Material.DIAMOND);

		getCustomOption().set(Option.PVP, true);
		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, false);
		getCustomOption().set(Option.COLOR, ChatColor.RED);
		getCustomOption().set(Option.SCORE_NOTIFYING, false);

		registerTasks();
	}

	private void registerTasks() {
		getTaskManager().registerTask("plus-tagger-score", () -> {
			plusScore(tagger, 1);

			// check tagger score with finish score
			if (getScore(tagger) >= finishScore) {
				finishGame();
			}
		});

		getTaskManager().registerTask("glow-time-checker", () -> {
			checkGlowTime();
		});
	}

	private void checkGlowTime() {
		// notify
		if (Math.abs(getLeftFinishTime() - this.glowTime) < 1) {
			sendTitles(ChatColor.GOLD + "Glow Time", "");
			getPlayers().forEach(p -> PlayerTool.playSound(p, Sound.BLOCK_BELL_USE));
		}

		// set tagger glowing
		if (getLeftFinishTime() <= this.glowTime) {
			this.tagger.setGlowing(true);
		}

		// cancel glowing other players
		getPlayers().stream().filter(p -> p.isGlowing() && !tagger.equals(p)).forEach(p -> p.setGlowing(false));
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = getCustomData();
		data.put("item", Material.DIAMOND.name());
		data.put("glow-time", 30);
		data.put("finish-score", 100);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = getCustomData();
		this.item = Material.valueOf((String) data.get("item"));
		this.glowTime = (int) data.get("glow-time");
		this.finishScore = (int) data.get("finish-score");
	}

	@Override
	protected void initGame() {
	}

	@Override
	protected void onEvent(Event event) {
		if (event instanceof EntityPickupItemEvent) {
			((EntityPickupItemEvent) event).setCancelled(true);
		} else if (event instanceof PlayerDropItemEvent) {
			((PlayerDropItemEvent) event).setCancelled(true);
		} else if (event instanceof EntityDamageEvent) {
			hitPlayer((EntityDamageEvent) event);
		}
	}

	private void hitPlayer(EntityDamageEvent damageEvent) {
		// cancel damage
		damageEvent.setDamage(0);

		// check event
		if (damageEvent instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) damageEvent;

			Entity victimEntity = e.getEntity();
			Entity damagerEntity = e.getDamager();

			if (!(victimEntity instanceof Player && damagerEntity instanceof Player)) {
				return;
			}

			Player victim = (Player) victimEntity;
			Player damager = (Player) damagerEntity;

			if (!(containsPlayer(victim) && containsPlayer(damager))) {
				return;
			}

			// check item is stolen
			if (victim.equals(this.tagger)) {
				stealItem(damager, victim);
			}
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
		getPlayers().forEach(p -> PlayerTool.playSound(p, Sound.ENTITY_CHICKEN_EGG));

		// firework
		spawnFirework(tagger.getLocation());
	}

	@Override
	protected void onStart() {
		super.onStart();

		setNewTagger(randomPlayer());

		// tasks
		getTaskManager().runTaskTimer("plus-tagger-score", 0, 20);
		getTaskManager().runTaskTimer("glow-time-checker", 0, 10);

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
