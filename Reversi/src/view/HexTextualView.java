package view;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;

/**
 * Class for text-based views of a Reversi game.
 */
public class HexTextualView implements TextView {

  // the model being depicted.
  private final ReadOnlyReversi model;

  /**
   * Constructor for a ReversiTextualView, which can render
   * a text representation of the current state of the given model.
   * @param model an IReversi game to represent
   */
  public HexTextualView(ReadOnlyReversi model) {
    this.model = model;
  }


  /**
   * Creates a String representing the state of the board in this model.
   * @return the String representation
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (int row = 0; row < (model.getBoardSize() * 2) - 1; row++) {
      for (int col = 0; col < (model.getBoardSize() * 2) - 1; col++) {
        if (row < model.getBoardSize()) {
          toStringTopHalfOfBoard(result, row, col);
        } else {
          toStringBottomHalfOfBoard(result, row, col);
        }
      }
      if (row < model.getBoardSize() * 2 - 2) {
        result.append("\n");
      }
    }
    return result.toString();
  }

  // helper that appends the boards state at row, col to the given StringBuilder,
  // following the formatting for the top half of the board
  private void toStringTopHalfOfBoard(StringBuilder s, int row, int col) {
    try {
      ITile.State state = this.model.getStateAt(new CubeCoord(row, col, this.model.getBoardSize(),
              true));
      addStateToString(s, state, row, col);
      if (col < this.model.getBoardSize() * 2 - 2) {
        // if this is not the last column in the board, add a space
        addSpace(s);
      }
    } catch (IllegalArgumentException e) {
      // if this element is null (outside the board) add a space (indents)
      addSpace(s);
    }
  }

  // helper that appends the boards state at row, col to the given StringBuilder,
  // following the formatting for the bottom half of the board
  private void toStringBottomHalfOfBoard(StringBuilder s, int row, int col) {
    try {
      ITile.State state = this.model.getStateAt(new CubeCoord(row, col, this.model.getBoardSize(),
              true));
      if (col == 0) {
        // if this is the first column, add the needed spaces to indent
        for (int space = 0; space <= row - this.model.getBoardSize(); space ++) {
          addSpace(s);
        }
        addStateToString(s, state, row, col);
        addSpace(s);
      }
      else {
        addStateToString(s, state, row, col);
        if (row + col < this.model.getBoardSize() * 2 - (3 - this.model.getBoardSize())) {
          addSpace(s);
        }
      }
    }
    catch (IllegalArgumentException ignored) { }
  }

  // appends a String representation of the given ITile.State to the given StringBuilder
  private void addStateToString(StringBuilder s, ITile.State state, int row, int q) {
    if (state == ITile.State.NONE) {
      s.append("_");
    }
    else if (state == ITile.State.BLACK) {
      s.append("X");
    }
    else if (state == ITile.State.WHITE) {
      s.append("O");
    }
  }

  // appends a space to the given StringBuilder
  private void addSpace(StringBuilder s) {
    s.append(" ");
  }

}
