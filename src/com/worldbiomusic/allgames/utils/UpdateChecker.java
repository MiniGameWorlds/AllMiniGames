package com.worldbiomusic.allgames.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.google.common.io.Files;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.minigameworld.util.Utils;

public class UpdateChecker {
	/*
	 * Check version with file in the download directory
	 */
	public static boolean check() {
		PluginDescriptionFile desc = AllMiniGamesMain.getInstance().getDescription();
		String currentVersion = desc.getVersion();
		String pluginName = desc.getName();

		String latestVersion = getLatestVersion(pluginName);
		if (latestVersion == null) {
			Utils.warning(pluginName + " is not exist in the download directory");
			return false;
		}

		boolean isLatest = false;
		isLatest = currentVersion.equals(latestVersion);

		ChatColor currentVersionColor = isLatest ? ChatColor.GREEN : ChatColor.RED;
		ChatColor latestVersionColor = ChatColor.GREEN;

		// print update checkers
		Utils.info("                Update Checker                ");
		Utils.info(" - Current version: " + currentVersionColor + currentVersion);
		Utils.info(" - Latest  version: " + latestVersionColor + latestVersion);

		if (!isLatest) {
			Utils.warning("");
			Utils.warning("Your version is " + currentVersionColor + "outdated");
			Utils.warning(
					"Download latest version: " + "https://github.com/MiniGameWorlds/AllMiniGames/tree/main/download");
		}
		Utils.info(ChatColor.GREEN + "=============================================");

		return isLatest;
	}

	private static String getLatestVersion(String pluginName) {
		// access github anonymously (not connect())
		String latestVersion = null;
		try {
			GitHub github = GitHub.connectAnonymously();
			GHRepository repo = github.getRepositoryById(429867428);

			List<GHContent> files = repo.getDirectoryContent("download");

			for (GHContent file : files) {
				String fileName = file.getName();
				String pureFileName = getFileNameWithoutVersionAndExtension(fileName);
				if (pureFileName.equals(pluginName)) {
					latestVersion = getVersionString(fileName, pluginName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return latestVersion;
	}

	private static String getFileNameWithoutVersionAndExtension(String fileName) {
		return fileName.split("-")[0];
	}

	private static String getVersionString(String fileName, String pluginName) {
		fileName = fileName.replace(pluginName, "").replace("-", "");
		return Files.getNameWithoutExtension(fileName);
	}
}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
