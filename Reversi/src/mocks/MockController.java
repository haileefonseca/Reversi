package mocks;

import controller.ModelStatusListener;
import controller.PlayerActionListener;
import model.CubeCoord;
import model.IReversi;
import model.ITile;
import player.IReversiPlayer;
import view.ViewPub;

/**
 * Mocks controller behavior by recording method calls and subscribing itself.
 */
public class MockController implements ModelStatusListener, PlayerActionListener {

  private final StringBuilder log;
  private final IReversiPlayer player;

  /**
   * Constructs a mock controller.
   * @param game the game being controlled.
   * @param view the view depicting the game.
   * @param player the player doing moves.
   */
  public MockController(IReversi game, ViewPub view, IReversiPlayer player) {
    game.subscribe(this);
    view.subscribe(this);
    this.player = player;
    this.player.subscribe(this);
    this.log = new StringBuilder();
  }

  @Override
  public void turnBegins(ITile.State whoseTurn) {
    log.append("turn begin notif " + whoseTurn + "\n");
    if (this.player.getColor() == whoseTurn) {
      try {
        this.player.requestMove();
      } catch (IllegalStateException e) {
        this.receivePass();
      }
    }
  }

  @Override
  public void receiveMove(CubeCoord loc) {
    log.append("move received\n");
  }

  @Override
  public void receivePass() {
    log.append("pass received\n");
  }

  public String getLog() {
    return this.log.toString();
  }
}
