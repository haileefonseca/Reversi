package view;

import model.ITile;
import model.SquareReversi;

/**
 * Class for text based views of a square Reversi game.
 */
public class SquareTextView implements TextView {
  SquareReversi model;

  /**
   * Constructs a textual view of this SquareReversi game.
   * @param game the game being depicted.
   */
  public SquareTextView(SquareReversi game) {
    this.model = game;
  }

  /**
   * Returns a string rendering of this square Reversi game.
   * @return a String representation of the current board state
   */
  public String toString() {
    ITile[][] dupe = this.model.duplicate();
    StringBuilder result = new StringBuilder();
    for (int row = 0; row < dupe.length; row++) {
      for (int col = 0; col < dupe.length; col ++) {
        if (dupe[row][col].getState() == ITile.State.BLACK) {
          result.append("X");
        }
        if (dupe[row][col].getState() == ITile.State.WHITE) {
          result.append("O");
        }
        if (dupe[row][col].getState() == ITile.State.NONE) {
          result.append("_");
        }
        if (col < dupe.length - 1) {
          result.append(" ");
        }
      }
      if (row < dupe.length - 1) {
        result.append("\n");
      }
    }
    return result.toString();
  }

}
