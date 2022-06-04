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
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

@SuppressWarnings("deprecation")
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
	protected void initGame() {
		// set score limit
		this.randomTime = (int) (Math.random() * this.getPlayTime());
	}

	protected void registerTasks() {
		// register task
		this.getTaskManager().registerTask("scoreTask", new Runnable() {
			@Override
			public void run() {
				for (Player p : getPlayers()) {
					if (!hasStopped(p)) {
						if (getLeftPlayTime() > randomTime) {
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
	protected void onStart() {
		super.onStart();

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
	protected void onEvent(Event event) {
		if (event instanceof PlayerChatEvent) {
			checkScore((PlayerChatEvent) event);
		} else if (event instanceof PlayerToggleSneakEvent) {
			fixScore((PlayerToggleSneakEvent) event);
		}
	}

	private void checkScore(PlayerChatEvent e) {
		Player p = e.getPlayer();
		int leftChance = this.chance.get(p);
		if (leftChance > 0) {
			int score = this.getScore(p);
			this.sendMessage(p, ChatColor.BOLD + "Current Score: " + ChatColor.GREEN + score);

			// chance -1
			this.chance.put(p, leftChance - 1);

			// msg
			sendMessage(p, "Left change: " + ChatColor.GREEN + this.chance.get(p));

			// sound
			SoundTool.play(p, Sound.BLOCK_NOTE_BLOCK_BELL);
		} else if (leftChance == 0) {
			this.sendMessage(p, "You has no more score checking chance");
		} else if (hasStopped(p)) {
			this.sendMessage(p, "You can't check score until game end");
		}
	}

	private void fixScore(PlayerToggleSneakEvent e) {
		// sneak: stop my score
		Player p = e.getPlayer();

		// set chance to -1
		this.chance.put(p, -1);

		// particle
		ParticleTool.spawn(p.getLocation(), Particle.FLAME);

		// sound
		SoundTool.play(p, Sound.BLOCK_BELL_USE);

		// msg
		sendTitle(p, ChatColor.GREEN + "Fixed", "");
		this.sendMessage(p, "Your score has been stopped");
		sendMessages(ChatColor.GREEN + p.getName() + ChatColor.RESET + " fixed score!");

		if (checkAllFixedScore()) {
			finishGame();
		}
	}

	private boolean checkAllFixedScore() {
		return this.chance.values().stream().filter(v -> v == -1).toList().size() == this.chance.size();
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add(ChatColor.GREEN
				+ "Every 1 sec: score get to plus until random timing and after then score get to minus until the game end");
		tutorial.add("Chat: check current score (chance: 3)");
		tutorial.add("Sneak: Fix score");
		return tutorial;
	}

}
