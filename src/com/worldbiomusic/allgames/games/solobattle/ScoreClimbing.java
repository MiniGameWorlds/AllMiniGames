package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.worldbiomusic.minigameworld.util.Metrics;

import net.md_5.bungee.api.ChatColor;

public class ScoreClimbing extends SoloBattleMiniGame {
	/*
	 * Random score graph
	 * - Jump: check graph score
	 * - Sneak: stop my score
	 */
	int randomTime;
	Map<Player, Integer> chance;

	public ScoreClimbing() {
		super("ScoreClimbing", 2, 10, 60, 20);
		
		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14390);

		this.chance = new HashMap<Player, Integer>();

		this.getSetting().setIcon(Material.OAK_STAIRS);

		this.getCustomOption().set(Option.SCORE_NOTIFYING, false);

		this.registerTasks();
	}

	@Override
	protected void initGameSettings() {
		// set score limit
		this.randomTime = (int) (Math.random() * this.getTimeLimit());
	}

	protected void registerTasks() {
		// register task
		this.getTaskManager().registerTask("scoreTask", new Runnable() {
			@Override
			public void run() {
				for (Player p : getPlayers()) {
					if (!hasStopped(p)) {
						if (getLeftFinishTime() > randomTime) {
							plusScore(p, 1);
						} else {
							minusScore(p, 1);
						}
					}
				}
			}
		});
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		// 3 chances
		this.chance.clear();
		this.getPlayers().forEach(p -> chance.put(p, 3));

		// timer task
		this.getTaskManager().runTaskTimer("scoreTask", 0, 20);
	}

	private boolean hasStopped(Player p) {
		return this.chance.get(p) == -1;
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof PlayerChatEvent) {
			PlayerChatEvent e = (PlayerChatEvent) event;
			Player p = e.getPlayer();
			int leftChance = this.chance.get(p);
			if (leftChance > 0) {
				int score = this.getScore(p);
				this.sendMessage(p, ChatColor.BOLD + "Current Score: " + ChatColor.GREEN + score);
				// chance -1
				this.chance.put(p, leftChance - 1);
			} else if (leftChance == 0) {
				this.sendMessage(p, "You has no more score checking chance");
			} else if (hasStopped(p)) {
				this.sendMessage(p, "You can't check score until game end");
			}
		} else if (event instanceof PlayerToggleSneakEvent) {
			// sneak: stop my score
			PlayerToggleSneakEvent e = (PlayerToggleSneakEvent) event;
			Player p = e.getPlayer();

			this.sendMessage(p, "Your score has been stopped");

			// set chance to -1
			this.chance.put(p, -1);

			if (checkAllGetScore()) {
				finishGame();
			}
		}
	}

	private boolean checkAllGetScore() {
		return this.chance.values().stream().filter(v -> v == -1).toList().size() == this.chance.size();
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add(
				"Every 1 sec: score get to plus until random timing and after then score get to minus until the game end");
		tutorial.add("Chat: check current score(max: 3)");
		tutorial.add("Sneak: stop Game and check score");
		return tutorial;
	}

}
