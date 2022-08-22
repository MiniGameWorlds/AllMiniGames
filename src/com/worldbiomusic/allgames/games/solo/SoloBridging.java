package com.worldbiomusic.allgames.games.solo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.minigameworld.frames.SoloMiniGame;
import com.minigameworld.frames.helpers.MiniGameCustomOption.Option;
import com.minigameworld.managers.event.GameEvent;
import com.wbm.plugin.util.Msgs;
import com.wbm.plugin.util.PlayerTool;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SoloBridging extends SoloMiniGame {

	private List<ItemStack> kit;
	private Material finishBlock;

	public SoloBridging() {
		super("SoloBridging", 300, 5);

		setting().setInstanceWorld(true);
		setting().setIcon(Material.LEATHER_BOOTS);
		customOption().set(Option.INVENTORY_SAVE, true);
		customOption().set(Option.BLOCK_BREAK, true);
		customOption().set(Option.BLOCK_PLACE, true);
		customOption().set(Option.COLOR, ChatColor.AQUA);

		taskManager().registerTask("actionbar", () -> {
			updateActionBar();
		});
	}

	private void updateActionBar() {
		Msgs.msg(getSoloPlayer(), ChatMessageType.ACTION_BAR, new TextComponent("" + ChatColor.YELLOW + ChatColor.BOLD
				+ "Score" + ChatColor.RESET + ": " + ChatColor.GREEN + ChatColor.BOLD + leftPlayTime()));
	}

	@Override
	protected void initCustomData() {
		super.initCustomData();

		Map<String, Object> data = customData();
		data.put("kit",
				List.of(new ItemStack(Material.WOODEN_PICKAXE), new ItemStack(Material.WOODEN_AXE),
						new ItemStack(Material.WOODEN_SHOVEL), new ItemStack(Material.WOODEN_SWORD),
						new ItemStack(Material.WOODEN_HOE)));
		data.put("finish-block", Material.GLOWSTONE.name());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadCustomData() {
		super.loadCustomData();

		Map<String, Object> data = customData();
		this.kit = (List<ItemStack>) data.get("kit");
		this.finishBlock = Material.valueOf((String) data.get("finish-block"));
	}

	@GameEvent(forced = true)
	public void onBlockBreak(BlockBreakEvent e) {
		Block b = e.getBlock();
		if (b == null) {
			return;
		}

		// prevent finish block breaking
		if (this.finishBlock == b.getType()) {
			e.setCancelled(true);
		}
	}

	@GameEvent
	public void onPlayerHurt(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();

		// respawn to the location
		if (p.getHealth() <= e.getDamage()) {
			e.setDamage(0);

			PlayerTool.makePureState(p);
			p.teleport(location());
			playSounds(Sound.ENTITY_ENDERMAN_TELEPORT);
			sendTitles(ChatColor.GREEN + "Respawn", "");
		}
	}

	@GameEvent
	public void onClickFinishBlock(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();
		if (b == null) {
			return;
		}

		// finish
		if (b.getType() == this.finishBlock) {
			playSounds(Sound.ENTITY_PLAYER_LEVELUP);
			sendTitles(ChatColor.GREEN + "@ FINISH @", "");
			spawnFirework(b.getLocation().add(0.5, 1, 0.5));

			finishGame();
		}
	}

	private void spawnFirework(Location loc) {
		Firework firework = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fwm = firework.getFireworkMeta();
		Builder builder = FireworkEffect.builder();
		fwm.addEffect(builder.flicker(true).trail(true).withColor(org.bukkit.Color.ORANGE).build());
		fwm.setPower(1);
		firework.setFireworkMeta(fwm);
	}

	@Override
	protected void onFinishDelay() {
		super.onFinishDelay();

		// give score with left time
		plusScore(leftPlayTime());

		int duration = playTime() - leftPlayTime();
		sendMessages("You took " + ChatColor.GREEN + duration + ChatColor.RESET + " seconds to finish the game");
	}

	@Override
	protected void onStart() {
		super.onStart();

		// kit
		giveKit();

		// tp to spawn
		getSoloPlayer().teleport(location());

		// start actionbar update task
		taskManager().runTaskTimer("actionbar", 0, 20);
	}

	private void giveKit() {
		Inventory inv = getSoloPlayer().getInventory();
		kit.forEach(inv::addItem);
	}

	@Override
	protected List<String> tutorial() {
		List<String> t = new ArrayList<>();
		t.add("Break blocks with your kit and bridge to the finish point");
		return t;
	}

}
