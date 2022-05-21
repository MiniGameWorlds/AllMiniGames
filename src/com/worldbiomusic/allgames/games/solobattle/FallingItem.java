package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.wbm.plugin.util.LocationTool;
import com.wbm.plugin.util.PlayerTool;
import com.wbm.plugin.util.instance.Counter;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;

/**
 * Pickup items falling from the sky<br>
 * - Enable PVP<br>
 * - Item spawns between pos1 and pos2<br>
 * - All items has the same chance to spawn<br>
 * - If pick item, player will get some scores<br>
 * - Each items has different scores<br>
 * - Items will be managed in List (removed when the game finished)<br>
 * - Disable food level change<br>
 * - Use spawn-item timer task<br>
 * 
 * # Custom Data<br>
 * - items<br>
 * - spawn-delay<br>
 * - spawn-pos1<br>
 * - spawn-pos2<br>
 * - finish-score<br>
 * - item-live-item<br>
 *
 */
public class FallingItem extends SoloBattleMiniGame {

	private Map<Material, Integer> items;
	private double spawnDelay;
	private Location spawnPos1, spawnPos2;
	private int finishScore;
	private int itemLiveTime;

	private List<Entity> spawnedEntities;

	public FallingItem() {
		super("FallingItem", 2, 10, 120, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14412);


		getSetting().setIcon(Material.FEATHER);

		getCustomOption().set(Option.PVP, true);
		getCustomOption().set(Option.FOOD_LEVEL_CHANGE, false);
		getCustomOption().set(Option.COLOR, ChatColor.BLUE);

		registerTasks();
	}

	private void registerTasks() {
		getTaskManager().registerTask("spawn-item", () -> {
			spawnRandomItem();
		});
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = getCustomData();

		// items
		Map<String, Integer> itemList = new HashMap<>();
		itemList.put(Material.TNT.name(), -3);
		itemList.put(Material.COAL.name(), 1);
		itemList.put(Material.IRON_INGOT.name(), 2);
		itemList.put(Material.GOLD_INGOT.name(), 3);
		itemList.put(Material.DIAMOND.name(), 4);
		data.put("items", itemList);

		// item-spawn-delay
		data.put("spawn-delay", 1.0);

		// spawn positions
		data.put("spawn-pos1", getLocation());
		data.put("spawn-pos2", getLocation());

		// finish score
		data.put("finish-score", 50);

		// item live time
		data.put("item-live-time", 10);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = getCustomData();

		// items: parse String to Material
		this.items = new HashMap<>();
		((Map<String, Integer>) data.get("items")).forEach((k, v) -> {
			this.items.put(Material.valueOf(k), v);
		});

		// spawn delay
		this.spawnDelay = (double) data.get("spawn-delay");

		// spawn positions
		this.spawnPos1 = (Location) data.get("spawn-pos1");
		this.spawnPos2 = (Location) data.get("spawn-pos2");

		// finish score
		this.finishScore = (int) data.get("finish-score");

		// item live time
		this.itemLiveTime = (int) data.get("item-live-time");
	}

	@Override
	protected void initGame() {
		this.spawnedEntities = new ArrayList<>();
	}

	@Override
	protected void onEvent(Event event) {
		if (event instanceof EntityPickupItemEvent) {
			pickupItem((EntityPickupItemEvent) event);
		} else if (event instanceof EntityDamageEvent) {
			onPlayerDamage((EntityDamageEvent) event);
		} else if (event instanceof PlayerDropItemEvent) {
			// prevent item drop
			((PlayerDropItemEvent) event).setCancelled(true);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		// start to fall random items between pos1 and pos2
		getTaskManager().runTaskTimer("spawn-item", 0, (long) (20 * this.spawnDelay));

		// nofity information
		// scores of items
		sendMessages("=====Score List=====");
		this.items.forEach((k, v) -> {
			sendMessages(k.name() + ": " + v);
		});

		// finish score
		sendMessages("\nFinish score: " + this.finishScore);

		// item live time
		sendMessages("\nItem will be disappeared in " + this.itemLiveTime + " seconds");
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		// remove remain items
		this.spawnedEntities.stream().filter(e -> e != null).forEach(e -> {
			e.remove();
		});
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Pick up items fallen from the sky!");

		return tutorial;
	}

	private void pickupItem(EntityPickupItemEvent e) {
		Player p = (Player) e.getEntity();

		Item item = e.getItem();

		// plus item score
		Material itemType = item.getItemStack().getType();
		Counter score = new Counter();
		this.items.forEach((k, v) -> {
			if (k == itemType) {
				score.setCount(v);
			}
		});
		if (score.getCount() > 0) {
			plusScore(p, score.getCount());
		} else {
			minusScore(p, -score.getCount());
		}

		// check finish
		if (checkGameFinish()) {
			finishGame();
		}
	}

	private boolean checkGameFinish() {
		return !getPlayerDataList().stream().filter(pData -> pData.getScore() >= this.finishScore).toList().isEmpty();
	}

	private void onPlayerDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}

		// no damage
		e.setDamage(0);
	}

	private void spawnRandomItem() {
		Location randomLoc = LocationTool.getRandomLocation(spawnPos1, spawnPos2);

		int randomIndex = (int) (Math.random() * this.items.size());
		Material randomItemType = new ArrayList<>(this.items.keySet()).get(randomIndex);
		Entity item = randomLoc.getWorld().dropItem(randomLoc, new ItemStack(randomItemType));

		// options
		item.setGlowing(true);

		// add spawnedEntities list
		this.spawnedEntities.add(item);

		// remove item after live time
		removeItemAfterLiveTime(item);

		// play sound
		getPlayers().forEach(p -> PlayerTool.playSound(p, Sound.ENTITY_CHICKEN_EGG));

	}

	private void removeItemAfterLiveTime(Entity item) {
		Bukkit.getScheduler().runTaskLater(AllMiniGamesMain.getInstance(), () -> {
			if (item != null) {
				item.remove();
				ParticleTool.spawn(item.getLocation(), Particle.EXPLOSION_NORMAL, 5, 0);
			}
		}, 20 * this.itemLiveTime);
	}

}
