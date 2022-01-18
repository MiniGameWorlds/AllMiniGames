package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

/**
 * - All players can be dead with just one punch<br>
 * - Player who hit: +1<br>
 * - Player who hit by other player: die (live to false)<br>
 * - Cancel all damage to a player<br>
 * 
 */
public class OnePunch extends SoloBattleMiniGame {

	public OnePunch() {
		super("OnePunch", 2, 10, 60, 10);

		getSetting().setIcon(Material.GRASS);

		// cancel damage from everything
		getCustomOption().set(Option.PLAYER_HURT, false);
		getCustomOption().set(Option.PVP, false);
		getCustomOption().set(Option.PVE, false);
	}

	@Override
	protected void initGameSettings() {

	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			Entity victimEntity = e.getEntity();
			Entity damagerEntity = e.getDamager();

			if (!(victimEntity instanceof Player && damagerEntity instanceof Player)) {
				return;
			}

			Player victim = (Player) victimEntity;
			Player damager = (Player) damagerEntity;

			// check two players are playing this minigame (because
			// EntityDamageByEntityEvent can be detected two ways)
			if (!(containsPlayer(victim) && containsPlayer(damager))) {
				return;
			}

			// add +1 score to damager
			plusScore(damager, 1);

			// set victim dead
			setLive(victim, false);
		}
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Hit other player: +1");
		tutorial.add("Hit by other player: die");

		return tutorial;
	}

}
