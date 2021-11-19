package com.worldbiomusic.allgames.games;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;

import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;

public class Bridge extends SoloBattleMiniGame {

	private List<Location> spawnLocations;
	private Material deathBlock;

	public Bridge() {
		super("Bridge", 2, 8, 180, 20);
		
		this.registerTask();
	}

	private void registerTask() {
		this.getTaskManager().registerTask("checkFallen", new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

		Map<String, Object> customData = this.getCustomData();

		// spawnLocations
		List<Location> locs = new ArrayList<Location>();
		for (int i = 0; i < this.getMaxPlayerCount(); i++) {
			locs.add(this.getLocation());
		}
		customData.put("spawnLocations", locs);

		// deathBlock
		customData.put("deathBlock", Material.STONE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> customData = this.getCustomData();
		this.spawnLocations = (List<Location>) customData.get("spawnLocations");
		this.deathBlock = (Material) customData.get("deathBlock");
	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void processEvent(Event arg0) {
	}

	@Override
	protected List<String> registerTutorial() {
		return null;
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
