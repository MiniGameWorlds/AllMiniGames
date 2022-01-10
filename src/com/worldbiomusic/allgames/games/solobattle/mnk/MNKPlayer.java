package com.worldbiomusic.allgames.games.solobattle.mnk;

import org.bukkit.entity.Player;

public class MNKPlayer {
	private Player p;
	private boolean turn;
	private Color color;
	private int placedCount;

	public MNKPlayer(Player p, Color color) {
		this.p = p;
		// black is first turn
		this.color = color;
		this.turn = (this.color == Color.BLACK);
		this.placedCount = 0;
	}

	public boolean isTurn() {
		return turn;
	}

	public void changeTurn() {
		this.turn = !turn;
	}

	public Player getPlayer() {
		return p;
	}

	public Color getColor() {
		return color;
	}

	public int getPlacedCount() {
		return placedCount;
	}

	public void plusPlacedCount() {
		this.placedCount += 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof MNKPlayer) {
			return getPlayer().equals(((MNKPlayer) obj).getPlayer());
		}
		return false;
	}
}
