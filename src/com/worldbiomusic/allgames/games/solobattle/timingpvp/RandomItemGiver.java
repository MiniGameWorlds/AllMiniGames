package com.worldbiomusic.allgames.games.solobattle.timingpvp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class RandomItemGiver {
	private List<ItemStack> randomItemList;

	public RandomItemGiver() {
		this.randomItemList = new ArrayList<ItemStack>();
	}

	public void giveRandomItemsToPlayers(List<Player> players) {
		players.forEach(p -> giveRandomItemsToPlayer(p));
	}

	public void giveRandomItemsToPlayer(Player p) {
		PlayerInventory inv = p.getInventory();

		// items
		getRandomItems().forEach(inv::addItem);

		// armors
		List<ItemStack> armors = getRandomArmors();
		inv.setHelmet(armors.get(0));
		inv.setChestplate(armors.get(1));
		inv.setLeggings(armors.get(2));
		inv.setBoots(armors.get(3));

		// off hand item
		inv.setItemInOffHand(getRandomOffHandItem());
	}

	public void setRandomItemList(List<ItemStack> items) {
		this.randomItemList = items;
	}

	public List<ItemStack> getRandomItemList() {
		return this.randomItemList;
	}

	public List<ItemStack> getRandomItems() {
		List<ItemStack> items = new ArrayList<>();

		// select random
		items.add(getRandomItemFromCandidates(this.randomItemList, false));

		return items;
	}

	public List<ItemStack> getRandomArmors() {
		List<ItemStack> items = new ArrayList<>();
		List<ItemStack> candidates = new ArrayList<>();

		// helmet
		candidates.add(new ItemStack(Material.LEATHER_HELMET));
		candidates.add(new ItemStack(Material.IRON_HELMET));
		candidates.add(new ItemStack(Material.GOLDEN_HELMET));
		candidates.add(new ItemStack(Material.DIAMOND_HELMET));
		// select random
		items.add(getRandomItemFromCandidates(candidates));

		// chestplate
		candidates.add(new ItemStack(Material.LEATHER_CHESTPLATE));
		candidates.add(new ItemStack(Material.IRON_CHESTPLATE));
		candidates.add(new ItemStack(Material.GOLDEN_CHESTPLATE));
		candidates.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
		// select random
		items.add(getRandomItemFromCandidates(candidates));

		// leggings
		candidates.add(new ItemStack(Material.LEATHER_LEGGINGS));
		candidates.add(new ItemStack(Material.IRON_LEGGINGS));
		candidates.add(new ItemStack(Material.GOLDEN_LEGGINGS));
		candidates.add(new ItemStack(Material.DIAMOND_LEGGINGS));
		// select random
		items.add(getRandomItemFromCandidates(candidates));

		// boots
		candidates.add(new ItemStack(Material.LEATHER_BOOTS));
		candidates.add(new ItemStack(Material.IRON_BOOTS));
		candidates.add(new ItemStack(Material.GOLDEN_BOOTS));
		candidates.add(new ItemStack(Material.DIAMOND_BOOTS));
		// select random
		items.add(getRandomItemFromCandidates(candidates));

		return items;
	}

	public ItemStack getRandomItemFromCandidates(List<ItemStack> candidates) {
		return getRandomItemFromCandidates(candidates, true);
	}

	public ItemStack getRandomItemFromCandidates(List<ItemStack> candidates, boolean clear) {
		Collections.shuffle(candidates);

		ItemStack item = candidates.get(0);

		if (clear) {
			candidates.clear();
		}

		return item;
	}

	public ItemStack getRandomOffHandItem() {
		ItemStack item = null;
		List<ItemStack> candidates = new ArrayList<>();

		// items
		candidates.add(new ItemStack(Material.SHIELD));
		candidates.add(new ItemStack(Material.FISHING_ROD));
		candidates.add(new ItemStack(Material.ARROW, 32));
		candidates.add(new ItemStack(Material.AIR));

		// select random
		item = getRandomItemFromCandidates(candidates);

		return item;
	}
}

