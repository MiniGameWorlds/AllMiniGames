package com.worldbiomusic.allgames.games.solobattle.mnk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;

import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;

public class MNK extends SoloBattleMiniGame {

	private Board board;

	public MNK() {
		super("MNK", 2, 2, 60 * 10, 10);

		getSetting().setIcon(Material.CRAFTING_TABLE);

		registerTasks();
	}

	private void registerTasks() {
		getTaskManager().registerTask("flowTurnTime", new Runnable() {
			@Override
			public void run() {
				if (board.decreaseLeftTurnTime()) {
					board.changeTurn();
				}
			}
		});

		// for delayed finish
		getTaskManager().registerTask("finishGame", () -> {
			finishGame();
		});
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

		Map<String, Object> data = getCustomData();
		data.put("board-pos1", getLocation());
		data.put("board-pos2", getLocation());
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
	protected void initGameSettings() {
		Map<String, Object> data = getCustomData();
		Location boardPos1 = (Location) data.get("board-pos1");
		Location boardPos2 = (Location) data.get("board-pos2");
		Material boardBlock = Material.valueOf((String) data.get("board-block"));
		Material whitePlayBlock = Material.valueOf((String) data.get("white-play-block"));
		Material blackPlayBlock = Material.valueOf((String) data.get("black-play-block"));
		int turnTime = (int) data.get("turn-time");
		boolean fly = (boolean) data.get("fly");
		int length = (int) data.get("length");
		int finishDelay = (int) data.get("finish-delay");

		this.board = new Board(this, getTaskManager(), boardPos1, boardPos2, boardBlock, whitePlayBlock, blackPlayBlock,
				turnTime, fly, length, finishDelay);
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		this.board.init();
		this.board.registerPlayers(getPlayers().get(0), getPlayers().get(1));
		this.board.flowTurnTime();
	}

	@Override
	protected void runTaskAfterFinish() {
		super.runTaskAfterFinish();

		getPlayers().forEach(p -> p.setAllowFlight(false));
	}

	@Override
	protected void processEvent(Event event) {
		this.board.processEvent(event);
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Board game in which two players task turns in placing a block of their color on `m by n` board");
		tutorial.add("The winner being the player who first placed k blocks  of their own color in a row");
		tutorial.add("Left turn time shows in player's level");

		return tutorial;
	}

}