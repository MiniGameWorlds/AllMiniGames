package com.worldbiomusic.allgames.games.teambattle;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.TeamBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class MoreHit extends TeamBattleMiniGame {
	public MoreHit() {
		super("MoreHit", 2, 10, 60, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14389);

		this.getSetting().setIcon(Material.STICK);

		this.getCustomOption().set(Option.PVP, true);
		this.getCustomOption().set(Option.PVE, false);
		this.setGroupChat(true);
	}

	@GameEvent
	protected void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Entity victimEntity = e.getEntity();
		Entity damagerEntity = e.getDamager();
		if (damagerEntity instanceof Player && containsPlayer((Player) damagerEntity)) {
			Player victim = (Player) victimEntity;
			Player damager = (Player) damagerEntity;
			
			// set damage 0
			e.setDamage(0);
			
			// if other team
			if (!isSameTeam(victim, damager)) {
				plusTeamScore(damager, 1);
			}
		}
	}

	@GameEvent
	protected void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		e.setRespawnLocation(this.getLocation());
		this.sendMessage(e.getPlayer(), "respawn!");
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Hit other team member: +1");
	}
}