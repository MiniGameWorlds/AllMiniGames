package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.ItemStackTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.Msgs;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.wbm.plugin.util.TeleportTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * [Rules]<br>
 * - Lava will come up to the ceiling<br>
 * - Players have to run away from the lava<br>
 * - Map must be made of not burning blocks<br>
 * - Y of "pos1" is the floor lava height<br>
 * - Y of "pos2" is the max lava height<br>
 * 
 */
public class LavaUp extends SoloBattleMiniGame {
	private Location pos1, pos2;
	private int startDelay;
	private double upSpeed;
	private int upScale;
	private int fireResistanceDuration;
	private int superJumpPower;
	private int superJumpLevitationDuration;

	private int lavaHeight;
	private int startDelayCounter;
	private Set<Player> fireResistanceChance;

	public LavaUp() {
		super("LavaUp", 2, 10, 120, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14415);

		setting().setIcon(Material.LAVA_BUCKET);

		customOption().set(Option.COLOR, ChatColor.RED);
		customOption().set(Option.PVP, true);
		customOption().set(Option.FOOD_LEVEL_CHANGE, false);
		customOption().set(Option.SCORE_NOTIFYING, false);

		registerTasks();
	}

	private void registerTasks() {
		taskManager().registerTask("start-delay", () -> {
			if (startDelayCounter <= 0) {
				taskManager().cancelTask("start-delay");
				return;
			}

			String counter = "" + startDelayCounter + ChatColor.RESET;
			if (startDelayCounter == 3) {
				counter = ChatColor.YELLOW + counter;
			} else if (startDelayCounter == 2) {
				counter = ChatColor.GOLD + counter;
			} else if (startDelayCounter == 1) {
				counter = ChatColor.RED + counter;
			}

			// sound
			if (startDelayCounter <= 3) {
				playSounds(Sound.BLOCK_NOTE_BLOCK_BELL);
			}

			sendTitles(counter, ChatColor.RED + "Lava " + ChatColor.RED + " will rise up after...", 0, 21, 0);
			startDelayCounter -= 1;
		});

		taskManager().registerTask("plus-score", () -> {
			livePlayers().forEach(p -> {
				plusScore(p, 1);
			});
		});

		taskManager().registerTask("fillup-lava", () -> {
			fillupLava();
		});

		taskManager().registerTask("update-actionbar", () -> {
			updateActionBar();
		});
	}

	private void updateActionBar() {
		players().forEach(p -> {
			String msg = "Dist between Lava: " + ChatColor.RED + (int) distBetweenLava(p) + ChatColor.RESET
					+ ", Live players: " + ChatColor.GREEN + livePlayersCount();
			Msgs.msg(p, ChatMessageType.ACTION_BAR, new TextComponent(msg));
		});
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();
		data.put("pos1", location());
		data.put("pos2", location());
		data.put("start-delay", 10);
		data.put("up-speed", 3.0);
		data.put("up-scale", 1);
		data.put("fire-resistance-duration", 5);
		data.put("super-jump-power", 2);
		data.put("super-jump-levitation-duration", 3);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = customData();
		this.pos1 = (Location) data.get("pos1");
		this.pos2 = (Location) data.get("pos2");
		this.startDelay = (int) data.get("start-delay");
		this.upSpeed = (double) data.get("up-speed");
		this.upScale = (int) data.get("up-scale");
		this.fireResistanceDuration = (int) data.get("fire-resistance-duration");
		this.superJumpPower = (int) data.get("super-jump-power");
		this.superJumpLevitationDuration = (int) data.get("super-jump-levitation-duration");
	}

	@Override
	protected void initGame() {
		// init lava height
		this.lavaHeight = (int) pos1.getY();

		// reset timer
		this.startDelayCounter = this.startDelay;
	}

	// check player is dead
	@GameEvent
	protected void onPlayerDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		boolean isDead = (p.getHealth() - e.getDamage()) <= 0;

