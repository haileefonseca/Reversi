package player;

import java.util.ArrayList;
import java.util.List;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;
import strategy.IStrategy;
import controller.PlayerActionListener;

/**
 * Represents an AI that can attempts to determine moves for a game of Reversi.
 */
public class AIPlayer implements IReversiPlayer {

  // The game being played.
  private final ReadOnlyReversi model;

  // The Strategy used by the AI.
  private final IStrategy strat;

  // The color this AI is playing as.
  private final ITile.State color;

  // The controllers subscribed to this AI.
  private final List<PlayerActionListener> subs;

  /**
   * Constructs an AI Reversi player.
   * @param model The game being played.
   * @param strat The Strategy being used by this AI.
   * @param color The color this AI is playing as.
   */
  public AIPlayer(ReadOnlyReversi model, IStrategy strat, ITile.State color) {
    this.model = model;
    this.strat = strat;
    this.color = color;
    this.subs = new ArrayList<>();
  }

  @Override
  public void requestMove() {
    this.notifyClicked(this.strat.chooseMove(this.model, this.color));
  }

  @Override
  public void notifyClicked(CubeCoord c) {
    for (PlayerActionListener sub : subs) {
      sub.receiveMove(c);
    }
  }

  @Override
  public void notifyPass() {
    for (PlayerActionListener sub : subs) {
      sub.receivePass();
    }
  }

  @Override
  public void subscribe(PlayerActionListener c) {
    this.subs.add(c);
  }

  @Override
  public ITile.State getColor() {
    return this.color;
  }
}