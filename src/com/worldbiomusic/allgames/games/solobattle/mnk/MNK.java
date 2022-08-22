package com.worldbiomusic.allgames.games.solobattle.mnk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.Metrics;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class MNK extends SoloBattleMiniGame {

	private Board board;

	public MNK() {
		super("MNK", 2, 2, 60 * 10, 10);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14404);

		setting().setIcon(Material.CRAFTING_TABLE);

		registerTasks();
	}

	private void registerTasks() {
		taskManager().registerTask("flowTurnTime", () -> {
			if (board.decreaseLeftTurnTime()) {
				board.changeTurn();
			}
		});

		// for delayed finish
		taskManager().registerTask("finishGame", () -> {
			finishGame();
		});
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();
		data.put("board-pos1", location());
		data.put("board-pos2", location());
		data.put("board-block", Material.OAK_WOOD.name());
		data.put("white-play-block", Material.WHITE_WOOL.name());
		data.put("black-play-block", Material.BLACK_WOOL.name());
		data.put("turn-time", 30);
		data.put("fly", false);
		data.put("length", 5);
		data.put("finish-delay", 10);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();
	}

	@Override
	protected void initGame() {
		Map<String, Object> data = customData();
		Location boardPos1 = (Location) data.get("board-pos1");
		Location boardPos2 = (Location) data.get("board-pos2");
		Material boardBlock = Material.valueOf((String) data.get("board-block"));
		Material whitePlayBlock = Material.valueOf((String) data.get("white-play-block"));
		Material blackPlayBlock = Material.valueOf((String) data.get("black-play-block"));
		int turnTime = (int) data.get("turn-time");
		boolean fly = (boolean) data.get("fly");
		int length = (int) data.get("length");
		int finishDelay = (int) data.get("finish-delay");

		this.board = new Board(this, taskManager(), boardPos1, boardPos2, boardBlock, whitePlayBlock, blackPlayBlock,
				turnTime, fly, length, finishDelay);
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.board.init();
		this.board.registerPlayers(players().get(0), players().get(1));
		this.board.flowTurnTime();
	}

	@GameEvent
	protected void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		this.board.onPlayerDropItemEvent(e);
	}

	@GameEvent
	protected void playBlockPlaced(BlockPlaceEvent e) {
		this.board.playBlockPlaced(e);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Board game in which two players task turns in placing a block of their color on `m by n` board");
		tutorial.add("The winner being the player who first placed k blocks  of their own color in a row");
		tutorial.add("Left turn time shows in player's level");

		return tutorial;
	}

}
