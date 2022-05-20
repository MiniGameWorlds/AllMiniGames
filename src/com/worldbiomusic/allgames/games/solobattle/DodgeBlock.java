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
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.wbm.plugin.util.LocationTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

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

		getSetting().setIcon(Material.GLOWSTONE);

		getCustomOption().set(Option.COLOR, ChatColor.GRAY);
		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, false);
		getCustomOption().set(Option.PVP, false);

		this.fallingBlocks = new ArrayList<>();

		registerTasks();
	}

	private void registerTasks() {
		getTaskManager().registerTask("every-sec", () -> {
			double spawnRateTime = 1.0 / spawnRate * 20.0;

			for (int i = 0; i < spawnRate; i++) {
				getTaskManager().runTaskLater("spawn-block", (int) (spawnRateTime * i));
			}

			spawnRate += spawnRateIncrease;
		});

		getTaskManager().registerTask("spawn-block", () -> {
			spawnFallingBlock();
		});

	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void onEvent(Event event) {
		if (event instanceof EntityDamageEvent) {
			killPlayer((EntityDamageEvent) event);
		}
	}

	@Override
	protected List<String> registerTutorial() {
		return List.of("Dodge the falling block!");
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

		Map<String, Object> data = getCustomData();

		List<String> blocks = new ArrayList<>();
		blocks.add(Material.GLOWSTONE.name());
		blocks.add(Material.LANTERN.name());
		blocks.add(Material.LANTERN.name());
		blocks.add(Material.CAKE.name());

		data.put("blocks", blocks);
		data.put("spawn-rate", 1);
		data.put("spawn-rate-increase", 1);

		data.put("pos1", getLocation());
		data.put("pos2", getLocation());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = getCustomData();

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

	private void killPlayer(EntityDamageEvent e) {

		if (e.getCause() == DamageCause.FALLING_BLOCK) {
			if (!(e instanceof EntityDamageByEntityEvent)) {
				return;
			}
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;

			Entity fallingBlock = event.getDamager();
			if (this.fallingBlocks.contains(fallingBlock)) {
				Player p = (Player) e.getEntity();

				// give score to others
				getLivePlayers().stream().filter(all -> !all.equals(p)).forEach(all -> plusScore(all, 1));

				// message, sound
				sendMessageToAllPlayers(p.getName() + ChatColor.RED + " died");
				sendTitle(p, ChatColor.RED + "DIE", "");
				getPlayers().forEach(all -> SoundTool.playSound(all, Sound.BLOCK_NOTE_BLOCK_CHIME));

				// live false
				setLive(p, false);
			}
		} else {
			e.setDamage(0);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.fallingBlocks.clear();

		// init spawn rate
		this.spawnRate = (int) getCustomData().get("spawn-rate");

		// start to spawn blocks
		getTaskManager().runTaskTimer("every-sec", 0, 20);

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