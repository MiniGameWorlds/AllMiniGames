package com.worldbiomusic.allgames.games.custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.minigameworld.frames.MiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;
import com.worldbiomusic.allgames.AllMiniGamesMain;

import net.md_5.bungee.api.ChatColor;

public class Tutorial extends MiniGame {

	public Tutorial() {
		super("Tutorial", 99, 99, 1, 99);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14411);

		getSetting().setIcon(Material.BOOK);

		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, ChatColor.BOLD);
		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, false);
		getCustomOption().set(Option.PLAYER_HURT, false);
		getCustomOption().set(Option.PVE, false);

		// register task (for cancel waitingTimer title)
		getTaskManager().registerTask("cancelAllTask", () -> getTaskManager().cancelAllTasks());
	}

	@Override
	protected void initGame() {
		// remove system tasks (_waiting-timer, _finish-timer)
		// game will not finish until the player leave the game
		getTaskManager().runTaskLater("cancelAllTask", 20);
		getTaskManager().runTaskTimer("cancelAllTask", 0, 20 * 5);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Look around the tutorials of minigame system");
		tutorial.add(ChatColor.BOLD + "Wiki: " + ChatColor.UNDERLINE
				+ "https://github.com/MiniGameWorlds/MiniGameWorld/blob/main/resources/userWiki/Home.md");

		return tutorial;
	}

	@Override
	public String getFrameType() {
		return "Custom";
	}
}
