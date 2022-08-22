package com.worldbiomusic.allgames.games.teambattle;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.minigameworld.frames.TeamBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.Metrics;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class MoreHit extends TeamBattleMiniGame {
	public MoreHit() {
		super("MoreHit", 2, 10, 60, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14389);

		this.setting().setIcon(Material.STICK);

		this.customOption().set(Option.PVP, true);
		this.customOption().set(Option.PVE, false);
		this.setGroupChat(true);
	}

	@GameEvent
	protected void onPlayerHurt(EntityDamageEvent e) {
		// set damage 0
		e.setDamage(0);
	}

	@GameEvent
	protected void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Entity damagerEntity = e.getDamager();
		if (damagerEntity instanceof Player && containsPlayer((Player) damagerEntity)) {
			Player victim = (Player) e.getEntity();
			Player damager = (Player) damagerEntity;

			// if other team
			if (!isSameTeam(victim, damager)) {
				plusTeamScore(damager, 1);
			}
		}
	}

	@GameEvent
	protected void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		e.setRespawnLocation(this.location());
		this.sendMessage(e.getPlayer(), "respawn!");
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Hit other team member: +1");
	}
}