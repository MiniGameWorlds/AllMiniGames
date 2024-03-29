package com.worldbiomusic.allgames.games.team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.TeamMiniGame;
import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.LocationTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class RemoveBlock extends TeamMiniGame {

	/*
	 * Remove all blocks 
	 */
	private Location pos1, pos2;
	private List<Material> blocks;

	public RemoveBlock() {
		super("RemoveBlock", 1, 10, 60 * 3, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14393);

		this.registerTasks();
		this.setting().setIcon(Material.STONE_PICKAXE);
	}

	protected void registerTasks() {
		this.taskManager().registerTask("every5", new Runnable() {
			@Override
			public void run() {
				minusEveryoneScore(1);
			}
		});
	}

	@Override
	protected void initCustomData() {
		Map<String, Object> data = this.customData();

		// block positions
		data.put("pos1", this.location());
		data.put("pos2", this.location());

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
		// hoe
		blocksData.add(Material.HAY_BLOCK.name());

		data.put("blocks", blocksData);
	}

	@Override
	public void loadCustomData() {
		// set positoins
		this.pos1 = (Location) this.customData().get("pos1");
		this.pos2 = (Location) this.customData().get("pos2");

		@SuppressWarnings("unchecked")
		List<String> blocksStr = (List<String>) this.customData().get("blocks");

		this.blocks = new ArrayList<>();
		for (String block : blocksStr) {
			this.blocks.add(Material.valueOf(block));
		}
	}

	@GameEvent
	protected void onBlockBreakEvent(BlockBreakEvent e) {
		Block block = e.getBlock();
		if (LocationTool.isIn(pos1, block.getLocation(), pos2) && this.isTargetBlock(block)) {
			// remove block
			e.getBlock().setType(Material.AIR);

			// sound
			SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_CHIME);

			if (this.checkAllBlocksRemoved()) {
				this.finishGame();
			}
		}
	}

	boolean isTargetBlock(Block target) {
		return this.blocks.contains(target.getType());
	}

	boolean checkAllBlocksRemoved() {
		return BlockTool.isAllSameBlockWithItemStack(this.pos1, this.pos2, new ItemStack(Material.AIR));
	}

	void refillAllBlocks() {
		BlockTool.fillBlockWithRandomMaterial(pos1, pos2, this.blocks);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// give tools
		List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(Material.IRON_AXE));
		items.add(new ItemStack(Material.IRON_PICKAXE));
		items.add(new ItemStack(Material.IRON_SHOVEL));
		items.add(new ItemStack(Material.IRON_SWORD));
		items.add(new ItemStack(Material.IRON_HOE));
		InventoryTool.addItemsToPlayers(this.players(), items);

		// add default score
		this.plusEveryoneScore(this.playTime());

		// start minus score timer every 5 sec
		this.taskManager().runTaskTimer("every5", 0, 20 * 5);

		// refill blocks
		this.refillAllBlocks();
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Game Start: +" + this.playTime());
		tutorial.add("every 5 second: -1");
		tutorial.add("Remove ALL Blocks: Game End");
		return tutorial;
	}

}
