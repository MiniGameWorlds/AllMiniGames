package com.worldbiomusic.allgames.games.fake;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.worldbiomusic.minigameworld.minigameframes.FakeMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

public class Teller extends FakeMiniGame {

	public Teller() {
		super("Teller");

		getSetting().setIcon(Material.BOOK);
		getCustomOption().set(Option.COLOR, ChatColor.AQUA);
	}

	@Override
	protected void onFakeJoin(Player p) {
		// print tutorial
		getTutorial().forEach(t -> sendMessage(p, t));

		// sound
		playSound(p, Sound.ENTITY_ARROW_HIT_PLAYER);
	}

	@Override
	protected List<String> tutorial() {
		return List.of("" + ChatColor.GREEN + ChatColor.BOLD + "[Tutorial]", "This game print tutorial",
				"Edit this minigame tutorial", "");
	}
}
