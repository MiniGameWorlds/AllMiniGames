package com.worldbiomusic.allgames.games.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.TeamMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class BreedMob extends TeamMiniGame {

	private List<Mob> mobs;
	private List<EntityType> mobList;

	public BreedMob() {
		super("BreedMob", 1, 10, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14395);

		this.mobList = new ArrayList<>();

		// settings
		this.getSetting().setIcon(Material.ZOMBIE_SPAWN_EGG);
		this.getCustomOption().set(Option.COLOR, ChatColor.RED);

		// register task
		registerMobRetargetingTask();
	}

	void registerMobRetargetingTask() {
		this.getTaskManager().registerTask("retarget", new Runnable() {

			@Override
			public void run() {
				BreedMob.this.mobs.forEach(m -> {
					// if has no target, retarget a random player
					if (m.getTarget() == null || !getPlayers().contains(m.getTarget())) {
						m.setTarget(randomPlayer());
					}
				});
			}
		});
	}

	@Override
	protected void initGame() {
		this.mobs = new ArrayList<>();
	}

	@GameEvent(forced = true)
	protected void onEntityDeathEvent(EntityDeathEvent e) {
		if (!(e.getEntity() instanceof Mob)) {
			return;
		}

		Mob entity = (Mob) e.getEntity();

		// check mob
		if (this.mobs.contains(entity)) {
			layTwoMobs(entity.getLocation());

			// clear drops
			e.getDrops().clear();
		}
	}

	@GameEvent
	protected void onEntityDamageEvent(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();

		// if death
		if (p.getHealth() <= e.getDamage()) {
			e.setCancelled(true);

			// title
			sendTitle(p, ChatColor.RED + "DIE", "");

			// set live: false
			this.setLive(p, false);
		}
	}

	@GameEvent
	protected void onEntityExplodeEvent(EntityExplodeEvent e) {
		// prevent block breaking by explosion of creeper
		if (!this.mobs.contains(e.getEntity())) {
			return;
		}

		// prevent breaking
		e.blockList().clear();

		// lay
		layTwoMobs(e.getEntity().getLocation());

		// sound
		SoundTool.play(getPlayers(), Sound.BLOCK_NOTE_BLOCK_CHIME);
	}

	private void layTwoMobs(Location deathLoc) {
		// spawn random 2 mobs
		spawnRandomMob(deathLoc);
		spawnRandomMob(deathLoc);

		// plus score
		this.plusTeamScore(1);
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = this.getCustomData();

		// mob list
		List<String> randomMobList = new ArrayList<>();
		randomMobList.add(EntityType.ZOMBIE.name());
		randomMobList.add(EntityType.SKELETON.name());
		randomMobList.add(EntityType.SPIDER.name());

		data.put("mobs", randomMobList);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = this.getCustomData();

		// mob list
		@SuppressWarnings("unchecked")
		List<String> mobsList = (List<String>) data.get("mobs");
		mobsList.forEach(m -> {
			this.mobList.add(EntityType.valueOf(m));
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		// spawn random mob
		spawnRandomMob(getLocation());

		// start task
		this.getTaskManager().runTaskTimer("retarget", 0, 20 * 5);

		// give kits to players
		List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(Material.IRON_SWORD));
		items.add(new ItemStack(Material.BOW));
		items.add(new ItemStack(Material.ARROW, 64));
		items.add(new ItemStack(Material.COOKED_BEEF, 10));
		items.add(new ItemStack(Material.GOLDEN_APPLE, 3));

		InventoryTool.addItemsToPlayers(getPlayers(), items);

		// set armors
		for (Player p : this.getPlayers()) {
			p.setHealthScale(40);
			p.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			p.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			p.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			p.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
		}
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		// remove mobs
		this.mobs.forEach(e -> e.remove());
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Kill mob: +1");
		tutorial.add("Death: spectator");
		tutorial.add("Mobs will lay two mobs when die");
		return tutorial;
	}

	public Mob spawnRandomMob(Location loc) {
		List<EntityType> randomMobList = this.mobList;
		Collections.shuffle(randomMobList);
		Mob randomMob = (Mob) loc.getWorld().spawnEntity(loc, randomMobList.get(0));

		// add to list
		this.mobs.add(randomMob);

		// set target with a random player
		randomMob.setTarget(this.randomPlayer());

		return randomMob;
	}

}
