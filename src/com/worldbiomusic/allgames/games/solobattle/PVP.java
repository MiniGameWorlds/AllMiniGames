package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.wbm.plugin.util.InventoryTool;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;


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
		super("PVP", 2, 5, 60 * 2, 30);
		this.getSetting().setIcon(Material.STONE_SWORD);
		this.getCustomOption().set(Option.PVP, true);

		this.getCustomOption().set(Option.COLOR, ChatColor.RED);
	}

	@Override
	protected void registerCustomData() {
		Map<String, Object> customData = this.getCustomData();
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
		this.health = (int) this.getCustomData().get("health");
		this.items = (List<ItemStack>) this.getCustomData().get("items");
	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();
		// set health scale, give kit items
		this.getPlayers().forEach(p -> initKitsAndHealth(p));
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void processEvent(Event event) {
		if (event instanceof PlayerRespawnEvent) {
			PlayerRespawnEvent e = (PlayerRespawnEvent) event;
			e.setRespawnLocation(this.getLocation());
			this.initKitsAndHealth(e.getPlayer());
		} else if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();

				// if death
				if (p.getHealth() <= e.getDamage()) {

					// check killer is playing the same minigame
					if (e.getDamager() instanceof Player || this.containsPlayer((Player) e.getDamager())) {
						// killer +1 score
						Player killer = (Player) e.getDamager();
						this.plusScore(killer, 1);

						// heal health, food level
						e.setDamage(0);
						this.sendTitle(p, "Die", "");
						p.setHealth(p.getMaxHealth());
						p.setFoodLevel(20);
					}

				}
			}
		}
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
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("kill: +1");
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
//
//
