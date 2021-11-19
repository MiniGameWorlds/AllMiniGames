package com.worldbiomusic.allgames;

import org.bukkit.plugin.java.JavaPlugin;

import com.worldbiomusic.allgames.games.BreedMob;
import com.worldbiomusic.allgames.games.FallingBlock;
import com.worldbiomusic.allgames.games.FitTool;
import com.worldbiomusic.allgames.games.HiddenArcher;
import com.worldbiomusic.allgames.games.MoreHit;
import com.worldbiomusic.allgames.games.PVP;
import com.worldbiomusic.allgames.games.PassMob;
import com.worldbiomusic.allgames.games.RandomScore;
import com.worldbiomusic.allgames.games.RemoveBlock;
import com.worldbiomusic.allgames.games.RockScissorPaper;
import com.worldbiomusic.allgames.games.ScoreClimbing;
import com.worldbiomusic.allgames.games.SuperMob;
import com.worldbiomusic.minigameworld.api.MiniGameWorld;

public class AllMiniGamesMain extends JavaPlugin {
	@Override
	public void onEnable() {
		super.onEnable();
		
		// register minigame
		MiniGameWorld mw = MiniGameWorld.create(MiniGameWorld.API_VERSION);
		mw.registerMiniGame(new FitTool());
		mw.registerMiniGame(new RandomScore());
		mw.registerMiniGame(new MoreHit());
		mw.registerMiniGame(new ScoreClimbing());
		mw.registerMiniGame(new RockScissorPaper());
		mw.registerMiniGame(new PVP());
		mw.registerMiniGame(new RemoveBlock());
		mw.registerMiniGame(new HiddenArcher());
		mw.registerMiniGame(new BreedMob());
		mw.registerMiniGame(new SuperMob());
		mw.registerMiniGame(new PassMob());
		mw.registerMiniGame(new FallingBlock());
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
