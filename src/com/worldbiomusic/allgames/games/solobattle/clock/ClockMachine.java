package com.worldbiomusic.allgames.games.solobattle.clock;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.wbm.plugin.util.ParticleTool;

public class ClockMachine {
	public static final int TICK_RATE = 1;

	public enum Direction {
		CLOCK_WISE, ANTI_CLOCK_WISE;

		public static Direction change(Direction dir) {
			return (dir == CLOCK_WISE) ? ANTI_CLOCK_WISE : CLOCK_WISE;
		}
	}

	private Clock clock;
	private Location center;
	private double handLength;
	private double speed;
	private double speedIncrement;
	private Direction direction;
	private boolean randomDirectionMode;
	private Particle particle;
	private int degree;

	public ClockMachine(Clock clock) {
		this.clock = clock;
	}

	public void init() {
		this.degree = 0;

		Map<String, Object> data = clock.getCustomData();
		this.center = (Location) data.get("center");
		this.handLength = (double) data.get("hand-length");
		this.speed = (double) data.get("hand-speed");
		this.speedIncrement = (double) data.get("hand-speed-increment");
		this.direction = ((boolean) data.get("clockwise")) ? Direction.CLOCK_WISE : Direction.ANTI_CLOCK_WISE;
		this.randomDirectionMode = (boolean) data.get("random-direction-mode");
		this.particle = Particle.valueOf((String) data.get("particle"));
	}

	public Location getFingertipLocation() {
		double x = Math.cos(Math.toRadians(this.degree)) * this.handLength;
		double z = Math.sin(Math.toRadians(this.degree)) * this.handLength;

		return this.center.clone().add(x, 0.1, z);
	}

	public Player getHitEntity() {
		Vector traceVector = getFingertipLocation().toVector().subtract(this.center.toVector());
		double distance = this.center.distance(getFingertipLocation());
		RayTraceResult result = this.center.getWorld().rayTraceEntities(this.center, traceVector, distance);

		if (result != null) {
			Entity hitEntity = result.getHitEntity();
			if (hitEntity instanceof Player && this.clock.containsPlayer((Player) hitEntity)) {
				return (Player) hitEntity;
			}
		}

		return null;
	}

	public void drawHand() {
		int distance = (int) getFingertipLocation().distance(this.center);
		ParticleTool.line(center, getFingertipLocation(), particle, distance * 4, 1);
	}

	public void updateWithEmptySpace() {
		int savedDegree = this.degree;

		plusDegree(degreeIncrementPerTick());
		int emptySpaceDegrees = degreeIncrementPerTick();

		for (int i = 0; i <= emptySpaceDegrees; i++) {
			Player hitPlayer = getHitEntity();
			if (hitPlayer != null) {
				this.clock.onPlayerCollide(hitPlayer);
			}

			drawHand();

			plusDegree(1);
		}

		this.degree = savedDegree;
	}

	/*
	 * Called 20 times in a second
	 */
	public void updateHand() {
		updateWithEmptySpace();

		this.speed += this.speedIncrement * (TICK_RATE / 20.0);
		plusDegree(degreeIncrementPerTick());

		if (this.randomDirectionMode) {
			if (Math.random() < 0.01) {
				changeDirection();
			}
		}
	}

	public void plusDegree(int amount) {
		if (this.direction == Direction.CLOCK_WISE) {
			this.degree = (this.degree + amount) % 360;
		} else {
			this.degree = (this.degree - amount) % 360;
		}
		if (this.degree < 0) {
			this.degree += 360;
		}
	}

	public void minusDegree(int amount) {
		plusDegree(-amount);
	}

	private int degreeIncrementPerTick() {
		return (int) (this.speed * 360 * (TICK_RATE / 20.0));
	}

	public void changeDirection() {
		this.direction = Direction.change(direction);
		ChatColor color = (this.direction == Direction.CLOCK_WISE) ? ChatColor.GREEN : ChatColor.RED;
		this.clock.sendTitleToAllPlayers(color + this.direction.name(), "");
	}

}
