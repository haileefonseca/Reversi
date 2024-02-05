import controller.Controller;
import model.HexReversi;
import model.IReversi;
import model.ITile;
import model.SquareReversi;
import player.AIPlayer;
import player.HumanPlayer;
import player.IReversiPlayer;
import strategy.AvoidAroundCorners;
import strategy.HighestScore;
import view.GraphicView;
import view.HintFeature;
import view.ShapePainter;
import view.ViewPub;

import static java.lang.Integer.parseInt;

/**
 * Class that runs a Reversi game.
 */
public final class Reversi {

  /**
   * Launches a graphic view of a Reversi game.
   * @param args main method arguments
   */
  public static void main(String[] args) {
    IReversi game = configureGame(args);
    game.startGame();
  }

  private static IReversi configureGame(String[] args) {
    int workingIndex = 0;
    int gameSize = 4;
    try {
      gameSize = parseInt(args[workingIndex]);
    } catch (IndexOutOfBoundsException ignored) {
      // no effect
    }
    if (gameSize <= 1) {
      throw new IllegalArgumentException("Invalid board size");
    }
    workingIndex++;
    IReversi game = createGame(args[workingIndex], gameSize);
    workingIndex++;
    IReversiPlayer p1 = new HumanPlayer(ITile.State.BLACK);
    try {
      p1 = createPlayer(args[workingIndex], game, ITile.State.BLACK);
    } catch (IndexOutOfBoundsException noInput) {
      // no effect
    }
    workingIndex++;
    IReversiPlayer p2 = new HumanPlayer(ITile.State.WHITE);
    try {
      p2 = createPlayer(args[workingIndex], game, ITile.State.WHITE);
    } catch (IndexOutOfBoundsException noInput) {
      // no effect
    }
    ViewPub view1 = new GraphicView(game, new HintFeature(new ShapePainter(game), game));
    ViewPub view2 = new GraphicView(game, new HintFeature(new ShapePainter(game), game));
    Controller c1 = new Controller(game, view1, p1);
    Controller c2 = new Controller(game, view2, p2);
    view1.setVisible(true);
    view2.setVisible(true);
    return game;
  }

  private static IReversi createGame(String userInput, int size) {
    switch (userInput) {
      case "hex":
        return new HexReversi(size);
      case "square":
        return new SquareReversi(size);
      default:
        throw new IllegalArgumentException("invalid board type");
    }
  }

  private static IReversiPlayer createPlayer(String userInput, IReversi model, ITile.State color) {
    switch (userInput) {
      case "human":
        return new HumanPlayer(color);
      case "highscore":
        return new AIPlayer(model, new HighestScore(), color);
      case "avoidcorners":
        return new AIPlayer(model, new AvoidAroundCorners(), color);
      case "combo":
        return new AIPlayer(model, new AvoidAroundCorners(new HighestScore()), color);
      default:
        throw new IllegalArgumentException("Invalid player type");
    }
  }
}