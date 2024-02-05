package view;

import org.junit.Assert;
import org.junit.Test;

import model.CubeCoord;
import model.HexReversi;
import model.IReversi;
import model.ITile;
import model.SquareReversi;

/**
 * A class to test the features of the TextView class.
 */
public class TestTextualView {

  ITile.State b = ITile.State.BLACK;
  ITile.State w = ITile.State.WHITE;

  @Test
  public void testStartingBoardSizeSix() {
    IReversi game = new HexReversi(6);
    TextView view = new HexTextualView(game);
    Assert.assertEquals("     _ _ _ _ _ _\n" +
                    "    _ _ _ _ _ _ _\n" +
                    "   _ _ _ _ _ _ _ _\n" +
                    "  _ _ _ _ _ _ _ _ _\n" +
                    " _ _ _ _ X O _ _ _ _\n" +
                    "_ _ _ _ O _ X _ _ _ _\n" +
                    " _ _ _ _ X O _ _ _ _\n" +
                    "  _ _ _ _ _ _ _ _ _\n" +
                    "   _ _ _ _ _ _ _ _\n" +
                    "    _ _ _ _ _ _ _\n" +
                    "     _ _ _ _ _ _",
            view.toString());
  }

  @Test
  public void testStartingBoardSizeFive() {
    IReversi game = new HexReversi(5);
    TextView view = new HexTextualView(game);
    Assert.assertEquals("    _ _ _ _ _\n" +
                    "   _ _ _ _ _ _\n" +
                    "  _ _ _ _ _ _ _\n" +
                    " _ _ _ X O _ _ _\n" +
                    "_ _ _ O _ X _ _ _\n" +
                    " _ _ _ X O _ _ _\n" +
                    "  _ _ _ _ _ _ _\n" +
                    "   _ _ _ _ _ _\n" +
                    "    _ _ _ _ _",
            view.toString());
  }

  @Test
  public void testStartingBoardSizeFour() {
    IReversi game = new HexReversi(4);
    TextView view = new HexTextualView(game);
    Assert.assertEquals("   _ _ _ _\n" +
                    "  _ _ _ _ _\n" +
                    " _ _ X O _ _\n" +
                    "_ _ O _ X _ _\n" +
                    " _ _ X O _ _\n" +
                    "  _ _ _ _ _\n" +
                    "   _ _ _ _",
            view.toString());
  }

  @Test
  public void testStartingBoardSizeThree() {
    IReversi game = new HexReversi(3);
    TextView view = new HexTextualView(game);
    Assert.assertEquals("  _ _ _\n" +
                    " _ X O _\n" +
                    "_ O _ X _\n" +
                    " _ X O _\n" +
                    "  _ _ _",
            view.toString());
  }

  @Test
  public void testStartingBoardSizeTwo() {
    IReversi game = new HexReversi(2);
    TextView view = new HexTextualView(game);
    Assert.assertEquals(" X O\n" +
                    "O _ X\n" +
                    " X O",
            view.toString());
  }

  @Test
  public void testStartingBoardSizeOneThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new HexTextualView(new HexReversi(1));
    });
  }

  @Test
  public void testStartingBoardNegativeSizeThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new HexTextualView(new HexReversi(-9000000));
    });
  }

  @Test
  public void testBoardOneMove() {
    IReversi game = new HexReversi(4);
    TextView model = new HexTextualView(game);
    Assert.assertEquals("   _ _ _ _\n" +
            "  _ _ _ _ _\n" +
            " _ _ X O _ _\n" +
            "_ _ O _ X _ _\n" +
            " _ _ X O _ _\n" +
            "  _ _ _ _ _\n" +
            "   _ _ _ _", model.toString());
    CubeCoord c = new CubeCoord(-1, -1, 2, false);
    game.pass();
    game.move(c, b);
    Assert.assertEquals("   _ _ _ _\n" +
            "  _ _ _ _ _\n" +
            " _ X X O _ _\n" +
            "_ _ X _ X _ _\n" +
            " _ _ X O _ _\n" +
            "  _ _ _ _ _\n" +
            "   _ _ _ _", model.toString());
  }

  @Test
  public void testSquareBoardSize4() {
    SquareReversi game = new SquareReversi(4);
    TextView view = new SquareTextView(game);
    Assert.assertEquals("_ _ _ _\n" +
            "_ X O _\n" +
            "_ O X _\n" +
            "_ _ _ _", view.toString());
  }

  @Test
  public void testSquareBoardSize8() {
    SquareReversi game = new SquareReversi(8);
    TextView view = new SquareTextView(game);
    Assert.assertEquals("_ _ _ _ _ _ _ _\n" +
            "_ _ _ _ _ _ _ _\n" +
            "_ _ _ _ _ _ _ _\n" +
            "_ _ _ X O _ _ _\n" +
            "_ _ _ O X _ _ _\n" +
            "_ _ _ _ _ _ _ _\n" +
            "_ _ _ _ _ _ _ _\n" +
            "_ _ _ _ _ _ _ _", view.toString());
  }

  @Test
  public void testSquareBoardOneMove() {
    SquareReversi game = new SquareReversi(4);
    TextView view = new SquareTextView(game);
    game.move(new CubeCoord(2, 3, 1, false), w);
    Assert.assertEquals("_ _ _ _\n" +
            "_ X O _\n" +
            "_ O O _\n" +
            "_ _ O _", view.toString());
  }



}
