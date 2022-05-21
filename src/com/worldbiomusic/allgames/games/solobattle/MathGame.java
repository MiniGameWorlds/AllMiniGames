package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

public class MathGame extends SoloBattleMiniGame {

	int num1, num2;
	Operator operator;
	int stage, maxStage;

	enum Operator {
		ADD, SUB, MUL, DIV;

		static Operator random() {
			int random = (int) (Math.random() * values().length);
			return values()[random];
		}

		public static String toString(Operator op) {
			switch (op) {
			case ADD:
				return "+";
			case SUB:
				return "-";
			case MUL:
				return "x";
			case DIV:
				return "%";
			default:
				return "error";
			}
		}
	}

	public MathGame() {
		super("MathGame", 2, 4, 60, 15);
		this.maxStage = 5;

		// settings
		this.getCustomOption().set(Option.SCORE_NOTIFYING, true);
	}

	@Override
	protected void initGame() {
		this.resetProblem();
		this.stage = 1;
		this.maxStage = (int) this.getCustomData().get("maxStage");
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();
		Map<String, Object> customData = this.getCustomData();
		customData.put("maxStage", 5);
	}

	void resetProblem() {
		this.num1 = (int) (Math.random() * 100) - 50;
		this.num2 = (int) (Math.random() * 100) - 50;
		this.operator = Operator.random();
	}

	void printProblem() {
		String problem = String.format("(%d) %s (%d) = ?", this.num1, Operator.toString(this.operator), this.num2);
		this.sendMessages(problem);
		this.sendTitles(problem, "", 20, 20 * 3, 20);
	}

	int getAnswer() {
		switch (this.operator) {
		case ADD:
			return this.num1 + this.num2;
		case SUB:
			return this.num1 - this.num2;
		case MUL:
			return this.num1 * this.num2;
		case DIV:
			return this.num1 / this.num2;
		default:
			return 0;
		}
	}

	boolean checkGameEnd() {
		return this.stage > this.maxStage;
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.printProblem();
	}

	@Override
	protected void onEvent(Event event) {
		if (event instanceof AsyncPlayerChatEvent) {
			AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
			Player p = e.getPlayer();
			int answer = 0;
			try {
				answer = Integer.parseInt(e.getMessage());
			} catch (NumberFormatException except) {
				e.setCancelled(true);
				this.sendMessage(p, "enter only Number");
				return;
			}

			if (answer == this.getAnswer()) {
				// hide answer
				e.setCancelled(true);

				// notify
				this.sendMessages(p.getName() + " solved!");

				// plus score
				this.plusScore(p, 1);

				// plus stage
				this.stage += 1;

				// check game is end
				if (this.checkGameEnd()) {
					this.finishGame();
				}

				// print problem
				this.resetProblem();
				this.printProblem();
			} else {
				this.minusScore(p, 1);
			}
		}
	}

	@Override
	protected List<String> tutorial() {
		List<String> list = new ArrayList<String>();
		list.add("Answer: +1");
		list.add("Wrong: -1");
		return list;
	}

}