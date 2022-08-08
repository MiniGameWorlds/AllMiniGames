package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.PlayerTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

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

	@GameEvent
	protected void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		Entity damagerEntity = e.getDamager();

		if (!(damagerEntity instanceof Player)) {
			return;
		}

		Player victim = (Player) e.getEntity();
		Player damager = (Player) damagerEntity;

		// check damager is playing this minigame
		if (!containsPlayer(damager)) {
			return;
		}

		// add +1 score to damager
		plusScore(damager, 1);

		// notify
		String victimStr = ChatColor.GRAY + victim.getName() + ChatColor.RESET;
		String damagerStr = ChatColor.RED + damager.getName() + ChatColor.RESET;
		sendMessages(victimStr + " punched " + damagerStr);

		sendTitle(victim, ChatColor.RED + "DIE", "");
		sendTitle(damager, ChatColor.GREEN + "+1", "");

		// sound
		getPlayers().forEach(p -> PlayerTool.playSound(p, Sound.BLOCK_BELL_USE));

		// set victim dead
		setLive(victim, false);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Hit other player: +1");
		tutorial.add("Hit by other player: die");
		return tutorial;
	}

}
