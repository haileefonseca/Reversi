package model;

/**
 * Represents a simple tile of any shape.
 */
public interface ITile {

  /**
   * Represents the color of a game piece currently on this tile, or NONE if there is no piece.
   */
  enum State {
    BLACK, WHITE, NONE;
  }

  /**
   * Returns the color of the piece on this tile.
   * @return the color of the piece as a State.
   */
  State getState();

  /**
   * Reassigns the State of this tile to the given State.
   * @param newState the color this tile's piece should be set to.
   */
  void setState(State newState);
}
