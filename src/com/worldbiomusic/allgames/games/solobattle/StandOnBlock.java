package com.worldbiomusic.allgames.games.solobattle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.PlayerTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

/*
 * [Rules]
 * - Player will die if fall from the blocks
 * - Only a random block type will remain
 * 
 *   [Custom Data]
 *   - blocks (List<Material)
 *   - disappear-delay (decimal)
 *   - disappear-delay-decrease (decimal)
 *   - pos1 (Location)
 *   - pos2 (Location)
 *   - void-time (decimal)
 */
public class StandOnBlock extends SoloBattleMiniGame {

	private List<Material> blocks;
	private double disappearDelay;
	private double disappearDelayDecrease;
	private Location pos1, pos2;
	private double voidTime;

	private double delay;
	private Material remainBlock;
	private double counter;

	public StandOnBlock() {
		super("StandOnBlock", 2, 10, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14573);

		setting().setIcon(Material.ARMOR_STAND);

		customOption().set(Option.COLOR, ChatColor.GREEN);
		customOption().set(Option.FOOD_LEVEL_CHANGE, false);
		customOption().set(Option.PVP, false);

		registerTasks();
	}

	private void registerTasks() {
		taskManager().registerTask("remove-blocks", () -> {
			removeBlocks();

			if (this.delay > this.disappearDelayDecrease) {
				this.delay -= this.disappearDelayDecrease;
			}

			taskManager().runTaskLater("fill-blocks", (int) (20 * voidTime));
		});

		taskManager().registerTask("fill-blocks", () -> {
			fillRandomBlocks();
			readyNextRound();

			// count down
			this.counter = this.delay;
			taskManager().runTaskTimer("count-down", 0, 2);

			// remove blocks
			taskManager().runTaskLater("remove-blocks", (int) (20 * this.delay));
		});

		taskManager().registerTask("check-players", () -> {
			livePlayers().forEach(p -> checkPlayerIsFallen(p));
		});

		taskManager().registerTask("count-down", () -> {
			String counterStr = "" + new DecimalFormat("#.#").format(this.counter);
			sendTitles(counterStr, ChatColor.GREEN + remainBlock.name(), 0, 3, 0);
			this.counter -= 0.1;

			if (this.counter <= 0) {
				taskManager().cancelTask("count-down");
			}
		});
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();

		List<String> defaultBlocks = new ArrayList<>();
		defaultBlocks.add(Material.RED_WOOL.name());
		defaultBlocks.add(Material.ORANGE_WOOL.name());
		defaultBlocks.add(Material.YELLOW_WOOL.name());
		defaultBlocks.add(Material.GREEN_WOOL.name());
		defaultBlocks.add(Material.BLUE_WOOL.name());
		defaultBlocks.add(Material.PURPLE_WOOL.name());
		defaultBlocks.add(Material.WHITE_WOOL.name());
		defaultBlocks.add(Material.BLACK_WOOL.name());
		data.put("blocks", defaultBlocks);

		data.put("pos1", location());
		data.put("pos2", location());

		data.put("disappear-delay", 5.0);
		data.put("disappear-delay-decrease", 0.2);
		data.put("void-time", 3.0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = customData();
		this.blocks = new ArrayList<>();
		((List<String>) data.get("blocks")).forEach(e -> this.blocks.add(Material.valueOf(e)));

		this.pos1 = (Location) data.get("pos1");
		this.pos2 = (Location) data.get("pos2");

		this.disappearDelay = (double) data.get("disappear-delay");
		this.disappearDelayDecrease = (double) data.get("disappear-delay-decrease");
		this.voidTime = (double) data.get("void-time");
	}

	@Override
	protected void initGame() {
		this.delay = this.disappearDelay;
	}

	@GameEvent
	protected void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("There are random blocks");
		tutorial.add("Only one block will be remained while other blocks will be disappeared");
		tutorial.add("Never fall from the block");
		return tutorial;
	}

	private void checkPlayerIsFallen(Player p) {
		double pY = p.getLocation().getY();
		double blockY = this.pos1.getY() - 0.3;

		if (pY < blockY) {
			livePlayers().stream().filter(all -> !all.equals(p)).forEach(all -> plusScore(all, 1));

			sendTitle(p, ChatColor.RED + "DIE", "", 10, 20, 10);
			sendMessages(ChatColor.RED + p.getName() + ChatColor.RESET + " died");
			SoundTool.play(players(), Sound.BLOCK_BELL_USE);

			setLive(p, false);
		}
	}

	private void removeBlocks() {
		// remove other blocks
		BlockTool.remainBlocks(pos1, pos2, Arrays.asList(new Material[] { this.remainBlock }));

		// sound
		players().forEach(p -> PlayerTool.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT));
	}

	private void fillRandomBlocks() {
		BlockTool.fillBlockWithRandomMaterial(pos1, pos2, blocks);

		// sound
		players().forEach(p -> PlayerTool.playSound(p, Sound.BLOCK_NOTE_BLOCK_CHIME));
	}

	private void readyNextRound() {
		// set and inform next remain block
		int r = (int) (Math.random() * this.blocks.size());
		this.remainBlock = this.blocks.get(r);

		infoRemainBlock();

	}

	private void infoRemainBlock() {
		livePlayers().forEach(p -> {
			p.getInventory().clear();
			for (int i = 0; i < 9; i++) {
				p.getInventory().setItem(i, new ItemStack(this.remainBlock));
			}
		});

		// title and sound
		players().forEach(p -> {
			PlayerTool.playSound(p, Sound.BLOCK_BELL_USE);
			sendTitle(p, "", ChatColor.GREEN + remainBlock.name(), 15, 30, 15);
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		fillRandomBlocks();

		taskManager().runTask("fill-blocks");
		taskManager().runTaskTimer("check-players", 0, 5);
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		fillRandomBlocks();
	}

}
