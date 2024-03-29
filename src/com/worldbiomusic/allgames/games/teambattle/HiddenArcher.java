
package com.worldbiomusic.allgames.games.teambattle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.TeamBattleMiniGame;
import com.wbm.plugin.util.InventoryTool;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class HiddenArcher extends TeamBattleMiniGame {
	/*
	 * shoot hiding players with bow
	 */

	private int reloadCoolDown;

	public HiddenArcher() {
		super("HiddenArcher", 2, 20, 60 * 3, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14394);

		this.setting().setIcon(Material.BOW);
		this.setGroupChat(false);
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();
		customData().put("reload-cooldown", 3);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();
		this.reloadCoolDown = (int) customData().get("reload-cooldown");
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("After game starts, everyone will hide from others with snowballs");
		tutorial.add("Hit by other: die");
		return tutorial;
	}

	@GameEvent
	protected void onEntityDamageEvent(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@GameEvent
	protected void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		// check
		Entity damager = e.getDamager();
		if (!(damager instanceof Snowball && ((Snowball) damager).getShooter() instanceof Player)) {
			return;
		}

		Player victim = (Player) e.getEntity();
		Player shooter = (Player) ((Snowball) damager).getShooter();
		if (!isSameTeam(victim, shooter)) {
			shootPlayer(shooter, victim);
		}
	}

	@GameEvent(forced = true)
	protected void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
		Projectile proj = e.getEntity();
		ProjectileSource shooterEntity = proj.getShooter();
		if (!(shooterEntity instanceof Player && containsPlayer((Player) shooterEntity))) {
			return;
		}

		ProjectileSource shooter = shooterEntity;
		if (proj.getType() == EntityType.SNOWBALL) {
			Player p = (Player) shooter;

			p.getInventory().addItem(new ItemStack(Material.SNOWBALL));
			p.setCooldown(Material.SNOWBALL, 20 * this.reloadCoolDown);
		}

	}

	private void shootPlayer(Player shooter, Player victim) {
		// damager
		this.sendTitle(shooter, ChatColor.GREEN + "HIT", "");
		this.plusTeamScore(shooter, 1);

		// victim
		this.sendTitle(victim, ChatColor.RED + "DIE", "");

		// message to all
		sendMessages(
				ChatColor.GREEN + shooter.getName() + ChatColor.RESET + " hits " + ChatColor.RED + victim.getName());

		// sound
		SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_CHIME);

		// particle
		spawnHitParticles(victim);

		// set live
		setLive(victim, false);
	}

	private void spawnHitParticles(Entity e) {
		ParticleTool.spawn(e.getLocation(), Particle.FLAME, 50, 0.1);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// hide player from other teams
		for (Player p : this.players()) {
			p.addPotionEffect(
					new PotionEffect(PotionEffectType.INVISIBILITY, 20 * this.playTime(), 1, false, false));
		}

		// give tools
		InventoryTool.addItemToPlayers(players(), new ItemStack(Material.SNOWBALL));
		InventoryTool.addItemToPlayers(players(), new ItemStack(Material.GOLDEN_APPLE));

	}

}
