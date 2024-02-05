package model;

import controller.ModelStatusListener;

/**
 * Represents a publisher for gameplay-related messages.
 */
public interface ModelStatusPub {

  /**
   * Indicates to subscribers that a new turn has begun.
   */
  public void notifyTurnChange();

  /**
   * Subscribes the given Controller to messages from this publisher.
   * @param c The Controller being subscribed.
   */
  public void subscribe(ModelStatusListener c);
}
