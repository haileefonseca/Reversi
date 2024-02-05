package model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests more advanced HexReversi methods and actions for functionality and edge cases.
 */
public class TestModel {

  ITile.State b = ITile.State.BLACK;
  ITile.State w = ITile.State.WHITE;
  ITile.State n = ITile.State.NONE;

  @Test
  public void testMultipleMoves() {
    IReversi game = new HexReversi(5);
    game.pass();
    game.move(new CubeCoord(-1, -1, 2, false), b);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(-1, -1, 2, false)));
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(-1, 0, 1, false)));
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(-1, 1, 0, false)));
    game.move(new CubeCoord(-2, -1, 3, false), w);

    Assert.assertEquals(w, game.getStateAt(new CubeCoord(-2, -1, 3, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(-1, -1, 2, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(0, -1, 1, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(1, -1, 0, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(2, -1, -1, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(-3, -1, 4, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(-2, -2, 4, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(-2, 0, 2, false)));

  }

  @Test
  public void testDoubleCapture() {
    IReversi game = new HexReversi(5);
    game.move(new CubeCoord(2, -1, -1, false), w);
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(1, 0, -1, false)));

    game.move(new CubeCoord(1, 1, -2, false), b);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(0, 1, -1, false)));
    Assert.assertNotSame(game.getStateAt(new CubeCoord(0, 1, -1, false)), w);
    game.move(new CubeCoord(-1, 2, -1, false), w);
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(-1, 1, 0, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(0, 1, -1, false)));
  }

  @Test
  public void testReturnResultsWorksBasic() {
    IReversi game = new HexReversi(4);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(0, -1, 1, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(-1, 0, 1, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(-1, -1, 2, false)));
    List<ITile> res1 = game.returnResultsOfMove(new CubeCoord(-1, -1, 2, false), b);
    Assert.assertEquals(2, res1.size());
    Assert.assertEquals(w, res1.get(0).getState());
    Assert.assertEquals(n, res1.get(1).getState());
  }

  @Test
  public void testReturnResultsWorksDoubleCapture() {
    IReversi game = new HexReversi(5);
    List<ITile> res1 = game.returnResultsOfMove(new CubeCoord(2, -1, -1, false), w);
    Assert.assertEquals(2, res1.size());
    Assert.assertEquals(b, res1.get(0).getState());
    Assert.assertEquals(n, res1.get(1).getState());
    game.move(new CubeCoord(2, -1, -1, false), w);

    List<ITile> res2 = game.returnResultsOfMove(new CubeCoord(1, 1, -2, false), b);
    Assert.assertEquals(2, res2.size());
    Assert.assertEquals(w, res2.get(0).getState());
    Assert.assertEquals(n, res2.get(1).getState());
    game.move(new CubeCoord(1, 1, -2, false), b);

    List<ITile> res3 = game.returnResultsOfMove(new CubeCoord(-1, 2, -1, false), w);
    Assert.assertEquals(3, res3.size());
    Assert.assertEquals(b, res3.get(0).getState());
    Assert.assertEquals(b, res3.get(1).getState());
    Assert.assertEquals(n, res3.get(2).getState());
    game.move(new CubeCoord(-1, 2, -1, false), w);
  }

  @Test
  public void testReturnResultsCantModifyGame() {
    IReversi game = new HexReversi(5);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(1, 0, -1, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(2, -1, -1, false)));
    Assert.assertEquals(3, game.calcScore(b));
    Assert.assertEquals(3, game.calcScore(w));
    List<ITile> res1 = game.returnResultsOfMove(new CubeCoord(2, -1, -1, false), w);

    res1.get(0).setState(w);
    res1.get(1).setState(w);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(1, 0, -1, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(2, -1, -1, false)));
    Assert.assertEquals(3, game.calcScore(b));
    Assert.assertEquals(3, game.calcScore(w));
  }

  @Test
  public void testCalcScoreWorks() {
    IReversi game = new HexReversi(5);
    Assert.assertEquals(3, game.calcScore(b));
    Assert.assertEquals(3, game.calcScore(w));
    game.move(new CubeCoord(2, -1, -1, false), w);
    Assert.assertEquals(2, game.calcScore(b));
    Assert.assertEquals(5, game.calcScore(w));
    game.move(new CubeCoord(1, 1, -2, false), b);
    Assert.assertEquals(4, game.calcScore(b));
    Assert.assertEquals(4, game.calcScore(w));
    game.move(new CubeCoord(-1, 2, -1, false), w);
    Assert.assertEquals(2, game.calcScore(b));
    Assert.assertEquals(7, game.calcScore(w));
  }

  @Test
  public void testGameOver() {
    IReversi game = new HexReversi(3);
    Assert.assertFalse(game.gameOver());
    game.move(new CubeCoord(1, -2, 1, false), w);
    Assert.assertFalse(game.gameOver());
    game.pass();
    game.move(new CubeCoord(2, -1, -1, false), w);
    Assert.assertFalse(game.gameOver());
    game.move(new CubeCoord(-1, -1, 2, false), b);
    Assert.assertFalse(game.gameOver());
    game.move(new CubeCoord(-2, 1, 1, false), w);
    Assert.assertFalse(game.gameOver());
    game.move(new CubeCoord(-1, 2, -1, false), b);
    Assert.assertFalse(game.gameOver());
    game.pass();
    game.move(new CubeCoord(1, 1, -2, false), b);
    Assert.assertTrue(game.gameOver());
    game.pass();
    Assert.assertThrows(IllegalStateException.class, () -> {
      game.move(new CubeCoord(2, 1, -3, false), b);
    });
  }

  @Test
  public void testSize2NotWinnable() {
    IReversi game = new HexReversi(2);
    Assert.assertTrue(game.gameOver());
  }

  @Test
  public void testMoveNotYourTurn() {
    IReversi game = new HexReversi(4);
    game.pass();
    game.move(new CubeCoord(-1, -1, 2, false), b);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      game.move(new CubeCoord(1, 1, -2, false), b);
    });
    game.pass();
    game.move(new CubeCoord(1, 1, -2, false), b);
    game.move(new CubeCoord(1, 2, -3, false), w);
  }

  @Test
  public void testGetBoardSize() {
    IReversi game = new HexReversi(4);
    ReadOnlyReversi roGame = new HexReversi(100);
    Assert.assertEquals(game.getBoardSize(), 4);
    Assert.assertEquals(roGame.getBoardSize(), 100);
  }

  @Test
  public void testDuplicate() {
    ReadOnlyReversi roGame = new HexReversi(2);
    ITile[][] dupe = roGame.duplicate();
    List<ITile.State> row1 = List.of(ITile.State.BLACK, ITile.State.WHITE);
    List<ITile.State> row2 = List.of(ITile.State.WHITE, ITile.State.NONE, ITile.State.BLACK);
    List<ITile.State> row3 = List.of(ITile.State.BLACK, ITile.State.WHITE);
    List<List<ITile.State>> game = Arrays.asList(row1, row2, row3);
    Assert.assertEquals(dupe[0][1].getState(), game.get(0).get(0));
    Assert.assertEquals(dupe[0][2].getState(), game.get(0).get(1));
    Assert.assertEquals(dupe[1][0].getState(), game.get(1).get(0));
    Assert.assertEquals(dupe[1][1].getState(), game.get(1).get(1));
    Assert.assertEquals(dupe[1][2].getState(), game.get(1).get(2));
    Assert.assertEquals(dupe[2][0].getState(), game.get(2).get(0));
    Assert.assertEquals(dupe[2][1].getState(), game.get(2).get(1));
  }

  @Test
  public void testLegalStartingMoves() {
    IReversi game = new HexReversi(3);
    CubeCoord m1 = new CubeCoord(-2, 1, 1, false);
    CubeCoord m2 = new CubeCoord(-1, 2, -1, false);
    CubeCoord m3 = new CubeCoord(2, -1, -1, false);
    CubeCoord m4 = new CubeCoord(1, 1, -2, false);
    CubeCoord m5 = new CubeCoord(1, -2, 1, false);
    CubeCoord m6 = new CubeCoord(-1, -1, 2, false);
    Assert.assertTrue(game.isLegalMove(m1, w));
    Assert.assertTrue(game.isLegalMove(m2, w));
    Assert.assertTrue(game.isLegalMove(m3, w));
    Assert.assertTrue(game.isLegalMove(m4, w));
    Assert.assertTrue(game.isLegalMove(m5, w));
    Assert.assertTrue(game.isLegalMove(m6, w));

    CubeCoord bm1 = new CubeCoord(2, -1, -1, false);
    CubeCoord bm2 = new CubeCoord(1, -2, 1, false);
    CubeCoord bm3 = new CubeCoord(-1, 2, -1, false);
    CubeCoord bm4 = new CubeCoord(1, 1, -2, false);
    CubeCoord bm5 = new CubeCoord(-1, -1, 2, false);

    CubeCoord bm6 = new CubeCoord(-2, 1, 1, false);
    Assert.assertTrue(game.isLegalMove(bm1, b));
    Assert.assertTrue(game.isLegalMove(bm2, b));
    Assert.assertTrue(game.isLegalMove(bm3, b));
    Assert.assertTrue(game.isLegalMove(bm4, b));
    Assert.assertTrue(game.isLegalMove(bm5, b));
    Assert.assertTrue(game.isLegalMove(bm6, b));
  }

  @Test
  public void testIllegalMoves() {
    IReversi game = new HexReversi(3);
    CubeCoord m1 = new CubeCoord(0, -2, 2, false);
    CubeCoord m2 = new CubeCoord(2, -2, 0, false);
    CubeCoord m3 = new CubeCoord(0, 0, 0, false);
    CubeCoord m4 = new CubeCoord(2, 0, -2, false);
    CubeCoord m5 = new CubeCoord(-2, 0, 2, false);
    CubeCoord m6 = new CubeCoord(0, 2, -2, false);
    Assert.assertFalse(game.isLegalMove(m1, w));
    Assert.assertFalse(game.isLegalMove(m1, b));
    Assert.assertFalse(game.isLegalMove(m2, w));
    Assert.assertFalse(game.isLegalMove(m2, b));
    Assert.assertFalse(game.isLegalMove(m3, w));
    Assert.assertFalse(game.isLegalMove(m3, b));
    Assert.assertFalse(game.isLegalMove(m4, w));
    Assert.assertFalse(game.isLegalMove(m4, b));
    Assert.assertFalse(game.isLegalMove(m5, w));
    Assert.assertFalse(game.isLegalMove(m5, b));
    Assert.assertFalse(game.isLegalMove(m6, w));
    Assert.assertFalse(game.isLegalMove(m6, b));
  }

  @Test
  public void testHasLegalMovesFullGamePlay() {
    IReversi game = new HexReversi(3);
    Assert.assertTrue(game.hasMoves(b));
    Assert.assertTrue(game.hasMoves(w));
    game.move(new CubeCoord(-2, 1, 1, false), w);
    game.move(new CubeCoord(2, -1, -1, false), b);
    game.move(new CubeCoord(1, -2, 1, false), w);
    Assert.assertTrue(game.hasMoves(b));
    Assert.assertTrue(game.hasMoves(w));
    game.move(new CubeCoord(-1, 2, -1, false), b);
    game.move(new CubeCoord(1, 1, -2, false), w);
    game.move(new CubeCoord(-1, -1, 2, false), b);
    Assert.assertFalse(game.hasMoves(w));
    Assert.assertFalse(game.hasMoves(b));
    Assert.assertTrue(game.gameOver());
    Assert.assertEquals(game.calcScore(b), 7);
    Assert.assertEquals(game.calcScore(w), 5);
  }

  @Test
  public void testGetStateSetState() {
    ITile state1 = new HexTile(ITile.State.BLACK);
    ITile state2 = new HexTile( ITile.State.WHITE);
    ITile state3 = new HexTile( ITile.State.NONE);
    Assert.assertEquals(state1.getState(), ITile.State.BLACK);
    Assert.assertEquals(state2.getState(), ITile.State.WHITE);
    Assert.assertEquals(state3.getState(), ITile.State.NONE);
    state1.setState(ITile.State.WHITE);
    state2.setState(ITile.State.NONE);
    state3.setState(ITile.State.BLACK);
    Assert.assertEquals(state3.getState(), ITile.State.BLACK);
    Assert.assertEquals(state1.getState(), ITile.State.WHITE);
    Assert.assertEquals(state2.getState(), ITile.State.NONE);
  }
}
