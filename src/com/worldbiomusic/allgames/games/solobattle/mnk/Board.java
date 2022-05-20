package com.worldbiomusic.allgames.games.solobattle.mnk;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.wbm.plugin.util.BlockTool;
import com.wbm.plugin.util.LocationTool;
import com.wbm.plugin.util.instance.TaskManager;

public class Board {
	private BoardChecker boardChecker;
	private List<MNKPlayer> players;

	private MNK mnk;
	private TaskManager taskManager;
	private Location boardPos1, boardPos2;
	private Location playPos1, playPos2;
	private Material boardBlock;
	private Material whitePlayBlock, blackPlayBlock;
	private int turnTime, leftTurnTime;
	private boolean fly;
	private int length;
	private int finishDelay;

	public Board(MNK mnk, TaskManager taskManager, Location boardPos1, Location boardPos2, Material boardBlock,
			Material whitePlayBlock, Material blackPlayBlock, int turnTime, boolean fly, int length, int finishDelay) {
		this.mnk = mnk;
		this.taskManager = taskManager;
		// set custom data
		this.boardPos1 = boardPos1;
		this.boardPos2 = boardPos2;
		this.boardBlock = boardBlock;
		this.whitePlayBlock = whitePlayBlock;
		this.blackPlayBlock = blackPlayBlock;
		this.turnTime = this.leftTurnTime = turnTime;
		this.fly = fly;
		this.length = length;
		this.finishDelay = finishDelay;

		// init
		this.boardChecker = new BoardChecker(this);
		this.players = new ArrayList<MNKPlayer>();
		this.playPos1 = this.boardPos1.clone().add(0, 1, 0);
		this.playPos2 = this.boardPos2.clone().add(0, 1, 0);
	}

	public void onEvent(Event event) {
		if (event instanceof BlockPlaceEvent) {
			playBlockPlaced((BlockPlaceEvent) event);
		} else if (event instanceof PlayerDropItemEvent) {
			// prevent item drop
			PlayerDropItemEvent e = (PlayerDropItemEvent) event;
			e.setCancelled(true);
		} else if (event instanceof PlayerInteractEvent) {
//			placePlayBlockUnderThePlayer((PlayerInteractEvent) event);
		} else if (event instanceof AsyncPlayerChatEvent) {
//			AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
//			
//			Player p = e.getPlayer();
//			MNKPlayer mnkP = getMNKPlayer(p);
//			
//			Material playBlockMat = getColorPlayBlock(mnkP.getColor());
//			e.setMessage(null);
		}
	}

//	private void placePlayBlockUnderThePlayer(PlayerInteractEvent e) {
//		Player p = e.getPlayer();
//		if (p.isSneaking()) {
//
//		}
//	}

	private void playBlockPlaced(BlockPlaceEvent e) {
		// cancel event
		e.setCancelled(true);

		Block block = e.getBlock();
		Material playBlockType = block.getType();
		Location loc = block.getLocation();
		Player p = e.getPlayer();

		// place only play block
		if (!(playBlockType == this.whitePlayBlock || playBlockType == this.blackPlayBlock)) {
			sendMsg(p, "Only play block can be placed");
			return;
		}

		// check is on play board
		if (!isOnPlayBoard(loc)) {
			sendMsg(p, "Place on the board");
			return;
		}

		// check turn
		if (!checkTurn(p)) {
			sendMsg(p, "Not your turn");
			return;
		}

		// check block color with player
		Color pColor = getMNKPlayer(p).getColor();

		if (!isSameColor(pColor, playBlockType)) {
			sendMsg(p, "It's not your color play block");
			return;
		}

		// place play block
		e.setCancelled(false);
		p.getInventory().setItemInMainHand(new ItemStack(playBlockType));
		getMNKPlayer(p).plusPlacedCount();

		// check finished
		// check play board is full
		if (!(checkGameDraw() || checkGameFinished(loc))) {
			changeTurn();
		}
	}

	private boolean checkGameFinished(Location loc) {
		if (check(loc)) {
			Player currentTurnP = getCurrentTurnPlayer().getPlayer();

			// give score
			this.mnk.getPlayerData(currentTurnP).plusScore(1);

			this.players.forEach(all -> {
				sendMsg(all.getPlayer(), currentTurnP.getName() + ChatColor.GREEN + " WIN");
				all.getPlayer().sendTitle(currentTurnP.getName() + ChatColor.GREEN + " WIN", "", 10, 20, 10);
			});

			// spawn firework
			spawnFirework(currentTurnP.getLocation());
			spawnFirework(currentTurnP.getLocation());
			spawnFirework(currentTurnP.getLocation());

			finishGameWithDelay();
			return true;
		}
		return false;
	}

	private void spawnFirework(Location loc) {
		Firework firework = loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fwm = firework.getFireworkMeta();
		Builder builder = FireworkEffect.builder();
		fwm.addEffect(builder.flicker(true).trail(true).withColor(org.bukkit.Color.ORANGE).build());
		fwm.setPower(2);
		firework.setFireworkMeta(fwm);
	}

