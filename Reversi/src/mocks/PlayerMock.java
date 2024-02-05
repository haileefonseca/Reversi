package mocks;

import java.util.ArrayList;
import java.util.List;

import controller.PlayerActionListener;
import model.CubeCoord;
import model.ITile;
import player.IReversiPlayer;

/**
 * Mocks players by keeping a log of method calls.
 */
public class PlayerMock implements IReversiPlayer {

  private final List<PlayerActionListener> subs;
  private final StringBuilder log;

  public PlayerMock() {
    this.subs = new ArrayList<>();
    this.log = new StringBuilder();
  }

  @Override
  public void requestMove() {
    log.append("move requested\n");
  }

  @Override
  public void subscribe(PlayerActionListener c) {
    log.append("subscribing PlayerActionListener\n");
    this.subs.add(c);
  }

  @Override
  public void notifyClicked(CubeCoord c) {
    log.append("notify click at " + c + "\n");
    for (PlayerActionListener p : this.subs) {
      p.receiveMove(c);
    }
  }

  @Override
  public void notifyPass() {
    log.append("attempted pass\n");
    for (PlayerActionListener p : this.subs) {
      p.receivePass();
    }
  }

  public String getLog() {
    return this.log.toString();
  }

  @Override
  public ITile.State getColor() {
    log.append("color retrieved\n");
    return ITile.State.BLACK;
  }
}
