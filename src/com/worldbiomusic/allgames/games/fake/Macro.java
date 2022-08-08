package com.worldbiomusic.allgames.games.fake;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.minigameworld.frames.FakeMiniGame;

/**
 * Macro features<br>
 * - Player who joined this minigame will perform macro command for each
 * tutorial lines. But if a command starting with "#" then will be executed by
 * the server bukkit which has OP.<br>
 * - Command in tutorial doesn't need to start with slash(/) <br>
 * - Support placeholder: {@code <player> } means joining player's name. <br>
 */
public class Macro extends FakeMiniGame {
	public static final String BUKKIT_CMD_MARK = "#";

	public Macro() {
		super("Macro");

		getSetting().setIcon(Material.BOOKSHELF);
	}

	@Override
	protected void onFakeJoin(Player p) {
		getTutorial().forEach(cmd -> {
			cmd = replacePlaceHolders(cmd, p);

			// bukkit cmd
			if (isBukkitCommand(cmd)) {
				cmd = removeBukkitCommandMark(cmd);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
			// player command
			else {
				p.performCommand(cmd);
			}
		});
	}

	public boolean isBukkitCommand(String command) {
		return command.startsWith(BUKKIT_CMD_MARK);
	}

	public String removeBukkitCommandMark(String command) {
		return command.substring(BUKKIT_CMD_MARK.length());
	}

	public String replacePlaceHolders(String command, Player p) {
		return command.replace("<player>", p.getName());
	}

	@Override
	protected List<String> tutorial() {
		return List.of("help", BUKKIT_CMD_MARK + "say Hi,<player>");
	}

}
