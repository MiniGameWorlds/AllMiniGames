package com.worldbiomusic.allgames.games.solo;

import java.util.List;

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

import com.minigameworld.frames.SoloMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

/**
 * - Summon bat when game start <br>
 * - Mob will be removed when game finished (onFinish()) <br>
 * - Hit bat with snowball to get score <br>
 * - Snowball has cooldown
 */
public class Tiny extends SoloMiniGame {
	private Entity bat;

	public Tiny() {
		super("Tiny", 60, 5);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14401);

		setting().setIcon(Material.OAK_BUTTON);

		customOption().set(Option.COLOR, ChatColor.YELLOW);
	}

	@Override
	protected void onStart() {
		super.onStart();

		summonBat();
		getSoloPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL));
	}

	private void summonBat() {
		this.bat = location().getWorld().spawnEntity(location(), EntityType.BAT);
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		this.bat.remove();
	}

	@GameEvent(forced = true)
	protected void onProjectileHitEvent(ProjectileHitEvent e) {
		// check shooter
		ProjectileSource shooter = e.getEntity().getShooter();
		if (!(shooter instanceof Player && containsPlayer((Player) shooter))) {
			return;
		}

		Entity hitEntity = e.getHitEntity();
		if (this.bat.equals(hitEntity)) {
			plusScore(1);

			SoundTool.play(hitEntity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL);
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
		shooter.setCooldown(Material.SNOWBALL, 20);
	}

	@GameEvent
	protected void onEntityDamageEvent(EntityDamageEvent e) {
		if (e.getEntity().equals(this.bat)) {
			e.setDamage(0);
		}
	}

	@GameEvent(forced = true)
	protected void onEntityDeathEvent(EntityDeathEvent e) {
		if (e.getEntity().equals(this.bat)) {
			summonBat();
		}
	}

	private void spawnHitParticles(Entity e) {
		ParticleTool.spawn(e.getLocation(), Particle.FLAME, 50, 0.1);
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Hit bat: " + ChatColor.GREEN + "+1");
	}

}
