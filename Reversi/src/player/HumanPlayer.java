package player;

import controller.PlayerActionListener;
import model.CubeCoord;
import model.ITile;

/**
 * Represents a human Reversi player.
 */
public class HumanPlayer implements IReversiPlayer {

  // The color this player is playing as.
  private final ITile.State color;

  /**
   * Constructs a representation of human player.
   * @param color the color being played as.
   */
  public HumanPlayer(ITile.State color) {
    this.color = color;
    // HumanPlayer previously stored a model for future expansion, but was removed for style
    // purposes.
  }

  @Override
  public ITile.State getColor() {
    return this.color;
  }

  // Methods inherited from PlayerActionPub have no effect in a human player class, since human
  // moves are handled by the view.
  @Override
  public void requestMove() {
    // no function in a human player
  }

  @Override
  public void notifyClicked(CubeCoord c) {
    // no function in a human player
  }

  @Override
  public void notifyPass() {
    // no function in a human player
  }

  @Override
  public void subscribe(PlayerActionListener c) {
    // no function in a human player
  }
}
