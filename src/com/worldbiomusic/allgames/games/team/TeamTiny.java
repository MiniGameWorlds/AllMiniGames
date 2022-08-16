package com.worldbiomusic.allgames.games.team;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import com.minigameworld.frames.TeamMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.allgames.games.solo.Tiny;

/**
 * - Similar to {@link Tiny} <br>
 * - Can customize entity<br>
 * - Can customize shoot delay<br>
 * - Player can get score with only projectile <br>
 */
public class TeamTiny extends TeamMiniGame {
	private EntityType entityType;
	private double shootDelay;
	private Entity entity;

	public TeamTiny() {
		super("TeamTiny", 1, 10, 60, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14403);

		getSetting().setIcon(Material.STONE_BUTTON);

		getCustomOption().set(Option.COLOR, ChatColor.GRAY);
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = getCustomData();

		data.put("mob", EntityType.BAT.name());
		data.put("shootDelay", 1);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = getCustomData();

		this.entityType = EntityType.valueOf((String) data.get("mob"));
		this.shootDelay = (int) data.get("shootDelay");
	}

	@Override
	protected void onStart() {
		super.onStart();

		// summon entity
		summonEntity();

		// give tools to player
		getPlayers().forEach(p -> p.getInventory().addItem(new ItemStack(Material.SNOWBALL)));
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		if (this.entity != null) {
			this.entity.remove();
		}
	}

	@GameEvent(forced = true)
	protected void onHitEntity(ProjectileHitEvent e) {
		// check shooter
		ProjectileSource shooter = e.getEntity().getShooter();
		if (!(shooter instanceof Player && containsPlayer((Player) shooter))) {
			return;
		}

		Entity hitEntity = e.getHitEntity();
		if (this.entity.equals(hitEntity)) {
			// event detector can detect shooter from ProjectileHitEvent
			plusTeamScore(1);

			// sound
			SoundTool.play(getPlayers(), Sound.BLOCK_NOTE_BLOCK_CHIME);

			// particle
			spawnHitParticles(hitEntity);
		}
	}

	@GameEvent(forced = true)
	protected void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
		ProjectileSource shooterEntity = e.getEntity().getShooter();
		if (!(shooterEntity instanceof Player && containsPlayer((Player) shooterEntity))) {
			return;
		}

		// event detector can detect player from ProjectileLaunchEvent
		Player shooter = (Player) shooterEntity;

		// don't change amount of snowball
		shooter.getInventory().addItem(new ItemStack(Material.SNOWBALL));

		// give cooldown
		shooter.setCooldown(Material.SNOWBALL, (int) (20 * this.shootDelay));
	}

	@GameEvent(forced = true)
	protected void onEntityDamageEvent(EntityDamageEvent e) {
		if (e.getEntity().equals(this.entity)) {
			e.setDamage(0);
		}
	}

	@GameEvent(forced = true)
	protected void onEntityDeathEvent(EntityDeathEvent e) {
		if (e.getEntity().equals(this.entity)) {
			summonEntity();
		}
	}

	private void spawnHitParticles(Entity e) {
		ParticleTool.spawn(e.getLocation(), Particle.FLAME, 50, 0.1);
	}

	private void summonEntity() {
		this.entity = getLocation().getWorld().spawnEntity(getLocation(), this.entityType);
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Hit mob: " + ChatColor.GREEN + "+1");
	}

}
