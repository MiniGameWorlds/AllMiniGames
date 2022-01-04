package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.ItemStackTool;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

public class Bridge extends SoloBattleMiniGame {

	private List<ItemStack> items;

	public Bridge() {
		super("Bridge", 2, 4, 180, 20);

		getSetting().setIcon(Material.REPEATER);

		getCustomOption().set(Option.PVP, true);

		registerTask();
	}

	private void registerTask() {
		this.getTaskManager().registerTask("checkFallen", new Runnable() {
			@Override
			public void run() {
				getPlayers().forEach(p -> {
					if (p.getLocation().getY() <= getLocation().subtract(0, 0.5, 0).getY()) {
						sendTitle(p, "DIE", "");
						minusScore(p, 1);
						setLive(p, false);
					}
				});
			}
		});
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		// start fallen check task
		getTaskManager().runTaskTimer("checkFallen", 0, 5);

		// give items
		InventoryTool.addItemsToPlayers(getPlayers(), this.items);
		InventoryTool.addItemToPlayers(getPlayers(), ItemStackTool.item(Material.RED_DYE, "JUMP"));
		InventoryTool.addItemToPlayers(getPlayers(), ItemStackTool.item(Material.ORANGE_DYE, "SPEED"));
		InventoryTool.addItemToPlayers(getPlayers(), ItemStackTool.item(Material.YELLOW_DYE, "HIDE"));
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

		Map<String, Object> customData = getCustomData();
		List<ItemStack> itemList = new ArrayList<>();
		itemList.add(new ItemStack(Material.FISHING_ROD));

		customData.put("items", itemList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> customData = getCustomData();
		this.items = (List<ItemStack>) customData.get("items");
	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			e.setDamage(0);
		} else if (event instanceof PlayerInteractEvent) {
			PlayerInteractEvent e = (PlayerInteractEvent) event;
			if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				return;
			}

			Player p = e.getPlayer();
			ItemStack item = e.getItem();
			if (item == null) {
				return;
			}

			// use skills
			if (item.hasItemMeta()) {
				String displayName = item.getItemMeta().getDisplayName();
				switch (displayName) {
				case "JUMP":
					jumpToSky(p);
					break;
				case "SPEED":
					giveSpeedEffect(p);
					break;
				case "HIDE":
					hidePlayer(p);
					break;
				}
			}
		}
	}

	private void jumpToSky(Player p) {
		p.setVelocity(new Vector(0, 5, 0));
		sendTitle(p, "JUMP", "");

		// remove item
		p.getInventory().getItemInMainHand().setAmount(0);
	}

	private void giveSpeedEffect(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 0));
		sendTitle(p, "SPEED", "for 3 seconds");
		// remove item
		p.getInventory().getItemInMainHand().setAmount(0);
	}

	private void hidePlayer(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 3, 0));
		sendTitle(p, "HIDE", "for 3 seconds");
		// remove item
		p.getInventory().getItemInMainHand().setAmount(0);
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Don't fall down from bridge");
		tutorial.add("Use skills with RIGHT_CLICK items");
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
