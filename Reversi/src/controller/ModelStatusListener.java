package controller;

import model.ITile;

/**
 * Represents a listener for model behavior. Accepts messages related to gameplay.
 */
public interface ModelStatusListener {

  /**
   * Checks if the turn that just began corresponds to this controller's player. If so, attempt
   * a move.
   * @param whoseTurn The color representing the player whose turn it is.
   */
  void turnBegins(ITile.State whoseTurn);
}
