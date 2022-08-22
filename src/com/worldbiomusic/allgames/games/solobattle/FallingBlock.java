package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.minigameworld.MiniGameWorldMain;
import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.LocationTool;
import com.wbm.plugin.util.Metrics;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class FallingBlock extends SoloBattleMiniGame {
	private Location pos1, pos2;
	private Material removingBlock;

	private List<Entity> landedFallenBlocks;

	public FallingBlock() {
		super("FallingBlock", 2, 10, 120, 20);
		this.landedFallenBlocks = new ArrayList<>();

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14398);

		// settings
		this.setting().setIcon(Material.SAND);

		// options
		this.customOption().set(Option.PVP, true);

		// task
		this.removeBelowBlockTask();
	}

	private void removeBelowBlockTask() {
		this.taskManager().registerTask("remove-below-block", () -> {

			for (Player p : livePlayers()) {
				Location pLoc = p.getLocation();

				// check removingBlock
				Block belowBlock = pLoc.subtract(0, 1, 0).getBlock();

				// check location
				if (!LocationTool.isIn(pos1, belowBlock.getLocation(), pos2)) {
					return;
				}

				if (belowBlock.getType() != removingBlock) {
					return;
				}

				Bukkit.getScheduler().runTaskLater(MiniGameWorldMain.getInstance(), () -> {
					plusScore(p, 1);
					belowBlock.setType(Material.AIR);
					@SuppressWarnings("deprecation")
					org.bukkit.entity.FallingBlock fallingBlock = p.getWorld()
							.spawnFallingBlock(belowBlock.getLocation().add(0.5, 0, 0.5), removingBlock, (byte) 0);

					// add fallingBlock to list
					landedFallenBlocks.add(fallingBlock);

					// remove fallen block
					removeFallenBlock(fallingBlock);
				}, 5);
			}
		});

		taskManager().registerTask("check-fallen", () -> {
			// only check live players
			livePlayers().stream().filter(p -> isFallen(p)).forEach(p -> onPlayerFall(p));
		});

	}

	private void removeFallenBlock(org.bukkit.entity.FallingBlock fallingBlock) {
		new BukkitRunnable() {
			@Override
			public void run() {
				// remove falling block from the list
				landedFallenBlocks.remove(fallingBlock);

				// remove
				fallingBlock.remove();
				fallingBlock.setDropItem(false);
			}
		}.runTaskLater(MiniGameWorldMain.getInstance(), 20);
	}

	private boolean isFallen(Player p) {
		Location pLoc = p.getLocation();
		if (pLoc.getY() < this.pos1.getY() - 0.5) {
			return true;
		}
		return false;
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		this.customData().put("pos1", this.location());
		this.customData().put("pos2", this.location());

		this.customData().put("removing-block", Material.STONE.name());
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		this.pos1 = (Location) customData().get("pos1");
		this.pos2 = (Location) customData().get("pos2");

		this.removingBlock = Material.valueOf((String) this.customData().get("removing-block"));
	}

	@Override
	protected void initGame() {
		// fill blocks
		BlockTool.fillBlockWithMaterial(pos1, pos2, this.removingBlock);

		this.landedFallenBlocks.clear();
	}

	private void onPlayerFall(Player p) {
		setLive(p, false);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// start remove block task
		taskManager().runTaskTimer("remove-below-block", 0, 3);

		taskManager().runTaskTimer("check-fallen", 0, 10);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("block will be disappeared after you stepped");
		tutorial.add("remove block with step: +1");
		tutorial.add("fallen: die");
		return tutorial;
	}

	@GameEvent
	protected void onEntityChangeBlockEvent(EntityChangeBlockEvent e) {
		removeFallenBlock(e);
	}

	private void removeFallenBlock(EntityChangeBlockEvent e) {
		Entity entity = e.getEntity();
		if (this.landedFallenBlocks.contains(entity)) {
			// cancel event
			e.setCancelled(true);

			// remove entity
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
//
//
//
//
//
//
//
//
//
//
//
//
//
//
