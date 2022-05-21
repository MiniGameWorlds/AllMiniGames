package com.worldbiomusic.allgames.games.solobattle;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

/**
 * [Rules]<br>
 * - A player will die if sneaks or fall from the blocks<br>
 * - PVP off <br>
 * - Check player is fallen with minigame "location"
 */
public class Center extends SoloBattleMiniGame {

	public Center() {
		super("Center", 2, 5, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14413);

		getSetting().setIcon(Material.END_ROD);

		getCustomOption().set(Option.PVP, false);
		getCustomOption().set(Option.COLOR, ChatColor.BLUE);
		getCustomOption().set(Option.SCORE_NOTIFYING, false);

		registerTasks();
	}

	private void registerTasks() {
		getTaskManager().registerTask("check-sneaking-and-fallen", () -> {

			// plus score
			getLivePlayers().forEach(p -> {
				plusScore(p, 1);
			});

			// check fallen and sneaking
			getLivePlayers().forEach(p -> {
				if (checkSneaking(p) || checkFallen(p)) {
					// msg
					sendMessages(p.getName() + ChatColor.RED + " died");

					// sound
					SoundTool.play(getPlayers(), Sound.BLOCK_BELL_USE);

					// minus score
					minusScore(p, 1);

					setLive(p, false);
				}
			});
		});
	}

	private boolean checkSneaking(Player p) {
		if (p.isSneaking()) {
			// notify
			sendTitle(p, ChatColor.RED + "Die", "You sneaked!");
			return true;
		}
		return false;
	}

	private boolean checkFallen(Player p) {
		Location pLoc = p.getLocation();
		if (pLoc.getY() < getLocation().getY() - 0.3) {
			// notify
			sendTitle(p, ChatColor.RED + "Die", "You are fallen!");
			return true;
		}
		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();
		getTaskManager().runTaskTimer("check-sneaking-and-fallen", 0, 5);
	}

	@Override
	protected void initGame() {
	}

	@Override
	protected void onEvent(Event event) {
	}

	@Override
	protected List<String> tutorial() {
		return List.of(ChatColor.RED + "Never Sneak!", ChatColor.RED + "Never Fall!");
	}

}
