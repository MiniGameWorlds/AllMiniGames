package com.worldbiomusic.allgames.games.fake;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.PlayerTool;
import com.minigameworld.frames.FakeMiniGame;

public class Heal extends FakeMiniGame {

	public Heal() {
		super("Heal");

		getSetting().setIcon(Material.CAKE);
	}

	@Override
	protected void onFakeJoin(Player p) {
		PlayerTool.heal(p);

		// title
		sendTitle(p, ChatColor.GREEN + "Healed", "");

		// particle
		ParticleTool.spawn(p.getLocation(), Particle.COMPOSTER, 30, 0.3);

		// sound
		playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
	}

}
