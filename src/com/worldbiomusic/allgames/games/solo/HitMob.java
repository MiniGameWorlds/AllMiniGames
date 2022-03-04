package com.worldbiomusic.allgames.games.solo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;

/**
 * - Player will get score equal to damage dealt to mob<br>
 * - Player can get score with direct weapon or projectile<br>
 * - If a player dead, game will be finished<br>
 * - Custom-data: player tools(List), mob <br>
 * - Mob never dies<br>
 * - Mob will spawn in spawn location when the game starts<br>
 * - Mob will die when the game finished<br>
 * - If mob died in any situation, will respawn in spawn location<br>
 */
public class HitMob extends SoloMiniGame implements Listener {

	private List<ItemStack> tools;
	private EntityType mobType;
	private Mob mob;

	public HitMob() {
		super("HitMob", 60, 5);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14402);


		getSetting().setIcon(Material.WOODEN_SWORD);

		getCustomOption().set(Option.COLOR, ChatColor.RED);

		// register as a listener
		Bukkit.getPluginManager().registerEvents(this, AllMiniGamesMain.getInstance());
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

		Map<String, Object> data = getCustomData();

		List<ItemStack> playerTools = new ArrayList<ItemStack>();
		playerTools.add(new ItemStack(Material.WOODEN_SWORD));
		playerTools.add(new ItemStack(Material.BOW));
		playerTools.add(new ItemStack(Material.ARROW, 10));

		data.put("tools", playerTools);
		data.put("mob", EntityType.SKELETON.name());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = getCustomData();

		this.tools = (List<ItemStack>) data.get("tools");
		this.mobType = EntityType.valueOf((String) data.get("mob"));
	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		// give tools
		this.tools.forEach(getSoloPlayer().getInventory()::addItem);

		// spawn mob
		spawnMob();

	}

	@Override
	protected void runTaskAfterFinish() {
		super.runTaskAfterFinish();

		// remove mob
		if (this.mob != null) {
			this.mob.remove();
		}
	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof EntityDamageEvent) {
			EntityDamageEvent damageEvent = (EntityDamageEvent) event;
			Entity entity = damageEvent.getEntity();

			// if player dead
			if (entity instanceof Player) {
				Player p = (Player) entity;

				if (p.getHealth() <= damageEvent.getDamage()) {
					finishGame();
				}
			}

			if (entity.equals(this.mob)) {
				if (event instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
					int damage = (int) e.getDamage();

					// event detector can detect EntityDamageByEntityEvent if damager is a player or
					// a shooter of projectile
					plusScore(damage);
				}

				// cancel damage dealt to the mob
				damageEvent.setDamage(0);
			}
		} else if (event instanceof EntityDeathEvent) {
			EntityDeathEvent e = (EntityDeathEvent) event;
			if (this.mob.equals(e.getEntity())) {
				spawnMob();
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		passEvent(e);
	}

	private void spawnMob() {
		this.mob = (Mob) getLocation().getWorld().spawnEntity(getLocation(), this.mobType);
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Hit mob: " + ChatColor.GREEN + "+1");
		tutorial.add("Die: finish");

		return tutorial;
	}

}
