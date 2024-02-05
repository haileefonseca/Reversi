package player;

import model.ITile;

/**
 * Represents an IReversi player.
 */
public interface IReversiPlayer extends PlayerActionPub {

  /**
   * Returns the color of this player.
   * @return the color of this player.
   */
  ITile.State getColor();
}
