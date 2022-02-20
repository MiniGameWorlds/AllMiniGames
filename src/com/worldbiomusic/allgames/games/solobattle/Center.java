package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.wbm.plugin.util.PlayerTool;
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

			getLivePlayers().forEach(p -> {
				if (checkSneaking(p) || checkFallen(p)) {
					// sound
					getPlayers().forEach(all -> PlayerTool.playSound(all, Sound.BLOCK_BELL_USE));

					sendMessageToAllPlayers(p.getName() + ChatColor.RED + " died");

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
			sendTitle(p, ChatColor.RED + "Die", "You sneaked");
			return true;
		}
		return false;
	}

	private boolean checkFallen(Player p) {
		Location pLoc = p.getLocation();
		if (pLoc.getY() < getLocation().getY() - 0.3) {
			// notify
			sendTitle(p, ChatColor.RED + "Die", "You are fallen");
			return true;
		}
		return false;
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();
		getTaskManager().runTaskTimer("check-sneaking-and-fallen", 0, 5);
	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void processEvent(Event event) {
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<>();

		tutorial.add(ChatColor.RED + "Never Sneak!");
		tutorial.add(ChatColor.RED + "Never FALL!");

		return tutorial;
	}

}
