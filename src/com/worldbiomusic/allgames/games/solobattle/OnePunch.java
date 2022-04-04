package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.PlayerTool;

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
		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, false);
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

			Player victim = (Player) e.getEntity();
			Player damager = (Player) damagerEntity;

			// check victim and damager is a player playing this minigame
			if (!(containsPlayer(victim) && containsPlayer(damager))) {
				return;
			}

			// add +1 score to damager
			plusScore(damager, 1);

			// notify
			String victimStr = ChatColor.GRAY + victim.getName() + ChatColor.RESET;
			String damagerStr = ChatColor.RED + victim.getName() + ChatColor.RESET;
			sendMessageToAllPlayers(victimStr + "이(가) " + damagerStr + "에게 탈락 당했습니다");

			sendTitle(victim, ChatColor.RED + "DIE", "");
			sendTitle(damager, ChatColor.GREEN + "+1", "");

			// sound
			getPlayers().forEach(p -> PlayerTool.playSound(p, Sound.BLOCK_BELL_USE));

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
