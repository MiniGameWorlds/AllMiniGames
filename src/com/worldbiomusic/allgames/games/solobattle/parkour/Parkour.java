package com.worldbiomusic.allgames.games.solobattle.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;
import com.worldbiomusic.allgames.AllMiniGamesMain;

/**
 * - Before die, teleport to spawn<br>
 * - Server must stopped after flicker blocks appeared<br>
 * - If endless (custom option) is true, player who reached the finish block
 * will teleport to the spawn with +1 score<br>
 * - If endless (custom option) is false, game will be finished when any player
 * reached the finish block<br>
 * 
 * - # Custom-data<br>
 * - `endless`: game endless option<br>
 * 
 * - ## event-block<br>
 * - `finish`: score +1, teleport to spawn<br>
 * - `respawn`: teleport to spawn<br>
 * - `up-teleport`: up-teleport to 3 block<br>
 * - `down-teleport`: down-teleport to 3 block<br>
 * - `flicker`: disappear after 3 seconds, and appear after 3 seconds<br>
 * - `heal`: heal player's health, hunger and remove all potion effects<br>
 * - `jump`: jump to the sky<br>
 * - `debuff`: give random debuff potion effect<br>
 */
public class Parkour extends SoloBattleMiniGame implements Listener {
	private EventBlockManager eventBlockManager;

	public Parkour() {
		super("Parkour", 2, 10, 60 * 5, 15);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14408);

		Bukkit.getServer().getPluginManager().registerEvents(this, AllMiniGamesMain.getInstance());

		this.eventBlockManager = new EventBlockManager(this);

		setting().setIcon(Material.LILY_PAD);

		customOption().set(Option.FOOD_LEVEL_CHANGE, false);

		registerTask();
	}

	private void registerTask() {
		taskManager().registerTask("check-move", () -> {
			players().forEach(p -> eventBlockManager.processPlayerMove(p));
		});
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> eventBlocks = new HashMap<String, Object>();
		eventBlocks.put(EventBlockManager.FINISH, Material.GLOWSTONE.name());
		eventBlocks.put(EventBlockManager.RESPAWN, Material.ORANGE_STAINED_GLASS.name());
		eventBlocks.put(EventBlockManager.UP_TELEPORT, Material.GRAY_STAINED_GLASS.name());
		eventBlocks.put(EventBlockManager.DOWN_TELEPORT, Material.LIGHT_GRAY_STAINED_GLASS.name());
		eventBlocks.put(EventBlockManager.FLICKER, Material.LIGHT_BLUE_STAINED_GLASS.name());
		eventBlocks.put(EventBlockManager.HEAL, Material.PINK_STAINED_GLASS.name());
		eventBlocks.put(EventBlockManager.JUMP, Material.WHITE_STAINED_GLASS.name());
		eventBlocks.put(EventBlockManager.DEBUFF, Material.MAGENTA_STAINED_GLASS.name());

		Map<String, Object> data = customData();
		// endless
		data.put("endless", false);
		// event blocks
		data.put("event-blocks", eventBlocks);
	}

	@Override
	public void loadCustomData() {
		super.loadCustomData();

		// pass custom data to event block manager
		this.eventBlockManager.setCustomData(customData());
	}

	@Override
	protected void onStart() {
		super.onStart();

		players().forEach(p -> p.teleport(location()));

		taskManager().runTaskTimer("check-move", 0, 4);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<String>();
		tutorial.add("Find and step on the finish block!");
		tutorial.add("Many event blocks exist: FINISH, RESPAWN, UP-TP, DOWN-TP, FLICKER, HEAL, JUMP, DEBUFF");

		return tutorial;
	}

	@Override
	public void plusScore(Player p, int amount) {
		super.plusScore(p, amount);
	}
}
