package strategy;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;

/**
 * Interface for strategy functions for an IReversi game.
 */
public interface IStrategy {

  /**
   * Method that chooses the move best representing the given strategy.
   * @param model the RO model to find moves for
   * @param color the game-piece color to look for moves for
   * @return the CubeCoord of the best move for this strategy
   * @throws IllegalStateException if the game is in a state where there are no playable moves for
   *                               the given color.
   */
  CubeCoord chooseMove(ReadOnlyReversi model, ITile.State color) throws IllegalStateException;
}
