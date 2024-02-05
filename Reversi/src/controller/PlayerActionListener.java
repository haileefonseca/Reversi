package controller;

import model.CubeCoord;

/**
 * Represents a listener for player behavior. Accepts messages from players.
 */
public interface PlayerActionListener {

  /**
   * Processes an attempted move by this controller's player at the given location.
   * @param loc The location of the attempted move.
   */
  void receiveMove(CubeCoord loc);

  /**
   * Processes an attempted pass by the player this controller represents. Ends the game if both
   * players have passed.
   */
  void receivePass();
}
