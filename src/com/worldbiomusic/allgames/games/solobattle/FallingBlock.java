package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.LocationTool;
import com.worldbiomusic.minigameworld.MiniGameWorldMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

public class FallingBlock extends SoloBattleMiniGame {
	private Location pos1, pos2;
	private Material removingBlock;

	public FallingBlock() {
		super("FallingBlock", 2, 4, 120, 20);

		// settings
		this.getSetting().setIcon(Material.SAND);

		// options
		this.getCustomOption().set(Option.PVP, true);

		// task
		this.removeBelowBlockTask();
	}

	private void removeBelowBlockTask() {
		this.getTaskManager().registerTask("removeBelowBlock", new Runnable() {

			@Override
			public void run() {
				for (Player p : getPlayers()) {
					if (!isLive(p)) {
						return;
					}

					Location pLoc = p.getLocation();

					// check removingBlock
					Block belowBlock = pLoc.subtract(0, 1, 0).getBlock();

					// check location
					if (!LocationTool.isIn(pos1, belowBlock.getLocation(), pos2)) {
						return;
					}

					if (belowBlock.getType() == removingBlock) {
						Bukkit.getScheduler().runTaskLater(MiniGameWorldMain.getInstance(),
								// remove block with delay
								new Runnable() {
									@Override
									public void run() {
										if (belowBlock.getType() == removingBlock) {
											plusScore(p, 1);
											belowBlock.setType(Material.AIR);
											@SuppressWarnings("deprecation")
											org.bukkit.entity.FallingBlock fallingBlock = p.getWorld()
													.spawnFallingBlock(belowBlock.getLocation().add(0.5, 0, 0.5),
															removingBlock, (byte) 0);
											new BukkitRunnable() {

												@Override
												public void run() {
													fallingBlock.remove();
													fallingBlock.setDropItem(false);
												}
											}.runTaskLater(MiniGameWorldMain.getInstance(), 20);
										}
									}
								}, 5);
					}
				}
			}
		});
	}

	private boolean hasFallen(Player p) {
		Location pLoc = p.getLocation();
		if (pLoc.getY() < this.pos1.getY() - 0.5) {
			return true;
		}
		return false;
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

		this.getCustomData().put("pos1", this.getLocation());
		this.getCustomData().put("pos2", this.getLocation());

		this.getCustomData().put("removingBlock", Material.STONE.name());
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		this.pos1 = (Location) getCustomData().get("pos1");
		this.pos2 = (Location) getCustomData().get("pos2");

		this.removingBlock = Material.valueOf((String) this.getCustomData().get("removingBlock"));
	}

	@Override
	protected void initGameSettings() {
		// fill blocks
		BlockTool.fillBlockWithMaterial(pos1, pos2, this.removingBlock);
	}

	private void processFallenPlayer(Player p) {
		this.setLive(p, false);
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		Location pos1 = (Location) this.getCustomData().get("pos1");
		Location pos2 = (Location) this.getCustomData().get("pos2");

		// fill blocks
		BlockTool.fillBlockWithMaterial(pos1, pos2, this.removingBlock);

		// start remove block task
		this.getTaskManager().runTaskTimer("removeBelowBlock", 0, 2);
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("block will be disappeared after you stepped");
		tutorial.add("remove block with step: +1");
		tutorial.add("fallen: die");
		return tutorial;
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			Player p = e.getPlayer();

			// check has fallen
			if (hasFallen(p)) {
				processFallenPlayer(p);
			}
		}
	}

}
