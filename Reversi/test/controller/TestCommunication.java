package controller;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import mocks.ReversiMock;
import mocks.MockController;
import mocks.PlayerMock;
import mocks.ViewPubMock;
import model.CubeCoord;
import model.HexReversi;
import model.IReversi;
import model.ITile;
import player.AIPlayer;
import player.HumanPlayer;
import player.IReversiPlayer;
import strategy.HighestScore;
import view.GraphicView;
import view.ShapePainter;
import view.ViewPub;

/**
 * Tests communication between the models, controller, views, and players.
 */
public class TestCommunication {
  ITile.State b = ITile.State.BLACK;
  ITile.State w = ITile.State.WHITE;

  @Test
  public void testAIPingsMove() {
    IReversi model = new HexReversi(4);
    ViewPub v1 = new ViewPubMock(model);
    ViewPub v2 = new ViewPubMock(model);
    IReversiPlayer p1 = new AIPlayer(model, new HighestScore(), b);
    IReversiPlayer p2 = new AIPlayer(model, new HighestScore(), w);
    MockController controller1 = new MockController(model, v1, p1);
    MockController controller2 = new MockController(model, v2, p2);
    controller1.turnBegins(b);
    controller2.turnBegins(b);
    Assert.assertEquals("turn begin notif BLACK\nmove received\n", controller1.getLog());
    Assert.assertEquals("turn begin notif BLACK\n", controller2.getLog());
  }

