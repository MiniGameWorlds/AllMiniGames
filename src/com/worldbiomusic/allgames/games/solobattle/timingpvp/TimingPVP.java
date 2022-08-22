package com.worldbiomusic.allgames.games.solobattle.timingpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.minigameworld.frames.SoloBattleMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.Metrics;
import com.wbm.plugin.util.PlayerTool;
import com.wbm.plugin.util.SoundTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;

/**
 * - All players get random armor and weapons<br>
 * - All players' armor and weapons will be changed regularly(after inventory
 * clear)<br>
 * - Custom-data: timing-delay(second), random inventory items<br>
 * - Players never die(set live to false before death)<br>
 * - Die: -1<br>
 * - PVP on<br>
 * - Disable item drop<br>
 *
 */
public class TimingPVP extends SoloBattleMiniGame {
	private int timingDelay;
	private RandomItemGiver randomGiver;

	public TimingPVP() {
		super("TimingPVP", 2, 10, 90, 15);

		// bstats
		new Metrics(AllMiniGamesMain.getInstance(), 14407);

		this.randomGiver = new RandomItemGiver();
		setting().setIcon(Material.CHORUS_FRUIT);

		customOption().set(Option.PVP, true);

		registerTask();
	}

	private void registerTask() {
		taskManager().registerTask("random-items", () -> {
			players().forEach(p -> p.getInventory().clear());
			randomGiver.giveRandomItemsToPlayers(players());

			// title
			sendTitles(ChatColor.GREEN + "Reset", "items");

			// sound
			SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_BELL);
		});
	}

	protected void onStart() {
		// start random item task
		taskManager().runTaskTimer("random-items", 0, 20 * this.timingDelay);

		// give 100 scores
		plusEveryoneScore(100);
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		// timinig delay
		customData().put("timing-delay", 10);

		// random items
		List<ItemStack> randomItems = new ArrayList<ItemStack>();
		randomItems.add(new ItemStack(Material.WOODEN_SWORD));
		randomItems.add(new ItemStack(Material.STONE_SWORD));
		randomItems.add(new ItemStack(Material.IRON_SWORD));
		randomItems.add(new ItemStack(Material.GOLDEN_SWORD));
		randomItems.add(new ItemStack(Material.DIAMOND_SWORD));

		randomItems.add(new ItemStack(Material.WOODEN_AXE));
		randomItems.add(new ItemStack(Material.STONE_AXE));
		randomItems.add(new ItemStack(Material.IRON_AXE));
		randomItems.add(new ItemStack(Material.GOLDEN_AXE));
		randomItems.add(new ItemStack(Material.DIAMOND_AXE));

		randomItems.add(new ItemStack(Material.BOW));
		randomItems.add(new ItemStack(Material.CROSSBOW));

		randomItems.add(new ItemStack(Material.TRIDENT));
		customData().put("random-items", randomItems);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		this.timingDelay = (int) customData().get("timing-delay");
		this.randomGiver.setRandomItemList((List<ItemStack>) customData().get("random-items"));
	}

	@GameEvent
	protected void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@GameEvent
	protected void onPlayerDeath(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}

		Player victim = (Player) e.getEntity();

		boolean death = (victim.getHealth() - e.getDamage()) <= 0;
		if (!death) {
			return;
		}

		// title, msg, sound
		sendTitle(victim, ChatColor.RED + "Die", "");
		sendMessages(ChatColor.BOLD + victim.getName() + ChatColor.RED + " died");
		SoundTool.play(players(), Sound.BLOCK_NOTE_BLOCK_CHIME);

		// minus score
		minusScore(victim, 1);

		// cancel event
		e.setDamage(0);

		// make player pure state (heal & remove potion effects)
		PlayerTool.makePureState(victim);
	}

	@Override
	protected List<String> tutorial() {
		List<String> tutorial = new ArrayList<String>();

		tutorial.add("All players' armor and weapons will be changed regularly");
		tutorial.add("Die: " + ChatColor.RED + "-1");

		return tutorial;
	}

}
