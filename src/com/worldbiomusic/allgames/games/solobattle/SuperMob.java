package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class SuperMob extends SoloBattleMiniGame {

	private Zombie superMob;
	private List<Entity> entities;
	double skillChance;

	public SuperMob() {
		super("SuperMob", 2, 10, 60 * 2, 30);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14396);

		this.entities = new ArrayList<>();

		// settings
		getSetting().setIcon(Material.ZOMBIE_HEAD);

		// options
		getCustomOption().set(Option.INVENTORY_SAVE, true);
		getCustomOption().set(Option.PVP, false);
		getCustomOption().set(Option.COLOR, ChatColor.RED);

		registerTask();
	}

	private void registerTask() {
		// random targeting task
		getTaskManager().registerTask("changeTarget", new Runnable() {

			@Override
			public void run() {
				int r = (int) (Math.random() * getPlayerCount());
				superMob.setTarget(getPlayers().get(r));
			}
		});
	}

	@Override
	protected void initGame() {
		this.killAllEntities();
		this.skillChance = 0.1;
	}

	private void killAllEntities() {
		this.entities.forEach(e -> e.remove());
		this.entities.clear();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
		super.onStart();

		// give kits
		for (Player p : this.getPlayers()) {
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.IRON_SWORD));
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.COOKED_PORKCHOP, 20));
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.BOW));
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.ARROW, 64));
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.GOLDEN_APPLE));

			p.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			p.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			p.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			p.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
		}

		// spawn super mob
		this.superMob = (Zombie) this.getLocation().getWorld().spawnEntity(this.getLocation(), EntityType.ZOMBIE);
		this.superMob.setMaxHealth(100_000_000);
		this.superMob.setHealth(this.superMob.getMaxHealth());
		this.superMob.getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
		this.superMob.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		this.superMob.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		this.superMob.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		this.superMob.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
		this.superMob.setBaby(true);

		// glow
		this.superMob.setGlowing(true);

		// add to list
		this.entities.add(this.superMob);

		// run random targeting task
		this.getTaskManager().runTaskTimer("changeTarget", 0, 20 * 60);
	}

	@Override
	protected void onFinish() {
		super.onFinish();
		this.killAllEntities();
	}

	@GameEvent
	protected void onMobDeath(EntityDeathEvent e) {
		if (this.entities.contains(e.getEntity())) {
			Entity killer = e.getEntity().getKiller();
			if (killer == null) {
				return;
			}

			Player killerPlayer = null;
			if (!(killer instanceof Player)) {
				return;
			}

			killerPlayer = (Player) killer;
			this.plusScore(killerPlayer, 5);
			e.getDrops().clear();
		}
	}

	@GameEvent
	protected void onPlayerDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();

		// if death
		if (p.getHealth() <= e.getDamage()) {
			// cancel damage
			e.setDamage(0);

			sendTitle(p, ChatColor.GREEN + "Respawn", "");
			SoundTool.play(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL);
			setLive(p, false);
		}
	}

	@GameEvent
	protected void onSuperMobDamaged(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Zombie)) {
			return;
		}

		Zombie zombie = (Zombie) e.getEntity();
		if (!this.superMob.equals(zombie)) {
			return;
		}

		// direct damage
		if (e.getDamager() instanceof Player) {
			this.whenSuperMobDamagedByPlayer(e, (Player) e.getDamager());
		}

		// projectile damage
		else if (e.getDamager() instanceof Arrow) {
			Arrow proj = (Arrow) e.getDamager();
			if (!(proj.getShooter() instanceof Player)) {
				return;
			}

			Player shooter = (Player) proj.getShooter();
			if (!this.containsPlayer(shooter)) {
				return;
			}

			this.whenSuperMobDamagedByPlayer(e, shooter);
		}
	}

	private void whenSuperMobDamagedByPlayer(EntityDamageByEntityEvent e, Player p) {
		int damage = (int) e.getDamage();
		if (damage > 0) {
			// plus score
			this.plusScore(p, damage);
		}

		// use skill
		if (Math.random() < this.skillChance) {
			this.useSkill();
			this.skillChance += 0.01;
		}

		// set target player
		this.superMob.setTarget(p);
	}

	public enum Mode {
		ATTACK, DEFENSE, FAST;

		public static Mode random() {
			switch ((int) (Math.random() * 3)) {
			case 0:
				return ATTACK;
			case 1:
				return DEFENSE;
			default:
				return FAST;
			}
		}
	}

	private void useSkill() {
		int r = (int) (Math.random() * 2);
		switch (r) {
		case 0:
			this.spawnFriends();
			break;
		case 1:
			this.useMode(Mode.random());
			break;
		}

		// etc
		SoundTool.play(getPlayers(), Sound.BLOCK_BELL_USE);
		ParticleTool.spawn(this.superMob.getLocation(), Particle.FLAME, 50, 0.2);
	}

	private void spawnFriends() {
		int amount = this.getPlayerCount();
		Location loc = this.superMob.getLocation();
		for (int i = 0; i < amount; i++) {
			this.spawnMob(loc, EntityType.ZOMBIE);
			this.spawnMob(loc, EntityType.SKELETON);
			this.spawnMob(loc, EntityType.SPIDER);
		}

		sendMessages(ChatColor.RED + "SuperMob invites friends");
	}

	private void spawnMob(Location loc, EntityType type) {
		// add to entities List
		this.entities.add(loc.getWorld().spawnEntity(loc, type));
	}

	private void useMode(Mode mode) {
		switch (mode) {
		case ATTACK:
			this.useAttackMode();
			break;
		case DEFENSE:
			this.useDefenseMode();
			break;
		case FAST:
			this.useFastMode();
			break;
		}
	}

	private void useAttackMode() {
		this.superMob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 0));
		this.sendMessages(ChatColor.RED + "SuperMob uses attack mode");
	}

	private void useDefenseMode() {
		this.superMob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 0));
		this.sendMessages(ChatColor.RED + "SuperMob uses defense mode");
	}

	private void useFastMode() {
		this.superMob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0));
		this.sendMessages(ChatColor.RED + "SuperMob uses fast mode");
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Hit Super Mob: +(damage)");
		tutorial.add("Super mob has some skills: ATTACK, DEFENSE, FAST, FRIENDSHIP");
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
//
//
//
//
