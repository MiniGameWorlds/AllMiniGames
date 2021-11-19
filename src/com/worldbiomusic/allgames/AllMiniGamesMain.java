package com.worldbiomusic.allgames;

import org.bukkit.plugin.java.JavaPlugin;

import com.worldbiomusic.allgames.games.solo.FitTool;
import com.worldbiomusic.allgames.games.solobattle.FallingBlock;
import com.worldbiomusic.allgames.games.solobattle.PVP;
import com.worldbiomusic.allgames.games.solobattle.RandomScore;
import com.worldbiomusic.allgames.games.solobattle.RockScissorPaper;
import com.worldbiomusic.allgames.games.solobattle.ScoreClimbing;
import com.worldbiomusic.allgames.games.solobattle.SuperMob;
import com.worldbiomusic.allgames.games.team.BreedMob;
import com.worldbiomusic.allgames.games.team.RemoveBlock;
import com.worldbiomusic.allgames.games.teambattle.HiddenArcher;
import com.worldbiomusic.allgames.games.teambattle.MoreHit;
import com.worldbiomusic.allgames.games.teambattle.PassMob;
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
