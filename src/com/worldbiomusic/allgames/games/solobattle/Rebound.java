package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.frames.helpers.MiniGamePlayer;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.ParticleTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class Rebound extends SoloBattleMiniGame {

	private double shootCooldown;
	private double shootPower;
	private boolean reverseMode;
	private boolean projectileGravity;

	private List<Entity> arrows;

	public Rebound() {
		super("Rebound", 2, 20, 180, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 15198);

		getSetting().setIcon(Material.DISPENSER);

		getCustomOption().set(Option.COLOR, ChatColor.AQUA);
		getCustomOption().set(Option.PLAYER_HURT, false);
		getCustomOption().set(Option.PVE, false);
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();
		Map<String, Object> data = getCustomData();
		data.put("shoot-cooldown", 1.0);
		data.put("shoot-power", 1.0);
		data.put("reverse-mode", false);
		data.put("projectile-gravity", true);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();
		Map<String, Object> data = getCustomData();
		this.shootCooldown = (double) data.get("shoot-cooldown");
		this.shootPower = (double) data.get("shoot-power");
		this.reverseMode = (boolean) data.get("reverse-mode");
		this.projectileGravity = (boolean) data.get("projectile-gravity");
	}

	@GameEvent
	protected void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@GameEvent
	protected void onEntityPickupItemEvent(EntityPickupItemEvent e) {
		e.setCancelled(true);
	}

	@GameEvent
	protected void onPlayerToggleFlightEvent(PlayerToggleFlightEvent e) {
		e.setCancelled(true);
	}

	@GameEvent
	protected void onPlayerHitByArrow(ProjectileHitEvent e) {
		if (!(e.getEntity().getShooter() instanceof Player)) {
			return;
		}

		Player shooter = (Player) e.getEntity().getShooter();
		Player victim = (Player) e.getHitEntity();

		// score
		plusScore(shooter, 1);

		// glow
		glowHighestScorePlayer();

		// msg
		sendTitles(ChatColor.GREEN + shooter.getName() + ChatColor.RESET + " -> " + ChatColor.RED + victim.getName()
				+ ChatColor.RESET, "");
		sendMessages(ChatColor.GREEN + shooter.getName() + ChatColor.RESET + " -> " + ChatColor.RED + victim.getName()
				+ ChatColor.RESET);

		// sound
		SoundTool.play(getPlayers(), Sound.BLOCK_NOTE_BLOCK_CHIME);

		// particle
		spawnHitParticles(victim);

		// hit rebound
		reboundByProjectileHit(victim, e.getEntity());

		// remove arrow
		if (e.getEntity() != null) {
			e.getEntity().remove();
		}
	}

	@GameEvent
	protected void onPlayerShootBow(EntityShootBowEvent e) {
		Player p = (Player) e.getEntity();

		Vector vector = p.getEyeLocation().getDirection();
		vector.normalize();

		// reverse
		if (!this.reverseMode) {
			vector.multiply(-1);
		}

		// bow shooting force
		vector.multiply(e.getForce());

		// power (custom data)
		vector.multiply(this.shootPower);

		// push shooter
		p.setVelocity(vector);

		// cooldown
		p.setCooldown(Material.BOW, (int) (this.shootCooldown * 20));

		// projectile gravity
		Entity proj = e.getProjectile();
		proj.setGravity(this.projectileGravity);

		this.arrows.add(proj);
	}

	private void glowHighestScorePlayer() {
		// glow off all players
		getPlayers().forEach(p -> p.setGlowing(false));

		// glow top player on
		Player topPlayer = getGamePlayers().stream().sorted(Comparator.comparing(MiniGamePlayer::getScore).reversed())
				.toList().get(0).getPlayer();
		topPlayer.setGlowing(true);
	}

	private void spawnHitParticles(Player p) {
		ParticleTool.spawn(p.getLocation(), Particle.FLAME, 50, 0.2);
	}

	private void reboundByProjectileHit(Player p, Entity proj) {
		Vector projVelocity = proj.getVelocity();
		p.setVelocity(p.getVelocity().add(projVelocity));
	}

	@Override
	protected void onStart() {
		super.onStart();

		getPlayers().forEach(p -> {
			// remove gravity
			p.setGravity(false);

			// allow flight
			p.setAllowFlight(true);

			// give bow & arrow
			ItemStack bow = new ItemStack(Material.BOW);
			ItemMeta meta = bow.getItemMeta();
			meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			bow.setItemMeta(meta);
			ItemStack arrow = new ItemStack(Material.ARROW);

			p.getInventory().addItem(bow);
			p.getInventory().addItem(arrow);
		});

		this.arrows = new ArrayList<>();
	}

	@Override
	protected void onFinish() {
		super.onFinish();

		// remove all arrows
		this.arrows.stream().filter(a -> a != null).forEach(Entity::remove);
	}

	@Override
	protected List<String> tutorial() {
		return List.of("Shoot others in zero gravity", "Arrow gives recoil(rebound) to shooter and hit player",
				"Player who has highest score will be glow");
	}

}
