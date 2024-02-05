package model;

/**
 * Represents a game of Reversi.
 */
public interface IReversi extends ReadOnlyReversi, ModelStatusPub {

  /**
   * Performs a move, flipping all the resulting sandwiched tiles to the color of the placed piece.
   * @param cube the cubic coordinates of the placed piece on the array board.
   * @param color the color of the placed piece.
   * @throws IllegalStateException if the move is invalid, meaning no tiles are sandwiched as a
   *                               result of the move.
   * @throws IllegalArgumentException if a move is attempted as a "NONE" color, or if a move is
   *                                  attempted by a color not corresponding to the current turn.
   */
  void move(CubeCoord cube, ITile.State color)
          throws IllegalStateException, IllegalArgumentException;

  /**
   * Change the turn to the next player.
   * @throws IllegalStateException if the game is over
   */
  void pass() throws IllegalStateException;

  /**
   * Begin this game of Reversi.
   */
  void startGame();
}
