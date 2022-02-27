package com.worldbiomusic.allgames;

import org.bukkit.plugin.java.JavaPlugin;

import com.worldbiomusic.allgames.games.custom.Tutorial;
import com.worldbiomusic.allgames.games.solo.FitTool;
import com.worldbiomusic.allgames.games.solo.HitMob;
import com.worldbiomusic.allgames.games.solo.Tiny;
import com.worldbiomusic.allgames.games.solobattle.Bridge;
import com.worldbiomusic.allgames.games.solobattle.Center;
import com.worldbiomusic.allgames.games.solobattle.Dropper;
import com.worldbiomusic.allgames.games.solobattle.FallingBlock;
import com.worldbiomusic.allgames.games.solobattle.FallingItem;
import com.worldbiomusic.allgames.games.solobattle.ItsMine;
import com.worldbiomusic.allgames.games.solobattle.LavaUp;
import com.worldbiomusic.allgames.games.solobattle.OnePunch;
import com.worldbiomusic.allgames.games.solobattle.PVP;
import com.worldbiomusic.allgames.games.solobattle.RandomScore;
import com.worldbiomusic.allgames.games.solobattle.RockScissorPaper;
import com.worldbiomusic.allgames.games.solobattle.ScoreClimbing;
import com.worldbiomusic.allgames.games.solobattle.Spleef;
import com.worldbiomusic.allgames.games.solobattle.SuperMob;
import com.worldbiomusic.allgames.games.solobattle.mnk.MNK;
import com.worldbiomusic.allgames.games.solobattle.parkour.Parkour;
import com.worldbiomusic.allgames.games.solobattle.timingpvp.TimingPVP;
import com.worldbiomusic.allgames.games.team.BreedMob;
import com.worldbiomusic.allgames.games.team.RemoveBlock;
import com.worldbiomusic.allgames.games.team.TeamTiny;
import com.worldbiomusic.allgames.games.teambattle.HiddenArcher;
import com.worldbiomusic.allgames.games.teambattle.MoreHit;
import com.worldbiomusic.allgames.games.teambattle.PassMob;
import com.worldbiomusic.minigameworld.api.MiniGameWorld;
import com.worldbiomusic.minigameworld.util.Metrics;

public class AllMiniGamesMain extends JavaPlugin {

	private static JavaPlugin instance;

	public static JavaPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		super.onEnable();

		instance = this;

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14386);

		// register minigame
		MiniGameWorld mw = MiniGameWorld.create(MiniGameWorld.API_VERSION);
		mw.registerMiniGame(new FitTool()); // 14387
		mw.registerMiniGame(new RandomScore()); // 14388
		mw.registerMiniGame(new MoreHit()); // 14389
		mw.registerMiniGame(new ScoreClimbing()); // 14390
		mw.registerMiniGame(new RockScissorPaper()); // 14391
		mw.registerMiniGame(new PVP()); // 14409
		mw.registerMiniGame(new RemoveBlock()); // 14393
		mw.registerMiniGame(new HiddenArcher()); // 14394
		mw.registerMiniGame(new BreedMob()); // 14395
		mw.registerMiniGame(new SuperMob()); // 14396
		mw.registerMiniGame(new PassMob()); // 14397
		mw.registerMiniGame(new FallingBlock()); // 14398
		mw.registerMiniGame(new Bridge()); // 14399
		mw.registerMiniGame(new Spleef()); // 14410
		mw.registerMiniGame(new Tiny()); // 14401
		mw.registerMiniGame(new HitMob()); // 14402
		mw.registerMiniGame(new TeamTiny()); // 14403
		mw.registerMiniGame(new MNK()); // 14404
		mw.registerMiniGame(new OnePunch()); // 14405
		mw.registerMiniGame(new Tutorial()); // 14411
		mw.registerMiniGame(new TimingPVP()); // 14407
		mw.registerMiniGame(new Parkour()); // 14408
		mw.registerMiniGame(new FallingItem()); // 14412
		mw.registerMiniGame(new Center()); // 14413
		mw.registerMiniGame(new ItsMine()); // 14414
		mw.registerMiniGame(new LavaUp()); // 14415
		mw.registerMiniGame(new Dropper()); // 14478
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
