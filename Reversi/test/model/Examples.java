package model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Includes simple tests for the HexReversi class, to demonstrate basic functionality.
 */
public class Examples {
  ITile.State b = ITile.State.BLACK;
  ITile.State w = ITile.State.WHITE;
  ITile.State n = ITile.State.NONE;

  @Test
  public void testCoordinateConversion() {
    CubeCoord c1 = new CubeCoord(-1, -1, 2, false);
    Assert.assertEquals(1, c1.getAsRow(3));
    Assert.assertEquals(1, c1.getAsCol(3));
    Assert.assertEquals(2, c1.getAsRow(4));
    Assert.assertEquals(2, c1.getAsCol(4));
    Assert.assertEquals(3, c1.getAsRow(5));
    Assert.assertEquals(3, c1.getAsCol(5));
    CubeCoord c2 = new CubeCoord(2, -1, -1, false);
    Assert.assertEquals(1, c2.getAsRow(3));
    Assert.assertEquals(4, c2.getAsCol(3));
    Assert.assertEquals(2, c2.getAsRow(4));
    Assert.assertEquals(5, c2.getAsCol(4));
    Assert.assertEquals(3, c2.getAsRow(5));
    Assert.assertEquals(6, c2.getAsCol(5));
  }

  @Test
  public void testInstantiation() {
    for (int i = -1; i < 2; i++) {
      int finalI = i;
      Assert.assertThrows(IllegalArgumentException.class, () -> {
        IReversi game = new HexReversi(finalI);
      });
    }
    IReversi game = new HexReversi(4);
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(0, 0, 0, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(-3, 0, 3, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(0, -3, 3, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(3, 0, -3, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(0, 3, -3, false)));
    Assert.assertEquals(n, game.getStateAt(new CubeCoord(1, 2, -3, false)));
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      game.getStateAt(new CubeCoord(-3, -3, 6, false));
    });
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      game.getStateAt(new CubeCoord(3, 3, -6, false));
    });
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      game.getStateAt(new CubeCoord(-2, -2, 4, false));
    });
  }

  @Test
  public void testInitialRingSetup() {
    IReversi game;
    for (int i = 2; i < 8; i++) {
      game = new HexReversi(i);
      Assert.assertEquals(b, game.getStateAt(new CubeCoord(0, -1, 1, false)));
      Assert.assertEquals(w, game.getStateAt(new CubeCoord(1, -1, 0, false)));
      Assert.assertEquals(b, game.getStateAt(new CubeCoord(1, 0, -1, false)));
      Assert.assertEquals(w, game.getStateAt(new CubeCoord(0, 1, -1, false)));
      Assert.assertEquals(b, game.getStateAt(new CubeCoord(-1, 1, 0, false)));
      Assert.assertEquals(w, game.getStateAt(new CubeCoord(-1, 0, 1, false)));
      Assert.assertEquals(n, game.getStateAt(new CubeCoord(0, 0, 0, false)));
    }
  }

  @Test
  public void testOneMove() {
    HexReversi game = new HexReversi(4);
    game.pass();
    CubeCoord c = new CubeCoord(-1, -1, 2, false);
    Assert.assertEquals(n, game.getStateAt(c));
    Assert.assertEquals(w, game.getStateAt(new CubeCoord(-1, 0, 1, false)));
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(-1, 1, 0, false)));

    game.move(c, b);
    Assert.assertEquals(b, game.getStateAt(new CubeCoord(-1, 0, 1, false)));
    Assert.assertEquals(b, game.getStateAt(c));
    Assert.assertThrows(IllegalStateException.class, () -> {
      game.move(c, w);
    });
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      game.move(c, b);
    });
  }

  @Test
  public void testVariousInvalidMoves() {
    IReversi game = new HexReversi(5);
    game.pass();
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      game.move(new CubeCoord(1, -2, 1, false), n);
    });
    List<Integer> rows = Arrays.asList(1, 6, -1, 4, -5);
    List<Integer> cols = Arrays.asList(1, 7, 4, -3, 2);
    for (int i = 0; i < rows.size(); i++) {
      int finalI = i;
      Assert.assertThrows(IllegalArgumentException.class, () -> {
        game.move(new CubeCoord(rows.get(finalI), cols.get(finalI), 5, true), b);
      });
    }
    List<Integer> srows = Arrays.asList(2, 7, 4, 2);
    List<Integer> scols = Arrays.asList(4, 1, 4, 6);
    for (int i = 0; i < srows.size(); i++) {
      int finalI = i;
      Assert.assertThrows(IllegalStateException.class, () -> {
        game.move(new CubeCoord(srows.get(finalI), scols.get(finalI), 5, true), b);
      });
    }
  }
}
