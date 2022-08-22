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

import com.minigameworld.frames.TeamMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.managers.event.GameEvent.State;
import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.Msgs;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BreedMob extends TeamMiniGame {

	private int maxHealth;

	private List<Mob> mobs;
	private List<EntityType> mobList;

	public BreedMob() {
		super("BreedMob", 1, 10, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14395);

		this.mobList = new ArrayList<>();

		// settings
		setting().setIcon(Material.ZOMBIE_SPAWN_EGG);
		customOption().set(Option.COLOR, ChatColor.RED);
		customOption().set(Option.SCORE_NOTIFYING, false);

		// register task
		registerMobRetargetingTask();
	}

	void registerMobRetargetingTask() {
		taskManager().registerTask("retarget", () -> {
			mobs.forEach(m -> {
				// if has no target, retarget a random player
				if (m.getTarget() == null || !players().contains(m.getTarget())) {
					m.setTarget(randomPlayer());
				}
			});
		});

		taskManager().registerTask("update-actionbar", () -> {
			updateActionBar();
		});
	}

	@Override
	protected void initGame() {
		this.mobs = new ArrayList<>();
	}

	@GameEvent(forced = true, state = State.WAIT)
	protected void onMobDamaged(EntityDamageEvent e) {
		if (this.mobs.contains(e.getEntity())) {
			e.setCancelled(true);
		}
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
			e.setDroppedExp(0);
		}
	}

	@GameEvent
	protected void onPlayerHurt(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();

		// if death
		if (p.getHealth() <= e.getDamage()) {
			e.setDamage(0);

			// title
			sendTitle(p, ChatColor.RED + "DIE", "");

			// set live: false
			setLive(p, false);
		}
	}

	@GameEvent(forced = true)
	protected void onEntityExplode(EntityExplodeEvent e) {
		// prevent block breaking by explosion of creeper
		if (!this.mobs.contains(e.getEntity())) {
			return;
		}

		// prevent breaking
		e.blockList().clear();

		// lay
		layTwoMobs(e.getEntity().getLocation());

		// sound
		SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_CHIME);
	}

	private void layTwoMobs(Location deathLoc) {
		// spawn random 2 mobs
		spawnRandomMob(deathLoc);
		spawnRandomMob(deathLoc);

		// plus score
		plusTeamScore(1);

		sendTitles(ChatColor.GREEN + "+" + ChatColor.RESET + " 1", "");

		// sound
		playSounds(Sound.BLOCK_NOTE_BLOCK_BELL);
	}

	private List<Mob> liveMobs() {
		return this.mobs.stream().filter(m -> m != null && !m.isDead()).toList();
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = this.customData();

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

		Map<String, Object> data = this.customData();

		// mob list
		@SuppressWarnings("unchecked")
		List<String> mobsList = (List<String>) data.get("mobs");
		mobsList.forEach(m -> {
			mobList.add(EntityType.valueOf(m));
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		// spawn random mob
		spawnRandomMob(location());

		// give kits to players
		List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(Material.IRON_SWORD));
		items.add(new ItemStack(Material.BOW));
		items.add(new ItemStack(Material.ARROW, 64));
		items.add(new ItemStack(Material.COOKED_BEEF, 10));
		items.add(new ItemStack(Material.GOLDEN_APPLE, 3));

		InventoryTool.addItemsToPlayers(players(), items);

		// set armors
		for (Player p : this.players()) {
			p.setHealthScale(40);
			p.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			p.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			p.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			p.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
		}

		// start task
		taskManager().runTaskTimer("retarget", 0, 20);
		taskManager().runTaskTimer("update-actionbar", 0, 10);
	}

	private void updateActionBar() {
		String msg = ChatColor.RED + "Mobs: " + ChatColor.WHITE + ChatColor.BOLD + liveMobs().size();
		msg += ", " + ChatColor.GREEN + "Live players: " + ChatColor.WHITE + ChatColor.BOLD + livePlayersCount();
		Msgs.msg(players(), ChatMessageType.ACTION_BAR, new TextComponent(msg));
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		// remove mobs
		liveMobs().forEach(e -> e.remove());
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
