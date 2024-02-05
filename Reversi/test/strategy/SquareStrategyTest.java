package strategy;

import org.junit.Assert;
import org.junit.Test;

import model.CubeCoord;
import model.IReversi;
import model.ITile;
import model.SquareReversi;

/**
 * Tests functionality of strategies in a square game of Reversi.
 */
public class SquareStrategyTest {

  @Test
  public void testPicksUpperLeft() {
    IReversi game = new SquareReversi(4);
    IStrategy ai = new HighestScore();
    CubeCoord picked = ai.chooseMove(game, ITile.State.WHITE);
    Assert.assertEquals(new CubeCoord(1, 0, -1, false), picked);
  }

  @Test
  public void testPicksHighScore() {
    SquareReversi game = new SquareReversi(8);
    game.move(new CubeCoord(4, 5, -1, false), ITile.State.WHITE);
    game.move(new CubeCoord(5, 5, -2, false), ITile.State.BLACK);
    game.move(new CubeCoord(6, 5, -2, false), ITile.State.WHITE);
    game.move(new CubeCoord(4,2,-3, false), ITile.State.BLACK);
    IStrategy ai = new HighestScore();
    CubeCoord picked = ai.chooseMove(game, ITile.State.WHITE);
    Assert.assertEquals(new CubeCoord(4, 1, -5, false), picked);
  }

  @Test
  public void testAvoidAroundCorners() {
    SquareReversi game = new SquareReversi(8);
    IStrategy ai = new AvoidAroundCorners(new HighestScore());
    CubeCoord picked = ai.chooseMove(game, ITile.State.WHITE);
    Assert.assertEquals(new CubeCoord(3, 2, -5, false), picked);
  }
}
