package com.worldbiomusic.allgames.games.team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.allgames.games.solo.Tiny;
import com.worldbiomusic.minigameworld.minigameframes.TeamMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.worldbiomusic.minigameworld.util.Metrics;

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
	protected void registerCustomData() {
		super.registerCustomData();

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
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		// summon entity
		summonEntity();

		// give tools to player
		getPlayers().forEach(p -> p.getInventory().addItem(new ItemStack(Material.SNOWBALL)));
	}

	@Override
	protected void runTaskAfterFinish() {
		super.runTaskAfterFinish();

		if (this.entity != null) {
			this.entity.remove();
		}
	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof ProjectileHitEvent) {
			ProjectileHitEvent e = (ProjectileHitEvent) event;
			Entity hitEntity = e.getHitEntity();

			if (hitEntity != null && hitEntity.equals(this.entity)) {
				// event detector can detect shooter from ProjectileHitEvent
				plusTeamScore(1);
			}

		} else if (event instanceof ProjectileLaunchEvent) {
			ProjectileLaunchEvent e = (ProjectileLaunchEvent) event;
			Projectile proj = e.getEntity();
			ProjectileSource shooterEntity = proj.getShooter();

			// event detector can detect player from ProjectileLaunchEvent
			Player shooter = (Player) shooterEntity;

			// don't change amount of snowball
			shooter.getInventory().addItem(new ItemStack(Material.SNOWBALL));

			// give cooldown
			shooter.setCooldown(Material.SNOWBALL, (int) (20 * this.shootDelay));
		} else if (event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;

			if (e.getEntity().equals(this.entity)) {
				e.setDamage(0);
			}
		} else if (event instanceof EntityDeathEvent) {
			EntityDeathEvent e = (EntityDeathEvent) event;
			if (e.getEntity().equals(this.entity)) {
				summonEntity();
			}
		}
	}

	private void summonEntity() {
		this.entity = getLocation().getWorld().spawnEntity(getLocation(), this.entityType);
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Hit mob: " + ChatColor.GREEN + "+1");
		return tutorial;
	}

}
