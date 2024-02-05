package model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


/**
 * Tests functionality of the square model.
 */
public class TestSquareModel {
  ITile.State b = ITile.State.BLACK;
  ITile.State w = ITile.State.WHITE;
  ITile.State n = ITile.State.NONE;


  @Test
  public void testMultipleMoves() {
    IReversi game = new SquareReversi(8);
    game.move(new CubeCoord(4, 5, 1, false), w);
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(4, 5, 1, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(4, 4, 1, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(4, 3, 1, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(3, 4, 1, false)));
    game.move(new CubeCoord(5, 5, 1, false), b);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(5, 5, 1, false)));
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(4, 4, 1, false)));
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(3, 3, 1, false)));
  }

  @Test
  public void testDoubleCapture() {
    IReversi game = new SquareReversi(4);
    game.move(new CubeCoord(2, 3, 1, false), w);
    game.move(new CubeCoord(3, 1, -1, false), b);
    game.move(new CubeCoord(2, 0, 1, false), w);
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(1, 2, 3, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(2, 2, 3, false)));
    game.move(new CubeCoord(1, 3, -1, false), b);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(1, 2, 3, false)));
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(2, 2, 3, false)));
  }

  @Test
  public void testReturnResultsWorksBasic() {
    IReversi game = new SquareReversi(4);
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(0, 1, 1, false)));
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(1, 1, 1, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(2, 1, 2, false)));
    List<ITile> res1 = game.returnResultsOfMove(new CubeCoord(0, 1, 2, false), w);
    Assert.assertEquals(2, res1.size());
    Assert.assertEquals(b, res1.get(0).getState());
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
    IReversi game = new SquareReversi(4);
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(0, 1, 1, false)));
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(1, 1, 1, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(2, 1, 2, false)));
    Assert.assertEquals(2, game.calcScore(b));
    Assert.assertEquals(2, game.calcScore(w));
    List<ITile> res1 = game.returnResultsOfMove(new CubeCoord(0, 1, 2, false), w);
    res1.get(0).setState(b);
    res1.get(1).setState(n);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(1, 1, -1, false)));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(2, 1, -1, false)));
    Assert.assertEquals(2, game.calcScore(b));
    Assert.assertEquals(2, game.calcScore(w));
  }

  @Test
  public void testCalcScoreWorks() {
    IReversi game = new SquareReversi(4);
    Assert.assertEquals(2, game.calcScore(b));
    Assert.assertEquals(2, game.calcScore(w));
    game.move(new CubeCoord(0, 1, -1, false), w);
    Assert.assertEquals(1, game.calcScore(b));
    Assert.assertEquals(4, game.calcScore(w));
  }

  @Test
  public void testGameOver() {
    IReversi game = new SquareReversi(4);
    game.move(new CubeCoord(2, 3, 0, false), w);
    game.pass();
    Assert.assertThrows(IllegalStateException.class, () -> {
      game.pass();
    });
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      game.move(new CubeCoord(3, 1, 0, false), b);
    });
  }

  @Test
  public void testSize2NotWinnable() {
    IReversi game = new SquareReversi(2);
    Assert.assertTrue(game.gameOver());
  }

  @Test
  public void testMoveNotYourTurn() {
    IReversi game = new SquareReversi(4);
    game.pass();
    game.move(new CubeCoord(1, 3, 2, false), b);
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      game.move(new CubeCoord(3, 1, -2, false), b);
    });
  }

  @Test
  public void testGetBoardSize() {
    IReversi game = new SquareReversi(4);
    ReadOnlyReversi roGame = new SquareReversi(100);
    Assert.assertEquals(game.getBoardSize(), 4);
    Assert.assertEquals(roGame.getBoardSize(), 100);
  }

  @Test
  public void testDuplicate() {
    ReadOnlyReversi roGame = new SquareReversi(4);
    ITile[][] dupe = roGame.duplicate();
    List<ITile.State> none = List.of(ITile.State.NONE, ITile.State.NONE, ITile.State.NONE,
            ITile.State.NONE);
    List<ITile.State> row1 = List.of(ITile.State.NONE, ITile.State.BLACK, ITile.State.WHITE,
            ITile.State.NONE);
    List<ITile.State> row2 = List.of(ITile.State.NONE, ITile.State.WHITE, ITile.State.BLACK,
            ITile.State.NONE);
    List<List<ITile.State>> game = Arrays.asList(none, row1, row2, none);
    for (int row = 0; row < roGame.getBoardSize(); row ++) {
      for (int col = 0; col < roGame.getBoardSize(); col++) {
        Assert.assertEquals(dupe[row][col].getState(), game.get(row).get(col));
      }
    }
  }

  @Test
  public void testLegalStartingMoves() {
    IReversi game = new SquareReversi(4);
    CubeCoord m1 = new CubeCoord(0, 1, 1, false);
    CubeCoord m2 = new CubeCoord(1, 0, -1, false);
    CubeCoord m3 = new CubeCoord(3, 2, -1, false);
    CubeCoord m4 = new CubeCoord(2, 3, -2, false);
    Assert.assertTrue(game.isLegalMove(m1, w));
    Assert.assertTrue(game.isLegalMove(m2, w));
    Assert.assertTrue(game.isLegalMove(m3, w));
    Assert.assertTrue(game.isLegalMove(m4, w));

    CubeCoord bm1 = new CubeCoord(2, 0, -1, false);
    CubeCoord bm2 = new CubeCoord(3, 1, 1, false);
    CubeCoord bm3 = new CubeCoord(0, 2, -1, false);
    CubeCoord bm4 = new CubeCoord(1, 3, -2, false);
    Assert.assertTrue(game.isLegalMove(bm1, b));
    Assert.assertTrue(game.isLegalMove(bm2, b));
    Assert.assertTrue(game.isLegalMove(bm3, b));
    Assert.assertTrue(game.isLegalMove(bm4, b));
  }

  @Test
  public void testIllegalMoves() {
    IReversi game = new SquareReversi(4);
    CubeCoord m1 = new CubeCoord(0, 0, 0, false);
    CubeCoord m2 = new CubeCoord(3, 0, 0, false);
    CubeCoord m3 = new CubeCoord(0, 3, 0, false);
    CubeCoord m4 = new CubeCoord(3, 3, 0, false);
    Assert.assertFalse(game.isLegalMove(m1, w));
    Assert.assertFalse(game.isLegalMove(m1, b));
    Assert.assertFalse(game.isLegalMove(m2, w));
    Assert.assertFalse(game.isLegalMove(m2, b));
    Assert.assertFalse(game.isLegalMove(m3, w));
    Assert.assertFalse(game.isLegalMove(m3, b));
    Assert.assertFalse(game.isLegalMove(m4, w));
    Assert.assertFalse(game.isLegalMove(m4, b));
  }


  //    System.out.print(new SquareTextView(game).toString());
  @Test
  public void testHasLegalMovesFullGamePlay() {
    IReversi game = new SquareReversi(4);
    Assert.assertTrue(game.hasMoves(b));
    Assert.assertTrue(game.hasMoves(w));
    game.move(new CubeCoord(2, 3, 1, false), w);
    game.move(new CubeCoord(3, 1, -1, false), b);
    game.move(new CubeCoord(2, 0, 1, false), w);
    Assert.assertTrue(game.hasMoves(b));
    Assert.assertTrue(game.hasMoves(w));
    game.move(new CubeCoord(1, 3, -1, false), b);
    game.move(new CubeCoord(0, 1, -1, false), w);
    game.move(new CubeCoord(1, 0, -1, false), b);
    game.move(new CubeCoord(0, 0, -1, false), w);
    game.move(new CubeCoord(3, 3, 0, false), b);
    game.move(new CubeCoord(0, 3, -1, false), w);
    game.move(new CubeCoord(0, 2, -1, false), b);
    Assert.assertFalse(game.hasMoves(w));
    game.pass();
    game.move(new CubeCoord(3, 0, -1, false), b);
    game.move(new CubeCoord(3, 2, -1, false), w);
    Assert.assertFalse(game.hasMoves(w));
    Assert.assertFalse(game.hasMoves(b));
    Assert.assertTrue(game.gameOver());
    Assert.assertEquals(9, game.calcScore(b));
    Assert.assertEquals(7, game.calcScore(w));
  }

  @Test
  public void testIllegalStartingSizes() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new SquareReversi(1));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new SquareReversi(5));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new SquareReversi(11));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new SquareReversi(-2));
  }
}
