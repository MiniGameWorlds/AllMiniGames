package com.worldbiomusic.allgames.games.solobattle.timingpvp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.wbm.plugin.util.PlayerTool;
import com.worldbiomusic.allgames.AllMiniGamesMain;
import com.worldbiomusic.minigameworld.minigameframes.SoloBattleMiniGame;
import com.worldbiomusic.minigameworld.minigameframes.helpers.MiniGameCustomOption.Option;
import com.wbm.plugin.util.Metrics;

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
		getSetting().setIcon(Material.CHORUS_FRUIT);

		getCustomOption().set(Option.PVP, true);

		registerTask();
	}

	private void registerTask() {
		getTaskManager().registerTask("randomItems", () -> {
			getPlayers().forEach(p -> p.getInventory().clear());
			randomGiver.giveRandomItemsToPlayers(getPlayers());
		});
	}

	protected void runTaskAfterStart() {
		// start random item task
		getTaskManager().runTaskTimer("randomItems", 0, 20 * this.timingDelay);

		// give 100 scores
		plusEveryoneScore(100);
	}

	@Override
	protected void registerCustomData() {
		super.registerCustomData();

		// timinig delay
		getCustomData().put("timingDelay", 10);

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
		getCustomData().put("randomItems", randomItems);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		this.timingDelay = (int) getCustomData().get("timingDelay");

		this.randomGiver.setRandomItemList((List<ItemStack>) getCustomData().get("randomItems"));
	}

	@Override
	protected void initGameSettings() {
	}

	@Override
	protected void processEvent(Event event) {
		if (event instanceof PlayerDropItemEvent) {
			((PlayerDropItemEvent) event).setCancelled(true);
		} else if (event instanceof EntityDamageEvent) {
			processPlayerDeath((EntityDamageEvent) event);
		}
	}

	private void processPlayerDeath(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}

		Player victim = (Player) e.getEntity();

		boolean death = (victim.getHealth() - e.getDamage()) <= 0;
		if (!death) {
			return;
		}

		sendTitle(victim, ChatColor.RED + "Die", "");
		sendMessageToAllPlayers(ChatColor.BOLD + victim.getName() + ChatColor.RED + " Died");

		// minus score
		minusScore(victim, 1);

		// cancel event
		e.setDamage(0);

		// make player pure state (heal & remove potion effects)
		PlayerTool.makePureState(victim);
	}

	@Override
	protected List<String> registerTutorial() {
		List<String> tutorial = new ArrayList<String>();

		tutorial.add("All players' armor and weapons will be changed regularly");
		tutorial.add("All players' armor and weapons  will be changed regularly");
		tutorial.add("Die: " + ChatColor.RED + "-1");

		return tutorial;
	}

}

