package com.worldbiomusic.allgames.games.solobattle.mnk;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import com.minigameworld.util.Utils;

/**
 * <h1>Start Point</h1><br>
 * - HORIZONTAL: right<br>
 * - VERTICAL: top<br>
 * - SLASH_DIAGONAL: top-right<br>
 * - REVERSE_SLASH_DIAGONAL: bottom-right<br>
 * 
 * <h1>Move direction</h1><br>
 * - HORIZONTAL: left<br>
 * - VERTICAL: bottom<br>
 * - SLASH_DIAGONAL: bottom-left<br>
 * - REVERSE_SLASH_DIAGONAL: top-left<br>
 */
public class BoardChecker {
	public enum Direction {
		HORIZONTAL, VERTICAL, SLASH_DIAGONAL, REVERSE_SLASH_DIAGONAL;
	}

	private Board board;

	public BoardChecker(Board board) {
		this.board = board;
	}

	public boolean check(Location placedLoc) {
		boolean finished = false;

		for (Direction dir : Direction.values()) {
			Utils.warning("\n");
			finished = finished || checkDirection(dir, placedLoc);
		}

		return finished;
	}

	private boolean checkDirection(Direction dir, Location placedLoc) {
		Material placedBlockMat = placedLoc.getBlock().getType();
		Location startPoint = goToStartPoint(dir, placedLoc.clone());

		// base: 1 (start point block)
		int length = 1;
		while (true) {
			moveOnDirection(dir, startPoint, false);
			Material nextBlockMat = startPoint.getBlock().getType();
			if (nextBlockMat != placedBlockMat || !this.board.isOnPlayBoard(startPoint)) {
				break;
			}

			length += 1;
		}

		Utils.warning(dir + " length: " + length);

		return length == this.board.getLength();
	}

	private void moveOnDirection(Direction dir, Location loc, boolean reverse) {
		Vector v = null;
		if (dir == Direction.HORIZONTAL) {
			v = new Vector(1, 0, 0);
		} else if (dir == Direction.VERTICAL) {
			v = new Vector(0, 0, 1);
		} else if (dir == Direction.SLASH_DIAGONAL) {
			v = new Vector(1, 0, 1);
		} else if (dir == Direction.REVERSE_SLASH_DIAGONAL) {
			v = new Vector(1, 0, -1);
		}

		if (reverse) {
			v = v.multiply(-1);
		}

		loc.subtract(v);
	}

	private Location goToStartPoint(Direction dir, Location placedLoc) {
		Material placedBlockMat = placedLoc.getBlock().getType();
		Location startPoint = placedLoc.clone();

		while (true) {
			moveOnDirection(dir, startPoint, true);
			Material nextBlockMat = startPoint.getBlock().getType();
			if (nextBlockMat != placedBlockMat || !this.board.isOnPlayBoard(startPoint)) {
				moveOnDirection(dir, startPoint, false);
				break;
			}
		}
		return startPoint;
	}

}
