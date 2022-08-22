package com.worldbiomusic.allgames.games.teambattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.TeamBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.util.Utils;
import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.PlayerTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

import net.md_5.bungee.api.ChatColor;

public class PassMob extends TeamBattleMiniGame {
	public class Area {
		String name;
		Team team;
		Location loc;
		List<Entity> mobs;

		public Area(String name) {
			this.name = name;
			this.mobs = new ArrayList<>();
		}

		public void init() {
			// kill all mobs
			Utils.debug(this.name + " area killed: " + this.mobs.size());
			this.mobs.forEach(m -> m.remove());
			this.mobs.clear();
		}

		public void setTeam(Team team) {
			this.team = team;
		}

		public void spawnRandomMob() {
			int r = (int) (Math.random() * getMobList().size());
			this.spawnMob(getMobList().get(r));
		}

		public void spawnMob(EntityType entityType) {
			Entity entity = this.loc.getWorld().spawnEntity(this.loc, entityType);
			this.mobs.add(entity);

			// set team random targeting
			((Mob) entity).setTarget(this.team.getRandomMember());
		}

		public void passMobToOtherArea(Entity mob, Area area) {
			this.mobs.remove(mob);
			area.spawnMob(mob.getType());

			// title
			this.team.getMembers().forEach(m -> m.sendTitle(ChatColor.GREEN + "Mob passed", "", 4, 12, 4));

			// sound
			SoundTool.play(area.team.getMembers(), Sound.BLOCK_NOTE_BLOCK_BELL);
		}

		public List<Entity> getMobs() {
			return this.mobs;
		}

		public void tpTeam() {
			this.team.getMembers().forEach(m -> m.teleport(this.loc));
		}

	}

	private Area redArea, blueArea;
	private int mobSpawnDelay;

	public PassMob() {
		super("PassMob", 2, 10, 60 * 2, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14397);

		// settings
		setting().setIcon(Material.OAK_FENCE);

		// options
		setGroupChat(true);
		customOption().set(Option.MINIGAME_RESPAWN, false);
		setTeamSize(maxPlayers() / 2);
		setTeamRegisterMode(TeamRegisterMode.NONE);

		// areas
		this.redArea = new Area("red");
		this.blueArea = new Area("blue");

		// task
		this.registerTask();
	}

	private void registerTask() {
		this.taskManager().registerTask("spawnMob", () -> {
			// spawn random mob
			redArea.spawnRandomMob();
			blueArea.spawnRandomMob();
		});
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();
		this.customData().put("red-location", new Location(this.location().getWorld(), 0, 0, 0));
		this.customData().put("blue-location", new Location(this.location().getWorld(), 0, 0, 0));

		this.customData().put("mob-spawn-delay", 15);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		// create areas
		this.redArea.loc = (Location) this.customData().get("red-location");
		this.blueArea.loc = (Location) this.customData().get("blue-location");

		// mob spawn delay
		this.mobSpawnDelay = (int) this.customData().get("mob-spawn-delay");
	}

	@GameEvent(forced = true)
	protected void onEntityDeathEvent(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if (isPassMobEntity(entity)) {
			Area area = this.getMobArea(entity);
			area.passMobToOtherArea(entity, this.otherArea(area));

			// remove drops
			e.getDrops().clear();
		}
	}

	@GameEvent
	protected void onEntityDamageEvent(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();

		// if death
		if (p.getHealth() <= e.getDamage()) {
			// cancel damage
			e.setDamage(0);

			// msg
			sendTitle(p, ChatColor.RED + "DIE", "");

			// heal player
			PlayerTool.heal(p);

			Team team = this.getTeam(p);
			Area area = this.getTeamArea(team);
			p.teleport(area.loc);
		}
	}

	private Area otherArea(Area area) {
		if (area.equals(this.redArea)) {
			return this.blueArea;
		} else {
			return this.redArea;
		}
	}

	@Override
	protected void initGame() {
		super.initGame();

		// set area with team
		this.redArea.setTeam(this.getTeams().get(0));
		this.blueArea.setTeam(this.getTeams().get(1));
	}

	@Override
	protected void onStart() {
		super.onStart();

		// give kits
		for (Player p : this.players()) {
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.IRON_SWORD));
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.COOKED_PORKCHOP, 64));
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.BOW));
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.ARROW, 64));
			InventoryTool.addItemToPlayer(p, new ItemStack(Material.GOLDEN_APPLE, 3));

			p.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
			p.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			p.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
			p.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
		}

		// spawn random mob
		this.redArea.spawnRandomMob();
		this.blueArea.spawnRandomMob();

		// tp team members
		this.redArea.tpTeam();
		this.blueArea.tpTeam();

		// start random mob spawn task
		this.taskManager().runTaskTimer("spawnMob", this.mobSpawnDelay * 20, this.mobSpawnDelay * 20);
	}

	public void processTeamScore() {
		int redMobCount = this.redArea.getMobs().size();
		int blueMobCount = this.blueArea.getMobs().size();

		// small is winner
		this.redArea.team.plusTeamScore(blueMobCount);
		this.blueArea.team.plusTeamScore(redMobCount);
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		// process scores
		this.processTeamScore();

		// init areas
		this.redArea.init();
		this.blueArea.init();
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("Kill monsters to pass them to the other's area");
		tutorial.add("The team with " + ChatColor.GREEN + "fewer" + ChatColor.RESET + " monster is the winner");
		return tutorial;
	}

	public boolean isPassMobEntity(Entity entity) {
		return this.redArea.getMobs().contains(entity) || this.blueArea.getMobs().contains(entity);
	}

	public Area getMobArea(Entity entity) {
		if (this.redArea.getMobs().contains(entity)) {
			return this.redArea;
		} else if (this.blueArea.getMobs().contains(entity)) {
			return this.blueArea;
		}
		return null;
	}

	public Area getTeamArea(Team team) {
		if (this.redArea.team.equals(team)) {
			return this.redArea;
		} else {
			return this.blueArea;
		}
	}

	public List<EntityType> getMobList() {
		List<EntityType> mobs = new ArrayList<>();
		mobs.add(EntityType.ZOMBIE);
		mobs.add(EntityType.SKELETON);
		mobs.add(EntityType.SPIDER);

		return mobs;
	}
}