	private boolean checkGameDraw() {
		if (isPlayBoardFullWithPlayBlocks()) {
			this.players.forEach(all -> {
				sendMsg(all.getPlayer(), ChatColor.YELLOW + "Draw");
				all.getPlayer().sendTitle(ChatColor.YELLOW + "Draw", "", 10, 20, 10);
			});

			finishGameWithDelay();
			return true;
		}
		return false;
	}

	private void finishGameWithDelay() {
		// placed count
		this.mnk.sendMessageToAllPlayers("\n================================");
		this.mnk.sendMessageToAllPlayers("" + ChatColor.GOLD + ChatColor.BOLD + "[Placed Count]");
		MNKPlayer p1 = getPlayers().get(0);
		MNKPlayer p2 = getPlayers().get(1);

		this.mnk.sendMessageToAllPlayers(String.format("%s(%d) : %s(%d)", p1.getPlayer().getName(), p1.getPlacedCount(),
				p2.getPlayer().getName(), p2.getPlacedCount()));

		// cancel flow turn time task
		this.taskManager.cancelTask("flowTurnTime");

		this.taskManager.runTaskLater("finishGame", 20 * this.finishDelay);
	}

	public boolean isSameColor(Color color, Material playBlock) {
		return (color == Color.WHITE) ? playBlock == this.whitePlayBlock : playBlock == this.blackPlayBlock;
	}

	public Material getColorPlayBlock(Color color) {
		return (color == Color.WHITE) ? this.whitePlayBlock : this.blackPlayBlock;
	}

	public void registerPlayers(Player p1, Player p2) {
		Color p1Color = Color.getRandom();
		Color p2Color = p1Color.getOpposite();

		// register players with random color
		this.players.add(new MNKPlayer(p1, p1Color));
		this.players.add(new MNKPlayer(p2, p2Color));

		// give play block
		this.players.forEach(p -> {
			Material playBlock = getColorPlayBlock(p.getColor());
			p.getPlayer().getInventory().addItem(new ItemStack(playBlock));
		});

		// set player's allow flight
		this.players.forEach(p -> p.getPlayer().setAllowFlight(this.fly));

	}

	public void flowTurnTime() {
		this.taskManager.runTaskTimer("flowTurnTime", 0, 20);
	}

	public void init() {
		createBaseBoard();
		clearPlayBoard();
	}

	private void createBaseBoard() {
		BlockTool.fillBlockWithMaterial(boardPos1, boardPos2, boardBlock);
	}

	private void clearPlayBoard() {
		BlockTool.fillBlockWithMaterial(playPos1, playPos2, Material.AIR);
	}

	public boolean isOnPlayBoard(Location target) {
		return LocationTool.isIn(playPos1, target, playPos2);
	}

	private boolean checkTurn(Player p) {
		return getMNKPlayer(p).isTurn();
	}

	private MNKPlayer getCurrentTurnPlayer() {
		return this.players.stream().filter(p -> p.isTurn()).toList().get(0);
	}

	private MNKPlayer getMNKPlayer(Player p) {
		return this.players.stream().filter(op -> op.getPlayer().equals(p)).toList().get(0);
	}

	private boolean check(Location placedLoc) {
		return this.boardChecker.check(placedLoc);
	}

	public void changeTurn() {
		// change turn
		this.players.forEach(p -> {
			p.changeTurn();
		});

		Player turnPlayer = getCurrentTurnPlayer().getPlayer();

		this.players.forEach(mnkP -> {
			Player p = mnkP.getPlayer();
			// send message, title
			sendMsg(p, ChatColor.GREEN + turnPlayer.getName() + " Turn");
			p.sendTitle(ChatColor.GREEN + turnPlayer.getName(), " Turn", 10, 20, 10);

			// make sound
			p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);

//			// change gamemode
//			if (mnkP.isTurn()) {
//				p.setGameMode(GameMode.SURVIVAL);
//			} else {
//
//			}
		});

		// reset left turn time
		this.leftTurnTime = this.turnTime;

	}

	private boolean isPlayBoardFullWithPlayBlocks() {
		return !BlockTool.containsBlock(playPos1, playPos2, Material.AIR);
	}

	private void sendMsg(Player p, String msg) {
		this.mnk.sendMessage(p, msg);
	}

	public boolean decreaseLeftTurnTime() {
		this.leftTurnTime -= 1;

		// show left turn time with player's level
		this.players.forEach(mnkP -> {
			mnkP.getPlayer().setLevel(leftTurnTime);
		});

		return this.leftTurnTime <= 0;
	}

	/*
	 * Getters
	 */
	public BoardChecker getBoardChecker() {
		return boardChecker;
	}

	public List<MNKPlayer> getPlayers() {
		return players;
	}

	public Location getBoardPos1() {
		return boardPos1;
	}

	public Location getBoardPos2() {
		return boardPos2;
	}

	public Location getPlayPos1() {
		return playPos1;
	}

	public Location getPlayPos2() {
		return playPos2;
	}

	public Material getBoardBlock() {
		return boardBlock;
	}

	public Material getWhitePlayBlock() {
		return whitePlayBlock;
	}

	public Material getBlackPlayBlock() {
		return blackPlayBlock;
	}

	public int getTurnTime() {
		return turnTime;
	}

	public boolean isFly() {
		return fly;
	}

	public int getLength() {
		return length;
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
//
//
//
