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
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

public class Clock extends SoloBattleMiniGame {
	private ClockMachine machine;

	public Clock() {
		super("Clock", 2, 10, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 15129);

		this.machine = new ClockMachine(this);

		getSetting().setIcon(Material.CLOCK);

		getCustomOption().set(Option.PLAYER_HURT, false);
		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, false);
		getCustomOption().set(Option.SCORE_NOTIFYING, false);

		registerTask();
	}

	private void registerTask() {
		getTaskManager().registerTask("update-hand", () -> {
			this.machine.updateHand();

		});
		getTaskManager().registerTask("plus-score", () -> {
			getLivePlayers().forEach(p -> plusScore(p, 1));
		});

	}

	@Override
	protected void registerCustomData() {
		Map<String, Object> data = getCustomData();
		data.put("center", getLocation());
		data.put("hand-length", 5.0);
		data.put("hand-speed", 0.0);
		data.put("hand-speed-increment", 0.01);
		data.put("clockwise", true);
		data.put("random-direction-mode", true);
		data.put("particle", Particle.FLAME.name());
	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void processEvent(Event event) {
	}

	@Override
	protected List<String> registerTutorial() {
		return List.of("Jump over the clock hand!");
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		this.machine.init();
		getTaskManager().runTaskTimer("update-hand", 0, ClockMachine.TICK_RATE);
		getTaskManager().runTaskTimer("plus-score", 20, 20);
	}

	public void onPlayerCollide(Player player) {
		if (isLive(player)) {
			if (getLivePlayersCount() == 2) {
				// plus 1 score to the last player
				getLivePlayers().stream().filter(p -> !p.equals(player)).forEach(p -> plusScore(p, 1));
			}

			sendTitle(player, ChatColor.RED + "DIE", "");
			sendMessageToAllPlayers(ChatColor.RED + player.getName() + ChatColor.RESET + " died");
			getPlayers().forEach(p -> SoundTool.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL));

			setLive(player, false);
		}
	}

}
