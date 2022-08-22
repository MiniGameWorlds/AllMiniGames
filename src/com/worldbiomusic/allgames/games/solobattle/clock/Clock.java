package com.worldbiomusic.allgames.games.solobattle.clock;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;

public class Clock extends SoloBattleMiniGame {
	private ClockMachine machine;

	public Clock() {
		super("Clock", 2, 10, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 15129);

		this.machine = new ClockMachine(this);

		setting().setIcon(Material.CLOCK);

		customOption().set(Option.PLAYER_HURT, false);
		customOption().set(Option.FOOD_LEVEL_CHANGE, false);
		customOption().set(Option.SCORE_NOTIFYING, false);

		registerTask();
	}

	private void registerTask() {
		taskManager().registerTask("update-hand", () -> {
			this.machine.updateHand();

		});
		taskManager().registerTask("plus-score", () -> {
			livePlayers().forEach(p -> plusScore(p, 1));
		});

	}

	@Override
	protected void initCustomData() {
		Map<String, Object> data = customData();
		data.put("center", location());
		data.put("hand-length", 5.0);
		data.put("hand-speed", 0.0);
		data.put("hand-speed-increment", 0.01);
		data.put("clockwise", true);
		data.put("random-direction-mode", true);
		data.put("particle", Particle.FLAME.name());
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Jump over the clock hand!");
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.machine.init();
		taskManager().runTaskTimer("update-hand", 0, ClockMachine.TICK_RATE);
		taskManager().runTaskTimer("plus-score", 20, 20);
	}

	public void onPlayerCollide(Player player) {
		if (isLive(player)) {
			if (livePlayersCount() == 2) {
				// plus 1 score to the last player
				livePlayers().stream().filter(p -> !p.equals(player)).forEach(p -> plusScore(p, 1));
			}

			sendTitle(player, ChatColor.RED + "DIE", "");
			sendMessages(ChatColor.RED + player.getName() + ChatColor.RESET + " died");
			SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_CHIME);
			


			setLive(player, false);
		}
	}

}
