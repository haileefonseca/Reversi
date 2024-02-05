package view;

import model.ITile;
import player.PlayerActionPub;

/**
 * Interface for graphics-based views of a game that can publish messages to subscribers.
 */
public interface ViewPub extends PlayerActionPub {

  /**
   * Redraws the board represented by the view.
   */
  public void refreshView();

  /**
   * Changes the color of the background of this view.
   * @param color the new background color.
   */
  public void setBackground(ITile.State color);

  /**
   * Pushes the given message to the view to be displayed.
   * @param message the message being displayed.
   */
  public void pushMessage(String message);

  /**
   * Sets the visibility of the view to the given boolean.
   * @param start whether the view should be visible.
   */
  public void setVisible(boolean start);
}
