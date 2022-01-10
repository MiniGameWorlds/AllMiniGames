package com.worldbiomusic.allgames.games.solobattle;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Event;

import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;

public class Center extends SoloBattleMiniGame{

	private int fallenPlayersCount;
	public Center() {
		super("Center", 2, 5, 120, 20);
		
		getSetting().setIcon(Material.END_ROD);
	}

	@Override
	protected void initGameSettings() {
		this.fallenPlayersCount = 0;
	}

	@Override
	protected void processEvent(Event event) {
	}

	@Override
	protected List<String> registerTutorial() {
		return null;
	}

}
