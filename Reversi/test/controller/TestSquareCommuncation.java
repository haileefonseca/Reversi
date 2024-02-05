package controller;

import org.junit.Assert;
import org.junit.Test;

import mocks.MockController;
import mocks.PlayerMock;
import mocks.ViewPubMock;
import model.CubeCoord;
import model.IReversi;
import model.ITile;
import model.SquareReversi;
import player.AIPlayer;
import player.HumanPlayer;
import player.IReversiPlayer;
import strategy.HighestScore;
import view.GraphicView;
import view.ShapePainter;
import view.ViewPub;

/**
 * Tests communcation between SquareReversi, the view, and controller.
 */
public class TestSquareCommuncation {
  ITile.State b = ITile.State.BLACK;
  ITile.State w = ITile.State.WHITE;

  @Test
  public void testAIPingsMove() {
    IReversi model = new SquareReversi(4);
    ViewPub v1 = new ViewPubMock(model);
    ViewPub v2 = new ViewPubMock(model);
    IReversiPlayer p1 = new AIPlayer(model, new HighestScore(), b);
    IReversiPlayer p2 = new AIPlayer(model, new HighestScore(), w);
    MockController controller1 = new MockController(model, v1, p1);
    MockController controller2 = new MockController(model, v2, p2);
    controller1.turnBegins(b);
    controller2.turnBegins(b);
    Assert.assertEquals("turn begin notif BLACK\n" +
            "move received\n", controller1.getLog());
    Assert.assertEquals("turn begin notif BLACK\n", controller2.getLog());
  }

  @Test
  public void testAIPingsPass() {
    IReversi model = new SquareReversi(2);
    ViewPub v1 = new ViewPubMock(model);
    ViewPub v2 = new ViewPubMock(model);
    IReversiPlayer p1 = new AIPlayer(model, new HighestScore(), b);
    IReversiPlayer p2 = new AIPlayer(model, new HighestScore(), w);
    MockController controller1 = new MockController(model, v1, p1);
    MockController controller2 = new MockController(model, v2, p2);
    controller1.turnBegins(b);
    controller2.turnBegins(b);
    Assert.assertEquals("turn begin notif BLACK\npass received\n", controller1.getLog());
    Assert.assertEquals("turn begin notif BLACK\n", controller2.getLog());
  }

  @Test
  public void testViewPings() {
    IReversi model = new SquareReversi(4);
    ViewPubMock v1 = new ViewPubMock(model);
    ViewPub v2 = new ViewPubMock(model);
    IReversiPlayer p1 = new AIPlayer(model, new HighestScore(), b);
    MockController controller1 = new MockController(model, v1, p1);
    v1.notifyClicked(new CubeCoord(0, 0, 0, false));
    v1.notifyPass();
    Assert.assertEquals("added a subscriber\n" +
            "click occurred in the view at q: 0, r: 0, s: 0\n" +
            "the view received a pass\n", v1.getLog());
    Assert.assertEquals("move received\npass received\n", controller1.getLog());
  }

  @Test
  public void testPlayersContactedRegularGame() {
    IReversi model = new SquareReversi(4);
    ViewPub v1 = new GraphicView(model, new ShapePainter(model));
    PlayerMock p1 = new PlayerMock();
    Controller c1 = new Controller(model, v1, p1);
    c1.turnBegins(b);
    Assert.assertEquals("subscribing PlayerActionListener\n" +
            "color retrieved\n" +
            "color retrieved\n" +
            "color retrieved\n" +
            "move requested\n", p1.getLog());
  }

  @Test
  public void testPlayerContactedForcedToPass() {
    IReversi model = new SquareReversi(4);
    ViewPub v1 = new GraphicView(model, new ShapePainter(model));
    PlayerMock p1 = new PlayerMock();
    Controller c1 = new Controller(model, v1, p1);
    MockController c2 = new MockController(model, v1, p1);
    c1.turnBegins(b);
    p1.notifyPass();
    Assert.assertEquals("turn begin notif BLACK\npass received\n", c2.getLog());
    Assert.assertEquals("subscribing PlayerActionListener\n" +
            "color retrieved\n" +
            "color retrieved\n" +
            "subscribing PlayerActionListener\n" +
            "color retrieved\n" +
            "move requested\n" +
            "attempted pass\n" +
            "color retrieved\n" +
            "move requested\n" +
            "color retrieved\n" +
            "move requested\n", p1.getLog());
  }

  @Test
  public void testModelContactsControllers() {
    IReversi model = new SquareReversi(4);
    ViewPub v1 = new GraphicView(model, new ShapePainter(model));
    PlayerMock p1 = new PlayerMock();
    ViewPub v2 = new GraphicView(model, new ShapePainter(model));
    PlayerMock p2 = new PlayerMock();
    MockController c = new MockController(model, v1, p1);
    MockController c2 = new MockController(model, v2, p2);
    model.startGame();
    Assert.assertEquals("turn begin notif WHITE\n", c.getLog());
    Assert.assertEquals("turn begin notif WHITE\n", c2.getLog());
  }


  @Test
  public void testPushMessageGameOver() {
    IReversi game = new SquareReversi(4);
    ViewPubMock v1 = new ViewPubMock(game);
    ViewPubMock v2 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(w);
    IReversiPlayer p2 = new HumanPlayer(b);
    Controller c1 = new Controller(game, v1, p1);
    Controller c2 = new Controller(game, v2, p2);
    v1.notifyPass();
    v2.notifyPass();
    Assert.assertTrue(v2.getLog().contains("pushed the message Game over!"));
  }

  @Test
  public void testPushNotYourTurn() {
    IReversi game = new SquareReversi(4);
    ViewPubMock v1 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(b);
    Controller c1 = new Controller(game, v1, p1);
    v1.notifyClicked(new CubeCoord(3,1, 1, false));

    Assert.assertTrue(v1.getLog().contains("pushed the message " +
            "Hold your horses, its not your turn yet! RED"));
  }

  @Test
  public void testPushIllegalMove() {
    IReversi game = new SquareReversi(4);
    ViewPubMock v1 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(w);
    Controller c1 = new Controller(game, v1, p1);
    v1.notifyClicked(new CubeCoord(0, 0, 0, false));
    Assert.assertTrue(v1.getLog().contains("That's not a valid move. Do better."));
  }

  @Test
  public void testPushMoveOnTopOfPieceSameColor() {
    IReversi game = new SquareReversi(4);
    ViewPubMock v1 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(b);
    Controller c1 = new Controller(game, v1, p1);
    v1.notifyClicked(new CubeCoord(1, 1, 1, false));
    Assert.assertTrue(v1.getLog().contains("You cant put a piece on another piece!"));
  }

  @Test
  public void testPushMoveOnTopOfPieceDifferentColor() {
    IReversi game = new SquareReversi(4);
    ViewPubMock v1 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(b);
    Controller c1 = new Controller(game, v1, p1);
    v1.notifyClicked(new CubeCoord(2, 1, 0, false));
    Assert.assertTrue(v1.getLog().contains("You cant put a piece on another piece!"));
  }


}
