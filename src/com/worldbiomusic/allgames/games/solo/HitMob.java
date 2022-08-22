package com.worldbiomusic.allgames.games.solo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.minigameworld.frames.SoloMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

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

		setting().setIcon(Material.WOODEN_SWORD);

		customOption().set(Option.COLOR, ChatColor.RED);

		// register as a listener
		Bukkit.getPluginManager().registerEvents(this, AllMiniGamesMain.getInstance());
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();

		List<ItemStack> playerTools = new ArrayList<ItemStack>();
		playerTools.add(new ItemStack(Material.WOODEN_SWORD));
		playerTools.add(new ItemStack(Material.WOODEN_AXE));
		playerTools.add(new ItemStack(Material.BOW));
		playerTools.add(new ItemStack(Material.ARROW, 10));

		data.put("tools", playerTools);
		data.put("mob", EntityType.SKELETON.name());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = customData();

		this.tools = (List<ItemStack>) data.get("tools");
		this.mobType = EntityType.valueOf((String) data.get("mob"));
	}

	@Override
	protected void onStart() {
		super.onStart();

		// give tools
		this.tools.forEach(getSoloPlayer().getInventory()::addItem);

		// spawn mob
		spawnMob();
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		// remove mob
		if (this.mob != null) {
			this.mob.remove();
		}
	}

	@GameEvent(forced = true)
	public void onEntityDamaged(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof Player) {
			onPlayerHurt(e);
		} else if (entity.equals(this.mob)) {
			onMobHurt(e);
		}
	}

	private void onPlayerHurt(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if (p.getHealth() <= e.getDamage()) {
			finishGame();
		}
	}

	private void onMobHurt(EntityDamageEvent e) {
		int damage = (int) e.getDamage();

		plusScore(damage);

		// title
		sendTitles("" + ChatColor.GREEN + damage, "damage");

		// cancel damage dealt to the mob
		e.setDamage(0);
		playSound(getSoloPlayer(), Sound.BLOCK_NOTE_BLOCK_COW_BELL);
	}

	@GameEvent(forced = true)
	protected void onEntityDeathEvent(EntityDeathEvent e) {
		if (e.getEntity().equals(this.mob)) {
			spawnMob();
		}
	}

	private void spawnMob() {
		this.mob = (Mob) location().getWorld().spawnEntity(location(), this.mobType);
		sendTitles(ChatColor.RED + mob.getName() + ChatColor.RESET + " has spawned!", "", 10, 20, 10);
		ParticleTool.spawn(this.mob.getLocation(), Particle.CAMPFIRE_COSY_SMOKE, 500, 0.01);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Hit mob: " + ChatColor.GREEN + "+" + ChatColor.RESET + " damage");
		tutorial.add("Die: game will be finished");
		return tutorial;
	}

}
