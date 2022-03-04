package com.worldbiomusic.allgames.games.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import com.wbm.plugin.util.InventoryTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.TeamMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;

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

		// need when mob die by other reason (e.g. falling, killed by other mob)
		getSetting().addCustomDetectableEvent(EntityExplodeEvent.class);
		getSetting().addCustomDetectableEvent(EntityDeathEvent.class);

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
	protected void initGameSettings() {
		this.mobs = new ArrayList<>();
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof EntityDeathEvent) {
			EntityDeathEvent e = (EntityDeathEvent) event;

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

		} else if (event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();

				// if death
				if (p.getHealth() <= e.getDamage()) {
					e.setCancelled(true);
					// set live: false
					this.setLive(p, false);
				}
			}
		} else if (event instanceof EntityExplodeEvent) {
			// prevent block breaking by explosion of creeper
			EntityExplodeEvent e = (EntityExplodeEvent) event;

			if (!this.mobs.contains(e.getEntity())) {
				return;
			}

			// prevent breaking
			e.blockList().clear();

			// lay
			layTwoMobs(e.getEntity().getLocation());
		}
	}

	private void layTwoMobs(Location loc) {
		Location deathLoc = loc;

		// spawn random 2 mobs
		spawnRandomMob(deathLoc);
		spawnRandomMob(deathLoc);

		// plus score
		this.plusTeamScore(1);
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

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
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

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
	protected void runTaskAfterFinish() {
		super.runTaskAfterFinish();

		// remove mobs
		this.mobs.forEach(e -> e.remove());
	}

	@Override
	protected List<String> registerTutorial() {
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
