package model;

import java.util.List;

/**
 * Represents a readable game of Reversi, all public methods return only observations of the
 * game state. There are no methods which modify the game or return any object which would affect
 * the game if mutated.
 */
public interface ReadOnlyReversi {

  /**
   * Returns a copy of the list of tiles that would be affected by a placed piece. Shares knowledge
   * of the effects of a move without the risk of alteration.
   * @param cube the cubic coordinates of the piece to be placed.
   * @param color the color of the piece being placed.
   * @return the list of affected tiles.
   */
  List<ITile> returnResultsOfMove(CubeCoord cube, ITile.State color);

  /**
   * Calculate the score, based on the color given.
   * @param color the color of the pieces being counted.
   * @return the number of pieces of the given color.
   */
  int calcScore(ITile.State color);

  /**
   * Returns the state of the tile at the given coordinates, or throws if there is no tile there.
   * @param cube the cubic coordinates of the tile being accessed.
   * @return the state of the tile at the given coordinates
   * @throws IllegalArgumentException if there is no tile at the given location
   */
  ITile.State getStateAt(CubeCoord cube) throws IllegalArgumentException;

  /**
   * Determines whether the game is over, which occurs when neither player has any valid moves left.
   * @return whether the game is over.
   */
  Boolean gameOver();

  /**
   * Returns the size of the hex board constructed, in terms of "rings".
   * @return the size of the board
   */
  int getBoardSize();

  /**
   * Returns a copy of the board.
   * @return a copy of the board, in its 2D array state.
   */
  ITile[][] duplicate();

  /**
   * Determines whether the given coordinate is a valid move for the given color.
   * @param move The location of the attemped move.
   * @param color The color of the player attempting a move.
   * @return boolean representing whether the move is valid.
   */
  boolean isLegalMove(CubeCoord move, ITile.State color);

  /**
   * Determines whether the given player has a possible move.
   * @return whether there is a move to be made
   */
  boolean hasMoves(ITile.State color);

  /**
   * Returns the currently active player.
   * @return the active player as a color.
   */
  ITile.State getActiveTurn();

  /**
   * Returns if this game is hexagonal.
   * @return true if the game is hexagonal, false if not.
   */
  boolean isHex();

  /**
   * Returns the size of the buffer used by this game of reversi.
   * @return the size of the buffer.
   */
  int getBuffer();

  /**
   * Returns a list of all tile locations that don't already have a piece on them.
   * @return The list of empty tile locations.
   */
  List<CubeCoord> getEmptyTiles();

  /**
   * Returns a list of all the corner tile locations.
   * @return The list of corner tiles.
   */
  List<CubeCoord> getCorners();
}
