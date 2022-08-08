package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class RPS extends SoloBattleMiniGame {
	/*
	 * Rock Paper Scissor game
	 */

	Map<Player, Selection> selections;

	enum Selection {
		R, S, P;

		static Selection random() {
			int r = (int) (Math.random() * 3);
			if (r == 0) {
				return R;
			} else if (r == 1) {
				return S;
			} else {
				return P;
			}
		}

		static Selection getSelectionWithString(String str) {
			switch (str) {
			case "R":
			case "r":
				return R;
			case "S":
			case "s":
				return S;
			case "P":
			case "p":
				return P;
			default:
				return null;
			}
		}

	}

	public RPS() {
		super("RPS", 2, 2, 30, 15);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14391);

		this.selections = new HashMap<Player, RPS.Selection>();
		this.getSetting().setIcon(Material.SHEARS);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// input random selection
		for (Player p : this.getPlayers()) {
			this.selections.put(p, Selection.random());
		}
	}

	@GameEvent
	protected void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		Selection selection = Selection.getSelectionWithString(msg);

		// put selection and cancel chat event
		if (selection != null) {
			this.selections.put(p, selection);
			e.setMessage(ChatColor.MAGIC + "*");
			this.sendMessage(p, "your choice: " + ChatColor.GREEN + selection.name());

			// sound
			SoundTool.play(p, Sound.BLOCK_NOTE_BLOCK_BELL);

			// particle
			ParticleTool.spawn(p.getLocation(), Particle.FLAME, 30, 0.1);
		}
	}

	Player getWinner(Player p1, Player p2) {
		Selection p1Selection = this.selections.get(p1);
		Selection p2Selection = this.selections.get(p2);

		// DRAW
		if (p1Selection == p2Selection) {
			return null;
		}

		if (p1Selection == Selection.R) {
			if (p2Selection == Selection.S) {
				return p1;
			}
		} else if (p1Selection == Selection.S) {
			if (p2Selection == Selection.P) {
				return p1;
			}
		} else if (p1Selection == Selection.P) {
			if (p2Selection == Selection.R) {
				return p1;
			}
		}
		return p2;
	}

	@Override
	protected void onFinish() {
		this.selections.clear();

		if (!(this.getPlayerCount() == 2)) {
			return;
		}

		Player p1 = this.getPlayers().get(0);
		Player p2 = this.getPlayers().get(1);
		Player winner = this.getWinner(p1, p2);

		// result
		if (winner != null) {
			this.plusScore(winner, 1);
		}

		// print result
		String p1Selection = "" + ChatColor.GREEN + ChatColor.BOLD + this.selections.get(p1).name() + ChatColor.WHITE;
		String p2Selection = "" + ChatColor.GREEN + ChatColor.BOLD + this.selections.get(p2).name() + ChatColor.WHITE;
		this.sendMessages(String.format("%s[%s] : [%s]%s", p1.getName(), p1Selection, p2Selection, p2.getName()));
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Enter chat: " + green("R") + " or " + green("S") + " or " + green("P"));
		tutorial.add("Result will be appeared at the end");
		tutorial.add("Selection can be changed");
		return tutorial;
	}

	private String green(String msg) {
		return ChatColor.GREEN + msg + ChatColor.RESET;
	}
}
