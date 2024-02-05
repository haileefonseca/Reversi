package strategy;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import mocks.ReversiMock;
import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;

/**
 * Class to test the functionality of the AvoidAroundCorners strategy.
 */
public class TestAvoidAroundCornersStrategy {
  IStrategy highestScore = new HighestScore();
  IStrategy avoidCorners = new AvoidAroundCorners();
  IStrategy avoidCornersWithBackup = new AvoidAroundCorners(highestScore);

  @Test
  public void testPicksOnlyTileNotAroundCorner() {
    CubeCoord best = new CubeCoord(-1, 2, -1, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(best, 3);
    scoreMap.put(new CubeCoord(3, -1, -2, false), 6);
    scoreMap.put(new CubeCoord(2, 0, -2, false), 6);
    scoreMap.put(new CubeCoord(2, 1, -3, false), 6);
    ReadOnlyReversi hm = new ReversiMock(scoreMap);
    CubeCoord picked = avoidCorners.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(best, picked);
  }

  @Test
  public void testGetsUpperLeftWhenAllAroundCorners() {
    CubeCoord best = new CubeCoord(3, -1, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(best, 6);
    scoreMap.put(new CubeCoord(2, 0, -2, false), 6);
    scoreMap.put(new CubeCoord(2, 1, -3, false), 6);
    scoreMap.put(new CubeCoord(1, 2, -3, false), 6);
    ReadOnlyReversi hm = new ReversiMock(scoreMap);
    // with backup
    CubeCoord picked = avoidCornersWithBackup.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(best, picked);
    // without backup
    Assert.assertThrows(IllegalStateException.class, () -> {
      CubeCoord result = avoidCorners.chooseMove(hm, ITile.State.BLACK);
    });
  }

  @Test
  public void testGetsUpperLeftWhenAllAroundCornersButNoTie() {
    CubeCoord best = new CubeCoord(2, 0, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(best, 7);
    scoreMap.put(new CubeCoord(3, -1, -2, false), 3);
    scoreMap.put(new CubeCoord(2, 1, -3, false), 2);
    scoreMap.put(new CubeCoord(1, 2, -3, false), 6);
    ReadOnlyReversi hm = new ReversiMock(scoreMap);
    // with backup
    CubeCoord picked = avoidCornersWithBackup.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(best, picked);
    // without backup
    Assert.assertThrows(IllegalStateException.class, () -> {
      CubeCoord result = avoidCorners.chooseMove(hm, ITile.State.BLACK);
    });
  }

  @Test
  public void testPicksHighestScore() {
    CubeCoord highScore = new CubeCoord(0, -3, 3, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(highScore, 6);
    scoreMap.put(new CubeCoord(-1, -2, 3, false), 3);
    scoreMap.put(new CubeCoord(0, 2, -2, false), 4);
    scoreMap.put(new CubeCoord(-2, 2, 0, false), 5);
    ReadOnlyReversi hm = new ReversiMock(scoreMap);
    CubeCoord picked = avoidCorners.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(highScore, picked);
    picked = avoidCornersWithBackup.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(highScore, picked);
  }


  @Test
  public void testActuallyInCornerAllowed() {
    CubeCoord best = new CubeCoord(-3, 0, 3, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(best, 3);
    scoreMap.put(new CubeCoord(3, -1, -2, false), 6);
    scoreMap.put(new CubeCoord(2, 0, -2, false), 6);
    scoreMap.put(new CubeCoord(2, 1, -3, false), 6);
    ReadOnlyReversi hm = new ReversiMock(scoreMap);
    CubeCoord picked = avoidCorners.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(best, picked);
  }

  @Test
  public void testCornerNeighborNotPickedEvenWithHighestScore() {
    CubeCoord highScore = new CubeCoord(1, -1, 0, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(highScore, 6);
    scoreMap.put(new CubeCoord(2, -2, 0, false), 3);
    scoreMap.put(new CubeCoord(1, -3, 2, false), 12);
    scoreMap.put(new CubeCoord(-1, 2, -1, false), 2);
    ReadOnlyReversi hm = new ReversiMock(scoreMap);
    CubeCoord picked = avoidCorners.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(highScore, picked);
    picked = avoidCornersWithBackup.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(highScore, picked);
  }
}
