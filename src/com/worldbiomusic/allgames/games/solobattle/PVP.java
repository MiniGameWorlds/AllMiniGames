package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.PlayerTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class PVP extends SoloBattleMiniGame {
	/*
	 * pvp game
	 * 
	 * rule:
	 * - change dead player's gamemode to spectator
	 * - kit: stone sword(1), bow(1), arrow(32), pork(10), golden apple(1)
	 */

	private double health;
	private List<ItemStack> items;

	public PVP() {
		super("PVP", 2, 10, 60 * 2, 30);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14409);

		this.setting().setIcon(Material.STONE_SWORD);
		this.customOption().set(Option.PVP, true);

		this.customOption().set(Option.COLOR, ChatColor.RED);
	}

	@Override
	protected void initCustomData() {
		Map<String, Object> customData = this.customData();
		customData.put("health", 30);
		List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(Material.STONE_SWORD));
		items.add(new ItemStack(Material.BOW));
		items.add(new ItemStack(Material.ARROW, 32));
		items.add(new ItemStack(Material.COOKED_PORKCHOP, 10));
		items.add(new ItemStack(Material.GOLDEN_APPLE));
		items.add(new ItemStack(Material.WOODEN_AXE));
		customData.put("items", items);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();
		this.health = (int) this.customData().get("health");
		this.items = (List<ItemStack>) this.customData().get("items");
	}

	@Override
	protected void onStart() {
		super.onStart();

		// set health scale, give kit items
		this.players().forEach(p -> initKitsAndHealth(p));
	}

	@GameEvent
	protected void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		e.setRespawnLocation(location());
		initKitsAndHealth(e.getPlayer());
	}

	@GameEvent
	protected void onPlayerDeath(EntityDamageByEntityEvent e) {
		// check killer is playing the same minigame
		if (!(e.getDamager() instanceof Player && this.containsPlayer((Player) e.getDamager()))) {
			return;
		}

		Player victim = (Player) e.getEntity();
		Player killer = (Player) e.getDamager();

		// if death
		if (victim.getHealth() > e.getDamage()) {
			return;
		}

		// killer +1 score
		this.plusScore(killer, 1);

		// cancel damage
		e.setDamage(0);

		// msg
		sendTitle(victim, "Die", "");
		sendMessages(ChatColor.BOLD + victim.getName() + ChatColor.RED + " died");

		// sound
		SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_BELL);

		// particle
		ParticleTool.spawn(victim.getLocation(), Particle.FLAME, 30, 0.1);

		// heal
		PlayerTool.makePureState(victim);

		// tp to the location
		victim.teleport(location());
	}

	private void initKitsAndHealth(Player p) {
		p.setHealthScale(this.health);
		InventoryTool.addItemsToPlayer(p, this.items);

		// set armors
		p.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		p.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
		p.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		p.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
		p.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
	}

	@Override
	protected List<String> tutorial() {
		return List.of("kill: +1", "Teleport to the spawn on death");
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
//
//
