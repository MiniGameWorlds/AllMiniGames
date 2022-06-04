package com.worldbiomusic.allgames.games.solobattle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wbm.plugin.util.Metrics;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

public class HungryFishing extends SoloBattleMiniGame {

	private Map<Material, Integer> catchItems;
	private int hunger;
	private int failHunger;

	public HungryFishing() {
		super("HungryFishing", 2, 10, 60 * 5, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 15375);

		// settings
		getSetting().setIcon(Material.FISHING_ROD);
		getSetting().setGameFinishConditionPlayerCount(1);

		// options
		getCustomOption().set(Option.COLOR, ChatColor.AQUA);
		getCustomOption().set(Option.PLAYER_HURT, false);
		getCustomOption().set(Option.PVP, false);
		getCustomOption().set(Option.PVE, false);
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = getCustomData();

		// catch-items
		Map<String, Integer> itemList = new HashMap<>();
		itemList.put(Material.COOKIE.name(), 40);
		itemList.put(Material.MELON_SLICE.name(), 30);
		itemList.put(Material.CARROT.name(), 20);
		itemList.put(Material.COOKED_PORKCHOP.name(), 10);
		data.put("catch-items", itemList);

		// hunger
		data.put("hunger", 1);

		// fail-hunger
		data.put("fail-hunger", 2);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = getCustomData();

		// catch-items
		this.catchItems = new HashMap<>();
		Map<String, Integer> itemList = (Map<String, Integer>) data.get("catch-items");
		itemList.forEach((k, v) -> catchItems.put(Material.valueOf(k), v));

		// hunger
		this.hunger = (int) data.get("hunger");

		// fail-hunger
		this.failHunger = (int) data.get("fail-hunger");
	}

	@Override
	protected void initGame() {
	}

	@Override
	protected void onEvent(Event event) {
		if (event instanceof PlayerFishEvent) {
			onFishEvent((PlayerFishEvent) event);
		} else if (event instanceof FoodLevelChangeEvent) {
			onFoodLevelChange((FoodLevelChangeEvent) event);
		}
	}

	void onFishEvent(PlayerFishEvent event) {
		State state = event.getState();

		if (state == State.CAUGHT_FISH) {
			onCatch(event);
		} else if (state == State.FAILED_ATTEMPT) {
			onFail(event);
		}
	}

	void onCatch(PlayerFishEvent event) {
		Player p = event.getPlayer();

		Entity entity = event.getCaught();
		ItemStack item = ((Item) entity).getItemStack();

		// replace with random item
		item.setType(getRandomItem());

		// send msg and title
		sendMessages(p.getName() + ChatColor.GREEN + " caught a item");
		sendTitle(p, ChatColor.GREEN + "Catch!", "");

		// sound
		p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 10.0F, 1.0F);
	}

	Material getRandomItem() {
		int random = new Random().nextInt(100);

		int range = 0;
		for (Entry<Material, Integer> entry : this.catchItems.entrySet()) {
			Material item = entry.getKey();
			int percent = entry.getValue();

			range += percent;

			if (random < range) {
				return item;
			}
		}

		// for safe
		return Material.COOKIE;
	}

	void onFail(PlayerFishEvent event) {
		Player p = event.getPlayer();

		// decrease player hunger
		int playerHunger = p.getFoodLevel() - this.failHunger;
		if (playerHunger < 1) {
			playerHunger = 1;
		}
		p.setFoodLevel(playerHunger);

		// send msg and title
		sendMessages(p.getName() + ChatColor.RED + " failed to catch item");
		sendMessage(p, "You lost " + this.failHunger + " hunger");
		sendTitle(p, ChatColor.RED + "Failed", "");

		// sound
		p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 10.0F, 1.0F);
	}

	void onFoodLevelChange(FoodLevelChangeEvent event) {
		Player p = (Player) event.getEntity();
		int foodLevel = event.getFoodLevel();

		// check food level is max
		if (foodLevel >= 20) {
			// give score
			plusScore(p, getLeftPlayTime());

			// msg, sound
			sendMessages(ChatColor.GREEN + p.getName() + " filled all hunger!");
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 10.0F, 1.0F);

			// change player to spectator
			setLive(p, false);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		getPlayers().forEach(p -> {
			// set start hungry level
			p.setFoodLevel(this.hunger);

			// give fishing rod
			ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
			ItemMeta meta = fishingRod.getItemMeta();
			meta.setUnbreakable(true);
			fishingRod.setItemMeta(meta);
			p.getInventory().addItem(fishingRod);
		});
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Fish and fill your hunger with items!", "If you fail to catch, you will be hungry");
	}

}
