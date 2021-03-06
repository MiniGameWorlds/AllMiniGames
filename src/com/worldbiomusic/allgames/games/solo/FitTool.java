package com.worldbiomusic.allgames.games.solo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.LocationTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloMiniGame;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.SoundTool;

public class FitTool extends SoloMiniGame {
	/*
	 * Break blocks with fit tools
	 */

	private List<Material> blocks;
	private Location pos1, pos2;

	public FitTool() {
		super("FitTool", 30, 10);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14387);

		this.getSetting().setIcon(Material.STONE_PICKAXE);
	}

	@Override
	protected void initGame() {
		// fill blocks
		BlockTool.fillBlockWithRandomMaterial(this.pos1, this.pos2, this.blocks);
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = this.getCustomData();

		// Blocks
		// save with String (Material doesn't implement ConfigurationSerialization)
		List<String> blocksData = new ArrayList<>();
		// sword
		blocksData.add(Material.COBWEB.name());
		// axe
		blocksData.add(Material.OAK_WOOD.name());
		// pickaxe
		blocksData.add(Material.COBBLESTONE.name());
		// shovel
		blocksData.add(Material.DIRT.name());

		data.put("blocks", blocksData);

		// blocks location
		data.put("pos1", this.getLocation());
		data.put("pos2", this.getLocation());
	}

	private Material getRandomBlock() {
		int r = (int) (Math.random() * this.blocks.size());
		return this.blocks.get(r);
	}

	@Override
	public void loadCustomData() {
		this.blocks = new ArrayList<>();
		// blocks
		@SuppressWarnings("unchecked")
		List<String> blocksStr = (List<String>) this.getCustomData().get("blocks");

		for (String block : blocksStr) {
			this.blocks.add(Material.valueOf(block));
		}

		// blocks location
		this.pos1 = (Location) this.getCustomData().get("pos1");
		this.pos2 = (Location) this.getCustomData().get("pos2");
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof BlockBreakEvent) {
			BlockBreakEvent e = (BlockBreakEvent) event;
			Player p = e.getPlayer();

			Block b = e.getBlock();

			// plus score with specific block
			if (LocationTool.isIn(pos1, b.getLocation(), pos2) && this.blocks.contains(b.getType())) {
				e.setCancelled(true);
				this.plusScore(p, 1);

				// random block
				b.setType(this.getRandomBlock());

				// sound
				SoundTool.play(b.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL);
			}
		}
	}

	@Override
	protected void onStart() {
		// give tools
		for (Player p : this.getPlayers()) {
			p.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
			p.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
			p.getInventory().addItem(new ItemStack(Material.IRON_AXE));
			p.getInventory().addItem(new ItemStack(Material.IRON_SHOVEL));
		}

	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Break blocks with fit tools");
		tutorial.add("Breaking block: +1");
		return tutorial;
	}

}
