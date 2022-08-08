package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class RandomScore extends SoloBattleMiniGame {
	/*
	 * Get random score when sneaking
	 */

	private List<Integer> randomScores;

	public RandomScore() {
		super("RandomScore", 2, 10, 20, 10);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14388);

		this.randomScores = new ArrayList<Integer>();
		this.getSetting().setIcon(Material.DISPENSER);
		getSetting().setScoreboard(false);
	}

	@Override
	protected void initGame() {
		this.randomScores.clear();
		for (int i = 1; i <= 10; i++) {
			this.randomScores.add(i);
		}
	}

	@GameEvent
	protected void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		// first sneaking
		if (this.getScore(p) == 0) {
			int randomIndex = (int) (Math.random() * this.randomScores.size());
			int randomScore = this.randomScores.remove(randomIndex);
			this.plusScore(p, randomScore);

			// sound
			SoundTool.play(p, Sound.BLOCK_NOTE_BLOCK_BELL);

			// particle
			ParticleTool.spawn(p.getLocation(), Particle.FLAME, 30, 0.1);

			// msg
			sendMessages(ChatColor.GREEN + p.getName() + ChatColor.RESET + " gets random score");
		}
	}

	@Override
	protected List<String> tutorial() {
		return List.of(ChatColor.GREEN + "Sneak to get random score");
	}

}
