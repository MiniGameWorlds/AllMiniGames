package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.LocationTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class DodgeBlock extends SoloBattleMiniGame implements Listener {

	private int spawnRate, spawnRateIncrease;
	private Location pos1, pos2;
	private List<Material> blocks;

	private List<Entity> fallingBlocks;

	public DodgeBlock() {
		super("DodgeBlock", 2, 10, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14723);

		// register as a event listener
		// Reason: last falling block can not be removed by in minigame
		AllMiniGamesMain.getInstance().getServer().getPluginManager().registerEvents(this,
				AllMiniGamesMain.getInstance());

		setting().setIcon(Material.GLOWSTONE);

		customOption().set(Option.COLOR, ChatColor.GRAY);
		customOption().set(Option.FOOD_LEVEL_CHANGE, false);
		customOption().set(Option.PVP, false);

		this.fallingBlocks = new ArrayList<>();

		registerTasks();
	}

	private void registerTasks() {
		taskManager().registerTask("every-sec", () -> {
			double spawnRateTime = 1.0 / spawnRate * 20.0;

			for (int i = 0; i < spawnRate; i++) {
				taskManager().runTaskLater("spawn-block", (int) (spawnRateTime * i));
			}

			spawnRate += spawnRateIncrease;
		});

		taskManager().registerTask("spawn-block", () -> {
			spawnFallingBlock();
		});

	}
	
	@GameEvent
	protected void onPlayerDamage(EntityDamageEvent e) {
		// cancel other damage types except for falling block damage
		if (e.getCause() != DamageCause.FALLING_BLOCK) {
			e.setCancelled(true);
		}
	}

	@GameEvent
	protected void onPlayerDamagedByFallingBlock(EntityDamageByEntityEvent e) {
		Entity fallingBlock = e.getDamager();
		if (this.fallingBlocks.contains(fallingBlock)) {
			Player p = (Player) e.getEntity();

			// give score to others
			livePlayers().stream().filter(all -> !all.equals(p)).forEach(all -> plusScore(all, 1));

			// message, sound
			sendMessages(p.getName() + ChatColor.RED + " died");
			sendTitle(p, ChatColor.RED + "DIE", "");
			SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_CHIME);

			// live false
			setLive(p, false);
		}
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Dodge the falling blocks!");
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();

		List<String> blocks = new ArrayList<>();
		blocks.add(Material.GLOWSTONE.name());
		blocks.add(Material.LANTERN.name());
		blocks.add(Material.LANTERN.name());
		blocks.add(Material.CAKE.name());

		data.put("blocks", blocks);
		data.put("spawn-rate", 1);
		data.put("spawn-rate-increase", 1);

		data.put("pos1", location());
		data.put("pos2", location());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = customData();

		this.blocks = new ArrayList<>();
		((List<String>) data.get("blocks")).forEach(b -> {
			Material block = Material.valueOf(b);
			if (block.isBlock()) {
				this.blocks.add(block);
			}
		});
		this.spawnRate = (int) data.get("spawn-rate");
		this.spawnRateIncrease = (int) data.get("spawn-rate-increase");

		this.pos1 = (Location) data.get("pos1");
		this.pos2 = (Location) data.get("pos2");
	}

	private Material getRandomBlock() {
		int r = (int) (this.blocks.size() * Math.random());
		return this.blocks.get(r);
	}

	private void spawnFallingBlock() {
		Location randomLoc = LocationTool.getRandomLocation(this.pos1, this.pos2).getBlock().getLocation().add(0.5, 0,
				0.5);

		// spawn block
		BlockData randomBlockData = Bukkit.createBlockData(getRandomBlock());
		org.bukkit.entity.FallingBlock block = randomLoc.getWorld().spawnFallingBlock(randomLoc, randomBlockData);
		block.setHurtEntities(true);
		block.setDropItem(false);
		block.setGlowing(true);

		this.fallingBlocks.add(block);
	}

	

	@Override
	protected void onStart() {
		super.onStart();

		this.fallingBlocks.clear();

		// init spawn rate
		this.spawnRate = (int) customData().get("spawn-rate");

		// start to spawn blocks
		taskManager().runTaskTimer("every-sec", 0, 20);

	}

	@Override
	protected void onFinish() {
		super.onFinish();

		// remove falling blocks
		this.fallingBlocks.stream().filter(b -> b != null).forEach(b -> b.remove());
	}

	/*
	 * Remove falling block until minigame is not started
	 */
	@EventHandler
	public void onFallingBlockChangeToBlock(EntityChangeBlockEvent e) {
		Entity entity = e.getEntity();
		if (this.fallingBlocks.contains(entity)) {
			e.setCancelled(true);
			entity.remove();
		}
	}

}

//
//
//
//
//
//