  @Test
  public void testAIPingsPass() {
    IReversi model = new HexReversi(2);
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
    IReversi model = new HexReversi(4);
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
    IReversi model = new HexReversi(4);
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
    IReversi model = new HexReversi(2);
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
    IReversi model = new HexReversi(4);
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
  public void testControllerContactsModel() {
    CubeCoord ideal = new CubeCoord(1, 1, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(ideal, 2);

    ReversiMock mock = new ReversiMock(scoreMap);
    ViewPub v1 = new GraphicView(mock, new ShapePainter(null));
    PlayerMock p1 = new PlayerMock();
    Controller c1 = new Controller(mock, v1, p1);
    c1.receiveMove(ideal);
    c1.receivePass();
    Assert.assertEquals("[Found the tile at coordinate: q: 1, r: 1, s: -2 " +
            "that exists in the mock., \n" + "Move received!\n, \nPass received!\n]",
            mock.getLog());
  }

  @Test
  public void testViewInitialSetup() {
    // tests on instantiation the right methods get called:
    // should add 1 subscriber, set the background, and refresh
    CubeCoord ideal = new CubeCoord(1, 1, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(ideal, 2);
    ReversiMock mock = new ReversiMock(scoreMap);
    ViewPubMock v1 = new ViewPubMock(mock);
    PlayerMock p1 = new PlayerMock();
    Controller c1 = new Controller(mock, v1, p1);
    Assert.assertEquals("added a subscriber\n" +
            "set background to BLACK\n", v1.getLog());
  }

  @Test
  public void testNotifyClickedPings() {
    // tests that when the view receives a click, this is communicated to the controller
    CubeCoord ideal = new CubeCoord(1, 1, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(ideal, 2);
    ReversiMock mock = new ReversiMock(scoreMap);
    ViewPubMock v1 = new ViewPubMock(mock);
    PlayerMock p1 = new PlayerMock();
    MockController c1 = new MockController(mock, v1, p1);
    v1.notifyClicked(ideal);
    Assert.assertEquals("move received\n", c1.getLog());
    Assert.assertEquals("added a subscriber\n" +
            "click occurred in the view at q: 1, r: 1, s: -2\n", v1.getLog());
  }

  @Test
  public void testNotifyPassedPings() {
    // tests that when the view receives a pass, this is communicated to the controller
    CubeCoord ideal = new CubeCoord(1, 1, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(ideal, 2);
    ReversiMock mock = new ReversiMock(scoreMap);
    ViewPubMock v1 = new ViewPubMock(mock);
    PlayerMock p1 = new PlayerMock();
    MockController c1 = new MockController(mock,v1,p1);
    v1.notifyPass();
    Assert.assertEquals("pass received\n", c1.getLog());
    Assert.assertEquals("added a subscriber\nthe view received a pass\n", v1.getLog());
  }

  @Test
  public void testReceiveMovePassRefreshViewEveryTime() {
    CubeCoord ideal = new CubeCoord(1, 1, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(ideal, 2);
    ReversiMock mock = new ReversiMock(scoreMap);
    ViewPubMock v1 = new ViewPubMock(mock);
    PlayerMock p1 = new PlayerMock();
    Controller c1 = new Controller(mock,v1,p1);
    v1.notifyClicked(ideal);
    v1.notifyPass();
    System.out.println(v1.getLog());
    Assert.assertEquals("added a subscriber\n" +
            "set background to BLACK\n" +
            "click occurred in the view at q: 1, r: 1, s: -2\n" +
            "view refreshed\n" +
            "the view received a pass\n" +
            "view refreshed\n", v1.getLog() );
  }

  @Test
  public void testPushMessageGameOver() {
    IReversi game = new HexReversi(4);
    ViewPubMock v1 = new ViewPubMock(game);
    ViewPubMock v2 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(w);
    IReversiPlayer p2 = new HumanPlayer(b);
    Controller c1 = new Controller(game,v1,p1);
    Controller c2 = new Controller(game,v2,p2);
    v1.notifyClicked(new CubeCoord(-2, 1, 1, false));
    v2.notifyClicked(new CubeCoord(2, -1, -1, false));
    v1.notifyClicked(new CubeCoord(1, -2, 1, false));
    v2.notifyClicked(new CubeCoord(-1, 2, -1, false));
    v1.notifyClicked(new CubeCoord(1, 1, -2, false));
    v2.notifyClicked(new CubeCoord(-1, -1, 2, false));
    v1.notifyPass();
    v2.notifyPass();
    Assert.assertTrue(v2.getLog().contains("pushed the message Game over! Red player wins!"));
  }

  @Test
  public void testPushMessageDuringGameplay() {
    //tests moves and passes, and game tie message is pushed after both passes
    IReversi game = new HexReversi(3);
    ViewPubMock v1 = new ViewPubMock(game);
    ViewPubMock v2 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(w);
    IReversiPlayer p2 = new HumanPlayer(b);
    Controller c1 = new Controller(game,v1,p1);
    Controller c2 = new Controller(game,v2,p2);

    v2.notifyClicked(new CubeCoord(2, -1, -1, false));
    v1.notifyClicked(new CubeCoord(-2, 1, 1, false));

    v1.notifyPass();
    v2.notifyPass();
    System.out.println(v1.getLog());
    System.out.println(v2.getLog());
    Assert.assertTrue(v1.getLog().contains("click occurred in the view at q: -2, r: 1, s: 1\n" +
            "view refreshed\n" +
            "view refreshed\n" +
            "the view received a pass\n" +
            "view refreshed\n" +
            "view refreshed\n"));
    Assert.assertTrue(v2.getLog().contains("added a subscriber\n" +
            "set background to BLACK\n" +
            "click occurred in the view at q: 2, r: -1, s: -1\n" +
            "pushed the message Hold your horses, its not your turn yet! RED\n" +
            "view refreshed\n" +
            "view refreshed\n" +
            "view refreshed\n" +
            "the view received a pass\n" +
            "pushed the message Game over! White player wins!\n" +
            "scores: red: 2 white: 5\n" +
            "view refreshed\n"));
  }

  @Test
  public void testPushNotYourTurn() {
    IReversi game = new HexReversi(3);
    ViewPubMock v1 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(b);
    Controller c1 = new Controller(game,v1,p1);
    v1.notifyClicked(new CubeCoord(-2, 1, 1, false));

    Assert.assertTrue(v1.getLog().contains("pushed the message " +
            "Hold your horses, its not your turn yet! RED"));
  }

  @Test
  public void testPushIllegalMove() {
    IReversi game = new HexReversi(3);
    ViewPubMock v1 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(w);
    Controller c1 = new Controller(game,v1,p1);
    v1.notifyClicked(new CubeCoord(0, 0, 0, false));
    Assert.assertTrue(v1.getLog().contains("That's not a valid move. Do better."));
  }

  @Test
  public void testPushMoveOnTopOfPieceSameColor() {
    IReversi game = new HexReversi(3);
    ViewPubMock v1 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(b);
    Controller c1 = new Controller(game,v1,p1);
    v1.notifyClicked(new CubeCoord(0, -1, 1, false));
    Assert.assertTrue(v1.getLog().contains("You cant put a piece on another piece!"));
  }

  @Test
  public void testPushMoveOnTopOfPieceDifferentColor() {
    IReversi game = new HexReversi(3);
    ViewPubMock v1 = new ViewPubMock(game);
    IReversiPlayer p1 = new HumanPlayer(b);
    Controller c1 = new Controller(game,v1,p1);
    v1.notifyClicked(new CubeCoord(1, -1, 0, false));
    Assert.assertTrue(v1.getLog().contains("You cant put a piece on another piece!"));
  }

}
