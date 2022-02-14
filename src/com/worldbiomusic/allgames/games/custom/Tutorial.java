package com.worldbiomusic.allgames.games.custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Event;

import com.worldbiomusic.minigameworld.minigameframes.MiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

import net.md_5.bungee.api.ChatColor;

public class Tutorial extends MiniGame {

	public Tutorial() {
		super("Tutorial", 99, 99, 1, 99);

		getSetting().setIcon(Material.BOOK);

		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, ChatColor.BOLD);
		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, false);
		getCustomOption().set(Option.PLAYER_HURT, false);
		getCustomOption().set(Option.PVE, false);

		// register task (for cancel waitingTimer title)
		getTaskManager().registerTask("cancelAllTask", () -> getTaskManager().cancelAllTasks());
	}

	@Override
	protected void initGameSettings() {
		// remove system tasks (_waiting-timer, _finish-timer)
		// game will not finish until the player leave the game
		getTaskManager().runTaskLater("cancelAllTask", 20);
		getTaskManager().runTaskTimer("cancelAllTask", 0, 20 * 5);
	}

	@Override
	protected void processEvent(Event event) {
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutoroial = new ArrayList<>();
		tutoroial.add("Look around the tutorials of minigame system");
		tutoroial.add(ChatColor.BOLD + "Wiki: " + ChatColor.UNDERLINE
				+ "https://github.com/MiniGameWorlds/MiniGameWorld/blob/main/resources/userWiki/Home.md");

		return tutoroial;
	}

	@Override
	public String getFrameType() {
		return "Custom";
	}
}
