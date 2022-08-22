package com.worldbiomusic.allgames.games.fake;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.minigameworld.frames.FakeMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;

public class Teller extends FakeMiniGame {

	public Teller() {
		super("Teller");

		setting().setIcon(Material.BOOK);
		customOption().set(Option.COLOR, ChatColor.AQUA);
	}

	@Override
	protected void onFakeJoin(Player p) {
		// print tutorial
		tutorials().forEach(t -> sendMessage(p, t));

		// sound
		playSound(p, Sound.ENTITY_ARROW_HIT_PLAYER);
	}

	@Override
	protected List<String> tutorial() {
		return List.of("" + ChatColor.GREEN + ChatColor.BOLD + "[Tutorial]", "This game print tutorial",
				"Edit this minigame tutorial", "");
	}
}
