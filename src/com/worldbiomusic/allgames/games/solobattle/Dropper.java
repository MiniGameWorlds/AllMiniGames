package com.worldbiomusic.allgames.games.solobattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.minigameworld.managers.event.GameEvent;
import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.PlayerTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

/**
 * [Rules]<br>
 * - 1 Health<br>
 * - PVP off<br>
 * - Teleport to "location" before start<br>
 * - Add scores when a player steps on any "score-blocks"<br>
 * - Teleport to "location" when a player steps on "respawn-block"<br>
 * - Respawn players when die(cancel damage, check below blocks and add
 * scores)<br>
 * - <br>
 * 
 * [Custom data]<br>
 * - score-blocks(Map<Material, Integer>)<br>
 * - respawn-block<br>
 *
 */
public class Dropper extends SoloBattleMiniGame {
	private Map<Material, Integer> scoreBlocks;
	private Material respawnBlock;
	private int health;

	public Dropper() {
		super("Dropper", 2, 10, 60, 20);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14478);

		setting().setIcon(Material.OAK_TRAPDOOR);

		customOption().set(Option.PVP, false);

		registerTasks();
	}

	private void registerTasks() {
		taskManager().registerTask("check-below-block", () -> {
			livePlayers().forEach(p -> {
				checkBelowBlock(p);
			});
		});
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();

		// score blocks
		Map<String, Integer> scoreBlockData = new LinkedHashMap<>();
		scoreBlockData.put(Material.BLUE_WOOL.name(), 1);
		scoreBlockData.put(Material.GREEN_WOOL.name(), 2);
		scoreBlockData.put(Material.YELLOW_WOOL.name(), 3);
		scoreBlockData.put(Material.ORANGE_WOOL.name(), 4);
		scoreBlockData.put(Material.RED_WOOL.name(), 5);
		scoreBlockData.put(Material.BLACK_WOOL.name(), -3);
		data.put("score-blocks", scoreBlockData);

		// respawn block
		data.put("respawn-block", Material.GLASS.name());

		// health
		data.put("health", 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		this.scoreBlocks = new HashMap<>();
		Map<String, Object> data = customData();
		((Map<String, Integer>) data.get("score-blocks")).forEach((k, v) -> {
			this.scoreBlocks.put(Material.valueOf(k), v);
		});

		this.respawnBlock = Material.valueOf((String) data.get("respawn-block"));

		this.health = (int) data.get("health");
	}

	@GameEvent
	protected void onEntityDamageEvent(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();

		boolean isDead = p.getHealth() <= e.getDamage();
		if (isDead) {
			e.setDamage(0);

			PlayerTool.makePureState(p);

			if (!checkBelowBlock(p)) {
				// respawn
				p.teleport(location());
			}
		}
	}

	private boolean checkBelowBlock(Player p) {
		Block block = p.getLocation().subtract(0, 1, 0).getBlock();
		Material blockType = block.getType();

		// check block typep
		if (blockType == this.respawnBlock) {
			respawnBlock(p);
			return true;
		} else if (this.scoreBlocks.keySet().contains(blockType)) {
			scoreBlocks(p, blockType);
			return true;
		}
		return false;
	}

	private void respawnBlock(Player p) {
		// teleport
		p.teleport(location());

		// title
		sendTitle(p, "Respawn", "");

		// sound
		PlayerTool.playSound(p, Sound.BLOCK_NOTE_BLOCK_BANJO);
	}

	private void scoreBlocks(Player p, Material blockType) {
		// score
		int score = this.scoreBlocks.get(blockType);
		String scoreTitle = "" + score;

		if (score > 0) {
			plusScore(p, score);
			scoreTitle = ChatColor.GREEN + "+" + scoreTitle;
		} else {
			minusScore(p, Math.abs(score));
			scoreTitle = ChatColor.RED + scoreTitle;
		}

		// title
		sendTitle(p, scoreTitle, "");

		// sound
		PlayerTool.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL);

		// respawn
		p.teleport(location());
	}

	@Override
	protected void onStart() {
		super.onStart();

		// scores
		sendMessages(ChatColor.BOLD + "\n===[ Scores ]===");
		this.scoreBlocks.forEach((k, v) -> {
			ChatColor color = v > 0 ? ChatColor.GREEN : ChatColor.RED;
			sendMessages(k.name() + ": " + color + v);
		});

		// respawn block
		sendMessages(ChatColor.BOLD + "\n===[ Respawn block ]===");
		sendMessages(this.respawnBlock.name());

		// init players
		players().forEach(p -> {
			p.teleport(location());
			p.setHealthScale(this.health);
		});

		// start task
		taskManager().runTaskTimer("check-below-block", 0, 5);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Drop and get scores!");

		return tutorial;
	}

}
