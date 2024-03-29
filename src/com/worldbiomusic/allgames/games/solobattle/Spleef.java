package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.LocationTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

/**
 * Spleef <br>
 * - SoloBattle <br>
 * - Check fallen from floor with timer task<br>
 * - Player will fall if below of y of custom-data.pos2<br>
 * - Gets score when break a block <br>
 * - Can set floor area with custom-data <br>
 * - Can set block, tool with custom-data <br>
 * - Fill floor in initGame() <br>
 * - PVP off <br>
 *
 */
public class Spleef extends SoloBattleMiniGame {

	private Material block;
	private Material tool;
	private Location pos1, pos2;

	public Spleef() {
		super("Spleef", 2, 10, 300, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14410);

		setting().setIcon(Material.STONE_SHOVEL);

		customOption().set(Option.COLOR, ChatColor.WHITE);
		customOption().set(Option.PVP, false);
		customOption().set(Option.PVE, false);
		customOption().set(Option.PLAYER_HURT, false);

		registerTask();
	}

	private void registerTask() {
		taskManager().registerTask("check-fallen", () -> {
			livePlayers().forEach(p -> checkFallenFromFloor(p));
		});
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();
		data.put("block", Material.SNOW_BLOCK.name());
		data.put("tool", Material.STONE_SHOVEL.name());
		data.put("pos1", location());
		data.put("pos2", location());
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = customData();
		this.block = Material.valueOf((String) data.get("block"));
		this.tool = Material.valueOf((String) data.get("tool"));
		this.pos1 = (Location) data.get("pos1");
		this.pos2 = (Location) data.get("pos2");
	}

	@Override
	protected void onStart() {
		super.onStart();

		taskManager().runTaskTimer("check-fallen", 0, 10);
		InventoryTool.addItemToPlayers(players(), new ItemStack(this.tool));
	}

	private void fillStage() {
		BlockTool.fillBlockWithMaterial(pos1, pos2, block);
	}

	private void checkFallenFromFloor(Player p) {
		double bottomY = this.pos2.getY();
		double playerY = p.getLocation().getY();

		if (playerY <= bottomY) {
			sendTitle(p, ChatColor.RED + "DIE", "");
			sendMessages(ChatColor.RED + p.getName() + ChatColor.RESET + " died");
			SoundTool.play(players(), Sound.BLOCK_BELL_USE);
			ParticleTool.spawn(p.getLocation(), Particle.FLAME, 30, 0.1);
			setLive(p, false);
		}
	}

	@Override
	protected void initGame() {
		fillStage();
	}

	@GameEvent
	protected void onBlockBreakEvent(BlockBreakEvent e) {
		Block block = e.getBlock();

		if (!LocationTool.isIn(pos1, block.getLocation(), pos2)) {
			return;
		}

		Material blockType = block.getType();
		if (blockType != this.block) {
			return;
		}

		Player p = e.getPlayer();
		plusScore(p, 1);
		block.setType(Material.AIR);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Break block: " + ChatColor.GREEN + "+1");
		tutorial.add("Fallen: " + ChatColor.RED + "die");
		return tutorial;
	}

}
