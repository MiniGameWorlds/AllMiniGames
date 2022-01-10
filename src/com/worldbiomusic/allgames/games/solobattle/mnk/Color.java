package com.worldbiomusic.allgames.games.solobattle.mnk;

public enum Color {
	WHITE, BLACK;

	public static Color getRandom() {
		return Math.random() < 0.5 ? WHITE : BLACK;
	}

	public Color getOpposite() {
		return (this == WHITE) ? BLACK : WHITE;
	}
	
}