		if (isDead) {
			onPlayerDie(e, p);
		} else if (e.getCause() == DamageCause.LAVA) {
			onFireResistanceChange(e, p);
		}
	}

	private void onPlayerDie(EntityDamageEvent e, Player p) {
		// cancel damage
		e.setDamage(0);

		// minus 1 score
		minusScore(p, 1);

		// notify
		sendMessages(p.getName() + ChatColor.RED + " died");
		playSounds(Sound.BLOCK_BELL_USE);
		ParticleTool.spawn(p.getLocation().add(0, 3, 0), Particle.CAMPFIRE_SIGNAL_SMOKE, 100, 0.01);

		// sound
		SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_BELL);

		setLive(p, false);
	}

	private void onFireResistanceChange(EntityDamageEvent e, Player p) {
		if (this.fireResistanceChance.contains(p)) {
			this.fireResistanceChance.remove(p);

			// cancel event
			e.setCancelled(true);

			// give fire resistance
			p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * this.fireResistanceDuration, 1));

			sendTitle(p, ChatColor.GREEN + "Chance", "fire resistance for " + this.fireResistanceDuration + " sec", 5,
					50, 5);
			ParticleTool.spawn(p.getLocation().add(0, 3, 0), Particle.FIREWORKS_SPARK, 500, 0.1);
			playSound(p, Sound.ITEM_TOTEM_USE);
		}
	}

	// set player hit damage to 0
	@GameEvent
	protected void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		if (!(damager instanceof Player && containsPlayer((Player) damager))) {
			return;
		}

		// cancel damage
		e.setDamage(0);
	}

	private void fillupLava() {
		Location position1 = this.pos1.clone();
		position1.setY(this.lavaHeight);

		Location position2 = this.pos2.clone();
		position2.setY(this.lavaHeight);

		BlockTool.replaceBlocks(position1, position2, Material.AIR, Material.LAVA);

		// increase height
		this.lavaHeight += this.upScale;

		// check max height
		if (this.lavaHeight > this.pos2.getBlockY()) {
			this.lavaHeight = this.pos2.getBlockY();
		}

		alert();
	}

	// alert msg to dangerous players
	private void alert() {
		players().forEach(p -> {
			double diff = distBetweenLava(p);

			if (0 <= diff && diff <= 3) {
				sendTitle(p, ChatColor.RED + "☠ Dangerous ☠", "Lava is close to you!", 2, 16, 2);
				playSound(p, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO);
			} else {
				playSounds(Sound.ITEM_BUCKET_FILL_LAVA);
			}
		});
	}

	private double distBetweenLava(Player p) {
		double playerHeight = p.getLocation().getY();
		return playerHeight - this.lavaHeight;
	}

	@GameEvent
	protected void onPlayerDropItem(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@GameEvent
	protected void onPlayerUseItem(PlayerInteractEvent e) {
		ItemStack item = e.getItem();
		if (item == null) {
			return;
		}

		// use skill
		if (item.equals(superJump())) {
			onSuperJump(e);
		}

		// remove item
		item.setAmount(0);
	}

	private void onSuperJump(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		p.setVelocity(new Vector(0, this.superJumpPower, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * this.superJumpLevitationDuration, 1));

		sendTitle(p, ChatColor.YELLOW + "Super Jump!", "+ levitation for " + this.superJumpLevitationDuration + " sec",
				5, 50, 5);
		playSound(p, Sound.ENTITY_ENDER_DRAGON_FLAP);
		particle(p, Particle.CLOUD, 300, 0.01);
	}

	private ItemStack superJump() {
		return ItemStackTool.item(Material.FEATHER, ChatColor.YELLOW + "Super Jump", "Right-click to use");
	}

	@Override
	protected void onStart() {
		super.onStart();

		// give super-jump item
		players().forEach(p -> p.getInventory().addItem(superJump()));

		this.fireResistanceChance = new HashSet<>();
		this.fireResistanceChance.addAll(players());

		// tp all to the spawn
		TeleportTool.tp(players(), location());

		// tasks
		taskManager().runTaskTimer("start-delay", 20, 20);

		int realStartDelay = 20 + 20 * this.startDelay;
		taskManager().runTaskTimer("plus-score", realStartDelay, 20);
		taskManager().runTaskTimer("fillup-lava", realStartDelay, (int) (20 * this.upSpeed));
		taskManager().runTaskTimer("update-actionbar", realStartDelay, 10);

	}

	@Override
	protected List<String> tutorial() {
		List<String> t = new ArrayList<>();
		t.add("Run away from the rising" + ChatColor.RED + ChatColor.BOLD + " LAVA!");
		t.add("There are only " + ChatColor.GREEN + "one chance" + ChatColor.RESET
				+ " to get fire resistance when you fall into the lava");
		t.add("\n" + ChatColor.AQUA + "[Skill Item] (Click to use)");
		t.add(ChatColor.YELLOW + "Super jump" + ChatColor.RESET + ": jump to the sky and get levitation");
		return t;
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		// remove all lava
		if (BlockTool.containsBlock(pos1, pos2, Material.LAVA)) {
			BlockTool.replaceBlocks(this.pos1, this.pos2, Material.LAVA, Material.AIR);
		}
	}

}
