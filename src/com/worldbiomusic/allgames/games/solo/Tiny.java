package com.worldbiomusic.allgames.games.solo;

import java.util.ArrayList;
import java.util.List;

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
import com.worldbiomusic.minigameworld.minigameframes.SoloMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;

/**
 * - Summon bat when game start <br>
 * - Mob will be removed when game finished (runTaskAfterFinish()) <br>
 * - Hit bat with snowball to get score <br>
 * - Snowball has cooldown
 */
public class Tiny extends SoloMiniGame {

	private Entity bat;

	public Tiny() {
		super("Tiny", 60, 5);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14401);


		getSetting().setIcon(Material.OAK_BUTTON);

		getCustomOption().set(Option.COLOR, ChatColor.YELLOW);
	}

	@Override
	protected void initGameSettings() {

	}

	@Override
	protected void runTaskAfterStart() {
		super.runTaskAfterStart();

		summonBat();
		getSoloPlayer().getInventory().addItem(new ItemStack(Material.SNOWBALL));
	}

	private void summonBat() {
		this.bat = getLocation().getWorld().spawnEntity(getLocation(), EntityType.BAT);
	}

	@Override
	protected void runTaskAfterFinish() {
		super.runTaskAfterFinish();

		this.bat.remove();
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof ProjectileHitEvent) {
			ProjectileHitEvent e = (ProjectileHitEvent) event;
			Entity hitEntity = e.getHitEntity();

			if (hitEntity != null && hitEntity.equals(this.bat)) {
				// event detector can detect shooter from ProjectileHitEvent
				plusScore(1);
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
			shooter.setCooldown(Material.SNOWBALL, 20);
		} else if (event instanceof EntityDamageEvent) {
			EntityDamageEvent e = (EntityDamageEvent) event;

			if (e.getEntity().equals(this.bat)) {
				e.setDamage(0);
			}
		} else if (event instanceof EntityDeathEvent) {
			EntityDeathEvent e = (EntityDeathEvent) event;
			if (e.getEntity().equals(this.bat)) {
				summonBat();
			}
		}
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Hit bat: " + ChatColor.GREEN + "+1");
		return tutorial;
	}

}
