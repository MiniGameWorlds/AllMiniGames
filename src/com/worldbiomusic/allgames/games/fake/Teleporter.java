package com.worldbiomusic.allgames.games.fake;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.minigameworld.frames.FakeMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;

public abstract class Teleporter extends FakeMiniGame {
	public Teleporter(String title) {
		super(title);

		getSetting().setIcon(Material.EMERALD_BLOCK);
		getCustomOption().set(Option.COLOR, ChatColor.GREEN);
	}

	@Override
	protected void onFakeJoin(Player p) {
		// teleport
		p.teleport(getLocation());

		// info
		sendTitle(p, getTitle(), "");
		sendMessage(p, "Moved to " + getCustomOption().get(Option.COLOR) + ChatColor.BOLD + getTitle());

		// sound
		playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT);
	}
}
