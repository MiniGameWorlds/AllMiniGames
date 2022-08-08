package com.worldbiomusic.allgames;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.wbm.plugin.util.Metrics;
import com.worldbiomusic.allgames.games.custom.Tutorial;
import com.worldbiomusic.allgames.games.fake.Heal;
import com.worldbiomusic.allgames.games.fake.Hub;
import com.worldbiomusic.allgames.games.fake.Location1;
import com.worldbiomusic.allgames.games.fake.Macro;
import com.worldbiomusic.allgames.games.fake.Teller;
import com.worldbiomusic.allgames.games.solo.FitTool;
import com.worldbiomusic.allgames.games.solo.HitMob;
import com.worldbiomusic.allgames.games.solo.Tiny;
import com.worldbiomusic.allgames.games.solobattle.Bridge;
import com.worldbiomusic.allgames.games.solobattle.Center;
import com.worldbiomusic.allgames.games.solobattle.DodgeBlock;
import com.worldbiomusic.allgames.games.solobattle.Dropper;
import com.worldbiomusic.allgames.games.solobattle.FallingBlock;
import com.worldbiomusic.allgames.games.solobattle.FallingItem;
import com.worldbiomusic.allgames.games.solobattle.HungryFishing;
import com.worldbiomusic.allgames.games.solobattle.ItsMine;
import com.worldbiomusic.allgames.games.solobattle.LavaUp;
import com.worldbiomusic.allgames.games.solobattle.OnePunch;
import com.worldbiomusic.allgames.games.solobattle.PVP;
import com.worldbiomusic.allgames.games.solobattle.RandomScore;
import com.worldbiomusic.allgames.games.solobattle.Rebound;
import com.worldbiomusic.allgames.games.solobattle.RPS;
import com.worldbiomusic.allgames.games.solobattle.ScoreClimbing;
import com.worldbiomusic.allgames.games.solobattle.Spleef;
import com.worldbiomusic.allgames.games.solobattle.StandOnBlock;
import com.worldbiomusic.allgames.games.solobattle.SuperMob;
import com.worldbiomusic.allgames.games.solobattle.clock.Clock;
import com.worldbiomusic.allgames.games.solobattle.mnk.MNK;
import com.worldbiomusic.allgames.games.solobattle.parkour.Parkour;
import com.worldbiomusic.allgames.games.solobattle.timingpvp.TimingPVP;
import com.worldbiomusic.allgames.games.team.BreedMob;
import com.worldbiomusic.allgames.games.team.RemoveBlock;
import com.worldbiomusic.allgames.games.team.TeamTiny;
import com.worldbiomusic.allgames.games.teambattle.HiddenArcher;
import com.worldbiomusic.allgames.games.teambattle.MoreHit;
import com.worldbiomusic.allgames.games.teambattle.PassMob;
import com.worldbiomusic.allgames.utils.UpdateChecker;
import com.minigameworld.api.MiniGameWorld;
import com.minigameworld.util.Setting;
import com.minigameworld.util.Utils;

public class AllMiniGamesMain extends JavaPlugin {

	private static JavaPlugin instance;

	public static JavaPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		printPluginName();

		// check update
		if (Setting.CHECK_UPDATE) {
			UpdateChecker.check();
		}

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14386);

		// register minigame
		MiniGameWorld mw = MiniGameWorld.create(MiniGameWorld.API_VERSION);
		mw.registerGame(new FitTool()); // 14387
		mw.registerGame(new RandomScore()); // 14388
		mw.registerGame(new MoreHit()); // 14389
		mw.registerGame(new ScoreClimbing()); // 14390
		mw.registerGame(new RPS()); // 14391
		mw.registerGame(new PVP()); // 14409
		mw.registerGame(new RemoveBlock()); // 14393
		mw.registerGame(new HiddenArcher()); // 14394
		mw.registerGame(new BreedMob()); // 14395
		mw.registerGame(new SuperMob()); // 14396
		mw.registerGame(new PassMob()); // 14397
		mw.registerGame(new FallingBlock()); // 14398
		mw.registerGame(new Bridge()); // 14399
		mw.registerGame(new Spleef()); // 14410
		mw.registerGame(new Tiny()); // 14401
		mw.registerGame(new HitMob()); // 14402
		mw.registerGame(new TeamTiny()); // 14403
		mw.registerGame(new MNK()); // 14404
		mw.registerGame(new OnePunch()); // 14405
		mw.registerGame(new Tutorial()); // 14411
		mw.registerGame(new TimingPVP()); // 14407
		mw.registerGame(new Parkour()); // 14408
		mw.registerGame(new FallingItem()); // 14412
		mw.registerGame(new Center()); // 14413
		mw.registerGame(new ItsMine()); // 14414
		mw.registerGame(new LavaUp()); // 14415
		mw.registerGame(new Dropper()); // 14478
		mw.registerGame(new StandOnBlock()); // 14573
		mw.registerGame(new DodgeBlock()); // 14723
		mw.registerGame(new Clock()); // 15129
		mw.registerGame(new Rebound()); // 15198
		mw.registerGame(new HungryFishing()); // 15375

		// fake
		mw.registerGame(new Hub()); //
		mw.registerGame(new Location1()); //
		mw.registerGame(new Teller()); //
		mw.registerGame(new Heal()); //
		mw.registerGame(new Macro()); //
	}

	private void printPluginName() {
		String pluginName = AllMiniGamesMain.getInstance().getDescription().getName();
		Utils.info(ChatColor.GREEN + "=============================================");
		Utils.info(ChatColor.RESET + "                  " + pluginName + "               ");
		Utils.info(ChatColor.GREEN + "=============================================");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
