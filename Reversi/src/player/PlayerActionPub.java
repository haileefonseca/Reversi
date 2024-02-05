package player;

import controller.PlayerActionListener;
import model.CubeCoord;

/**
 * Represents a publisher for player-related messages.
 */
public interface PlayerActionPub {

  /**
   * Indicates to the publisher that a move should be returned.
   * @throws IllegalStateException if no move is possible.
   */
  void requestMove();

  /**
   * Subscribes the given PlayerActionListener to notifications from this publisher.
   * @param c the listener to be subscribed.
   */
  void subscribe(PlayerActionListener c);

  /**
   * Notifies subscribers that a move is being attempted.
   * @param c the location of the attempted move.
   */
  void notifyClicked(CubeCoord c);

  /**
   * Notifies subscribers that a pass is being attempted.
   */
  void notifyPass();
}
