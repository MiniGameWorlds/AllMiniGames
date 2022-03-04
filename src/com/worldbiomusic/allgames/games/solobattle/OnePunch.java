package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;

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

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14405);

		
		getSetting().setIcon(Material.GRASS);
		
		// cancel damage from everything
		getCustomOption().set(Option.PVP, true);
	}
	
	@Override
	protected void initGameSettings() {
		
	}
	
	@Override
	protected void processEvent(Event event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			Entity damagerEntity = e.getDamager();
			
			if (!(damagerEntity instanceof Player)) {
				return;
			}
			
			Player victim = (Player) e.getEntity();
			Player damager = (Player) damagerEntity;
			
			// check damager is a player playing the same minigame
			if (!containsPlayer(damager)) {
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
