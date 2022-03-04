package com.worldbiomusic.allgames.games.teambattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.TeamBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;

public class MoreHit extends TeamBattleMiniGame {
	/*
	 * hit to get score
	 */

	public MoreHit() {
		super("MoreHit", 2, 10, 60, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14389);

		this.setGroupChat(true);
		this.getSetting().setIcon(Material.STICK);
		this.getCustomOption().set(Option.PVP, true);
	}

	@Override
	protected void processEvent(Event event) {
		super.processEvent(event);
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			Entity victimEntity = e.getEntity();
			Entity damagerEntity = e.getDamager();
			if (damagerEntity instanceof Player && containsPlayer((Player) damagerEntity)) {
				Player victim = (Player) victimEntity;
				Player damager = (Player) damagerEntity;
				// other team
				if (!this.isSameTeam(victim, damager)) {
					Team team = this.getTeam(damager);
					team.plusTeamScore(1);
					e.setDamage(0);
				}
			}
		} else if (event instanceof PlayerRespawnEvent) {
			PlayerRespawnEvent e = (PlayerRespawnEvent) event;
			e.setRespawnLocation(this.getLocation());
			this.sendMessage(e.getPlayer(), "respawn!");
		}
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Hit other team member: +1");
		return tutorial;
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
