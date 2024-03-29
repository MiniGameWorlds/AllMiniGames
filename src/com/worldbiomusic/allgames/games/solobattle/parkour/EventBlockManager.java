package com.worldbiomusic.allgames.games.solobattle.parkour;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.wbm.plugin.util.PlayerTool;
import com.wbm.plugin.util.PotionEffectTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

public class EventBlockManager {
	public static final String FINISH = "finish";
	public static final String RESPAWN = "respawn";
	public static final String UP_TELEPORT = "up-teleport";
	public static final String DOWN_TELEPORT = "down-teleport";
	public static final String FLICKER = "flicker";
	public static final String HEAL = "heal";
	public static final String JUMP = "jump";
	public static final String DEBUFF = "debuff";

	private Parkour parkour;
	private Map<String, Material> eventBlocks;
	private boolean endless;

	public EventBlockManager(Parkour parkour) {
		this.parkour = parkour;
	}

	@SuppressWarnings("unchecked")
	public void setCustomData(Map<String, Object> customData) {
		this.endless = (boolean) customData.get("endless");

		this.eventBlocks = new HashMap<String, Material>();
		Map<String, Object> stringEventBlock = (Map<String, Object>) customData.get("event-blocks");
		// convert string to Material
		stringEventBlock.forEach((k, v) -> {
			Material eventBlock = Material.valueOf((String) v);
			this.eventBlocks.put(k, eventBlock);
		});
	}

	private String getEventBlockName(Block b) {
		Material blockType = b.getType();
		for (Entry<String, Material> entry : this.eventBlocks.entrySet()) {
			if (entry.getValue() == blockType) {
				return entry.getKey();
			}
		}

		return null;
	}

	public void processPlayerMove(Player p) {
		Block block = p.getLocation().subtract(0, 1, 0).getBlock();

		String eventBlockName = getEventBlockName(block);
		if (eventBlockName == null) {
			return;
		}

		// process event block event
		switch (eventBlockName) {
		case FINISH:
			finish(p);
			break;
		case RESPAWN:
			respawn(p);
			break;
		case UP_TELEPORT:
			upTeleport(p);
			break;
		case DOWN_TELEPORT:
			downTeleport(p);
			break;
		case FLICKER:
			flicker(p);
			break;
		case HEAL:
			heal(p);
			break;
		case JUMP:
			jump(p);
			break;
		case DEBUFF:
			debuff(p);
			break;
		}

		// send title
		this.parkour.sendTitle(p, ChatColor.BOLD + eventBlockName, "");
	}

	private void finish(Player p) {

		// score +1
		this.parkour.plusScore(p, 1);

		if (this.endless) {
			respawn(p);
		} else {
			this.parkour.finishGame();
		}
	}

	private void respawn(Player p) {

		p.teleport(this.parkour.location());
	}

	private void upTeleport(Player p) {

		p.teleport(p.getLocation().add(0, 3, 0));
	}

	private void downTeleport(Player p) {

		p.teleport(p.getLocation().subtract(0, 3, 0));
	}

	private void flicker(Player p) {

		Block block = p.getLocation().subtract(0, 1, 0).getBlock();
		Material mat = block.getType();

		// make block disappear after 3 seconds
		Bukkit.getScheduler().runTaskLater(AllMiniGamesMain.getInstance(), () -> {
			block.setType(Material.AIR);
		}, 20 * 3);

		// make block appear after 6 seconds
		Bukkit.getScheduler().runTaskLater(AllMiniGamesMain.getInstance(), () -> {
			block.setType(mat);
		}, 20 * 6);
	}

	private void heal(Player p) {

		// heal health, hunger and remove all potion effects
		PlayerTool.makePureState(p);
	}

	private void jump(Player p) {

		Location pLoc = p.getLocation().clone();

		double dirX = pLoc.getDirection().multiply(0.05).getX();
		double dirZ = pLoc.getDirection().multiply(0.05).getZ();

		// jump to the sky
		p.setVelocity(new Vector(dirX, 0.65, dirZ));
	}

	private void debuff(Player p) {

		if (!p.getActivePotionEffects().isEmpty()) {
			return;
		}

		// random debuff
		p.addPotionEffect(PotionEffectTool.getRandomDebuffPotionEffect());
	}
}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
