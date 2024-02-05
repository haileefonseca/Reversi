package controller;

import model.CubeCoord;
import model.IReversi;
import model.ITile;
import player.IReversiPlayer;
import view.ViewPub;

/**
 * Controls a game of Reversi, based on the given player and game and drawing to the given view.
 */
public class Controller implements ModelStatusListener, PlayerActionListener {

  // The game being played
  private final IReversi model;

  // A visual representation of the view
  private final ViewPub view;

  // The player being represented
  private final IReversiPlayer player;

  // String representation of the player color (for holiday theme purposes)
  private final String playerColorString;

  /**
   * Constructs a Reversi Controller.
   * @param game the game being played.
   * @param view the view depicting the game.
   * @param player the player beign represented.
   */
  public Controller(IReversi game, ViewPub view, IReversiPlayer player) {
    this.model = game;
    this.model.subscribe(this);
    this.view = view;
    this.view.subscribe(this);
    this.player = player;
    this.player.subscribe(this);
    this.view.setBackground(player.getColor());
    if (this.player.getColor() == ITile.State.BLACK) {
      this.playerColorString = "RED";
    } else {
      this.playerColorString = "WHITE";
    }
  }

  @Override
  public void turnBegins(ITile.State whoseTurn) {
    this.view.refreshView();
    if (this.player.getColor() == whoseTurn) {
      try {
        if (!this.model.hasMoves(whoseTurn)) {
          this.view.pushMessage("Hey " + this.playerColorString
                  + ", its time to pass. You have no valid moves.");
        }
        this.player.requestMove();
      } catch (IllegalStateException e) {
        this.receivePass();
      }
    }
  }

  @Override
  public void receiveMove(CubeCoord loc) {
    if (loc == null) {
      return;
    }
    if (this.model.getStateAt(loc) != ITile.State.NONE) {
      this.view.pushMessage("You cant put a piece on another piece!");
    } else {
      try {
        this.model.move(loc, this.player.getColor());
      } catch (IllegalStateException e) {
        this.view.pushMessage("That's not a valid move. Do better.");
      } catch (IllegalArgumentException e) {
        this.view.pushMessage("Hold your horses, its not your turn yet! " + this.playerColorString);
      }
    }
    this.view.refreshView();
  }

  @Override
  public void receivePass() {
    try {
      this.model.pass();
    } catch (IllegalStateException e) {
      this.handleGameOver();
    }
    this.view.refreshView();
  }

  /**
   * Determines the winner of the game and pushes a message stating the winning color to
   * the view.
   */
  private void handleGameOver() {
    String result = "Game over! ";
    int black = this.model.calcScore(ITile.State.BLACK);
    int white = this.model.calcScore(ITile.State.WHITE);
    if (black > white) {
      result += "Red player wins!";
    } else if (black == white) {
      result += "Tie game!";
    } else {
      result += "White player wins!";
    }
    result += "\nscores: red: " + black + " white: " + white;
    this.view.pushMessage(result);
  }
}
