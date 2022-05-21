
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
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.wbm.plugin.util.InventoryTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.TeamBattleMiniGame;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;

public class HiddenArcher extends TeamBattleMiniGame {
	/*
	 * shoot hiding players with bow
	 */

	private int reloadCoolDown;

	public HiddenArcher() {
		super("HiddenArcher", 2, 20, 60 * 3, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14394);

		this.getSetting().setIcon(Material.BOW);
		this.setGroupChat(false);
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();
		getCustomData().put("reload-cooldown", 3);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();
		this.reloadCoolDown = (int) getCustomData().get("reload-cooldown");
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<>();
		tutorial.add("After game starts, everyone will hide from others with snowballs");
		tutorial.add("Hit by other: die");
		return tutorial;
	}

	@Override
	protected void onEvent(Event event) {
		super.onEvent(event);
		if (event instanceof EntityDamageEvent) {
			EntityDamageEvent damageEvent = (EntityDamageEvent) event;
			damageEvent.setCancelled(true);

			if (damageEvent instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) damageEvent;

				// check
				Entity damager = e.getDamager();
				Entity victimEntity = e.getEntity();
				if (!(damager instanceof Snowball && victimEntity instanceof Player)) {
					return;
				}

				Player victim = (Player) victimEntity;
				Snowball snowball = (Snowball) damager;

				if (snowball.getShooter() instanceof Player) {
					Player shooter = ((Player) snowball.getShooter());
					if (!this.isSameTeam(victim, shooter)) {
						damageEvent.setCancelled(false);

						// remove damage
						e.setDamage(0);

						this.shootPlayer(shooter, victim);
					}
				}
			}
		} else if (event instanceof ProjectileLaunchEvent) {
			ProjectileLaunchEvent e = (ProjectileLaunchEvent) event;
			Projectile proj = e.getEntity();
			ProjectileSource shooter = proj.getShooter();

			if (proj.getType() == EntityType.SNOWBALL) {
				Player p = (Player) shooter;

				p.getInventory().addItem(new ItemStack(Material.SNOWBALL));
				p.setCooldown(Material.SNOWBALL, 20 * this.reloadCoolDown);
			}

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
		SoundTool.play(getPlayers(), Sound.BLOCK_NOTE_BLOCK_CHIME);

		// particle
		spawnHitParticles(victim);

		// set live
		this.setLive(victim, false);
	}

	private void spawnHitParticles(Entity e) {
		ParticleTool.spawn(e.getLocation(), Particle.FLAME, 50, 0.1);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// hide player from other teams
		for (Player p : this.getPlayers()) {
			p.addPotionEffect(
					new PotionEffect(PotionEffectType.INVISIBILITY, 20 * this.getPlayTime(), 1, false, false));
		}

		// give tools
		InventoryTool.addItemToPlayers(getPlayers(), new ItemStack(Material.SNOWBALL));
		InventoryTool.addItemToPlayers(getPlayers(), new ItemStack(Material.GOLDEN_APPLE));

	}

}
