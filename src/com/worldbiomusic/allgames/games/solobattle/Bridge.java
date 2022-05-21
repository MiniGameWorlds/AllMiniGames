package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;

public class Bridge extends SoloBattleMiniGame {

	enum SkillItem {
		JUMP(ItemStackTool.item(Material.RED_DYE, "JUMP")), SPEED(ItemStackTool.item(Material.ORANGE_DYE, "SPEED")),
		HIDE(ItemStackTool.item(Material.YELLOW_DYE, "HIDE"));

		private ItemStack item;

		SkillItem(ItemStack item) {
			this.item = item;
		}

		public ItemStack item() {
			return this.item;
		}

		public static List<ItemStack> items() {
			List<ItemStack> items = new ArrayList<>();
			Arrays.asList(values()).forEach(i -> items.add(i.item));
			return items;
		}

		public static boolean isSkillItem(ItemStack item) {
			for (SkillItem skillItem : values()) {
				if (skillItem.item().equals(item)) {
					return true;
				}
			}
			return false;
		}
	}

	private List<ItemStack> items;
	private int skillCooldown;

	public Bridge() {
		super("Bridge", 2, 10, 180, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14399);

		getSetting().setIcon(Material.REPEATER);

		getCustomOption().set(Option.PVP, true);

		registerTask();
	}

	private void registerTask() {
		this.getTaskManager().registerTask("check-fallen", () -> {
			// check only live players
			getLivePlayers().forEach(p -> {
				if (p.getLocation().getY() <= getLocation().subtract(0, 0.5, 0).getY()) {
					// msg
					sendTitle(p, "DIE", "");
					sendMessages(p.getName() + ChatColor.RED + " died");
					minusScore(p, getLivePlayersCount());

					// sound
					SoundTool.play(getPlayers(), Sound.BLOCK_NOTE_BLOCK_BELL);

					setLive(p, false);
				}
			});
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		// start fallen check task
		getTaskManager().runTaskTimer("check-fallen", 0, 5);

		// give items
		InventoryTool.addItemsToPlayers(getPlayers(), this.items);
		InventoryTool.addItemsToPlayers(getLivePlayers(), SkillItem.items());
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> customData = getCustomData();
		List<ItemStack> itemList = new ArrayList<>();
		itemList.add(new ItemStack(Material.FISHING_ROD));
		customData.put("items", itemList);

		customData.put("skill-cooldown", 10);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> customData = getCustomData();
		this.items = (List<ItemStack>) customData.get("items");
		this.skillCooldown = (int) customData.get("skill-cooldown");
	}

	@Override
	protected void initGame() {
	}

	@Override
	protected void onEvent(Event event) {
		if (event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			e.setDamage(0);
		} else if (event instanceof PlayerInteractEvent) {
			useSkill((PlayerInteractEvent) event);
		}
	}

	private void useSkill(PlayerInteractEvent e) {
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if (item == null) {
			return;
		}

		// check skill items
		if (!SkillItem.isSkillItem(item)) {
			return;
		}

		// check cooldown
		if (p.hasCooldown(item.getType())) {
			String remainSec = "" + ChatColor.RED + (p.getCooldown(item.getType()) / 20.0) + ChatColor.RESET;
			sendMessage(p, remainSec + " seconds remain to use");
			return;
		}

		// use skills
		if (item.equals(SkillItem.JUMP.item)) {
			jumpToSky(p);
		} else if (item.equals(SkillItem.SPEED.item)) {
			giveSpeedEffect(p);
		} else if (item.equals(SkillItem.HIDE.item)) {
			hidePlayer(p);
		}

		// set cooldown
		p.setCooldown(item.getType(), this.skillCooldown * 20);
	}

	private void jumpToSky(Player p) {
		p.setVelocity(new Vector(0, 5, 0));
		sendTitle(p, "JUMP", "");
	}

	private void giveSpeedEffect(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 0));
		sendTitle(p, "SPEED", "for 3 seconds");
	}

	private void hidePlayer(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 3, 0));
		sendTitle(p, "HIDE", "for 3 seconds");
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Don't fall down from the bridge");
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
