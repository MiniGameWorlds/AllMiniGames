package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.PlayerTool;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

/**
 * [Rules]<br>
 * - Lava will come up to the ceiling<br>
 * - Players have to run away from the lava<br>
 * - Map must be made of not burning blocks<br>
 * - Y of "pos1" is the floor lava height<br>
 * - Y of "pos2" is the max lava height<br>
 * 
 * [Custom data]<br>
 * - pos1<br>
 * - pos2<br>
 * - up-speed<br>
 * - up-scale<br>
 *
 */
public class LavaUp extends SoloBattleMiniGame {
	private Location pos1, pos2;
	private double upSpeed;
	private int upScale;

	private int height;

	public LavaUp() {
		super("LavaUp", 2, 10, 120, 20);

		getSetting().setIcon(Material.LAVA);

		getCustomOption().set(Option.COLOR, ChatColor.RED);
		getCustomOption().set(Option.PVP, true);
		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, false);
		getCustomOption().set(Option.SCORE_NOTIFYING, false);

		registerTasks();
	}

	private void registerTasks() {
		getTaskManager().registerTask("plus-score", () -> {
			getLivePlayers().forEach(p -> {
				plusScore(p, 1);
			});
		});

		getTaskManager().registerTask("fillup-lava", () -> {
			fillupLava();
		});
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

		Map<String, Object> data = getCustomData();
		data.put("pos1", getLocation());
		data.put("pos2", getLocation());
		data.put("up-speed", 3.0);
		data.put("up-scale", 1);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = getCustomData();
		this.pos1 = (Location) data.get("pos1");
		this.pos2 = (Location) data.get("pos2");
		this.upSpeed = (double) data.get("up-speed");
		this.upScale = (int) data.get("up-scale");
	}

	@Override
	protected void initGameSettings() {
		// init lave height
		this.height = (int) pos1.getY();

		// remove all lava
		if (BlockTool.containsBlock(pos1, pos2, Material.LAVA)) {
			BlockTool.replaceBlocks(this.pos1, this.pos2, Material.LAVA, Material.AIR);
		}
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof EntityDamageEvent) {
			EntityDamageEvent damageEvent = (EntityDamageEvent) event;
			checkPlayerIsDead(damageEvent);

			if (damageEvent instanceof EntityDamageByEntityEvent) {
				cancelPlayerHitDamage((EntityDamageByEntityEvent) damageEvent);
			}
		}
	}

	private void checkPlayerIsDead(EntityDamageEvent e) {
		Entity entity = e.getEntity();

		if (!(entity instanceof Player)) {
			return;
		}

		Player p = (Player) entity;
		if (!containsPlayer(p)) {
			return;
		}

		boolean isDead = (p.getHealth() - e.getDamage()) <= 0;
		if (isDead) {
			// cancel damage
			e.setDamage(0);

			// minus 1 score
			minusScore(p, 1);

			// notify
			sendTitleToAllPlayers("" + ChatColor.RED + p.getName(), "DIE");
			getPlayers().forEach(all -> PlayerTool.playSound(all, Sound.BLOCK_BELL_USE));

			setLive(p, false);
		}
	}

	private void cancelPlayerHitDamage(EntityDamageByEntityEvent e) {
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

		// cancel damage
		e.setDamage(0);
	}

	private void fillupLava() {
		Location position1 = this.pos1.clone();
		position1.setY(this.height);

		Location position2 = this.pos2.clone();
		position2.setY(this.height);

		BlockTool.replaceBlocks(position1, position2, Material.AIR, Material.LAVA);

		// increase height
		this.height += this.upScale;

		// check max height
		if (this.height > this.pos2.getBlockY()) {
			this.height = this.pos2.getBlockY();
		}

		getPlayers().forEach(all -> PlayerTool.playSound(all, Sound.ITEM_BUCKET_FILL_LAVA));
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		// tasks
		getTaskManager().runTaskTimer("plus-score", 0, 20);
		getTaskManager().runTaskTimer("fillup-lava", (int) (20 * this.upSpeed), (int) (20 * this.upSpeed));
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Run away from the " + ChatColor.RED + ChatColor.BOLD + "LAVA");

		return tutorial;
	}

}
