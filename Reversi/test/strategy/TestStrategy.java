package strategy;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import model.CubeCoord;
import mocks.ReversiMock;
import model.ITile;

/**
 * Class to test the functionality of AStrategy classes.
 */
public class TestStrategy {
  HighestScore highestScore = new HighestScore();

  @Test
  public void testPicksHighestScoreStartingBoard() {
    CubeCoord best = new CubeCoord(1, -2, 1, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(best, 2);
    scoreMap.put(new CubeCoord(-1, -1, 2, false), 2);
    scoreMap.put(new CubeCoord(2, -1, -1, false), 2);
    scoreMap.put(new CubeCoord(1, 1, -2, false), 2);
    scoreMap.put(new CubeCoord(-1, 2, -1, false), 2);
    scoreMap.put(new CubeCoord(-2, 1, 1, false), 2);
    ReversiMock hm = new ReversiMock(scoreMap);
    CubeCoord picked = highestScore.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(best, picked);
    String mockLog = hm.getLog();
    // assert that all coordinates were checked
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -1, r: -1, s: 2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 1, r: -2, s: 1"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 2, r: -1, s: -1"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 1, r: 1, s: -2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -1, r: 2, s: -1"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -2, r: 1, s: 1"));
    System.out.print(mockLog);
  }

  @Test
  public void testPicksHighestScore() {
    CubeCoord highScore = new CubeCoord(0, -3, 3, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(highScore, 6);
    scoreMap.put(new CubeCoord(-1, -2, 3, false), 3);
    scoreMap.put(new CubeCoord(0, 2, -2, false), 4);
    scoreMap.put(new CubeCoord(-2, 2, 0, false), 5);
    ReversiMock hm = new ReversiMock(scoreMap);
    CubeCoord picked = highestScore.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(highScore, picked);
    String mockLog = hm.getLog();
    // assert that all coordinates were checked
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 0, r: -3, s: 3"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -1, r: -2, s: 3"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 0, r: 2, s: -2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -2, r: 2, s: 0"));
  }

  @Test
  public void testPicksHighestScoreEvenWhenBottomRight() {
    CubeCoord highScore = new CubeCoord(0, 2, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(highScore, 4);
    scoreMap.put(new CubeCoord(1, -2, 1, false), 3);
    scoreMap.put(new CubeCoord(0, -2, 2, false), 2);
    ReversiMock hm = new ReversiMock(scoreMap);
    CubeCoord picked = highestScore.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(highScore, picked);
    String mockLog = hm.getLog();
    // assert that all coordinates were checked
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 0, r: 2, s: -2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 1, r: -2, s: 1"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 0, r: -2, s: 2"));
  }

  @Test
  public void testTiePicksUpper() {
    CubeCoord upperLeft = new CubeCoord(0, -3, 3, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(upperLeft, 2);
    // coords from left-most side
    scoreMap.put(new CubeCoord(-1, -2, 3, false), 2);
    scoreMap.put(new CubeCoord(-2, -1, 3, false), 2);
    scoreMap.put(new CubeCoord(-3, 0, 3, false), 2);
    ReversiMock hm = new ReversiMock(scoreMap);
    CubeCoord picked = highestScore.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(upperLeft, picked);
    String mockLog = hm.getLog();
    // assert that all coordinates were checked
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 0, r: -3, s: 3"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -1, r: -2, s: 3"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -2, r: -1, s: 3"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -3, r: 0, s: 3"));
  }

  @Test
  public void testTiePicksLeft() {
    CubeCoord moreLeft = new CubeCoord(-3, 0, 3, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(moreLeft, 2);
    // coords from radius of the board, all in same row
    scoreMap.put(new CubeCoord(-2, 0, 2, false), 2);
    scoreMap.put(new CubeCoord(-1, 0, 1, false), 2);
    scoreMap.put(new CubeCoord(0, 0, 0, false), 2);
    ReversiMock hm = new ReversiMock(scoreMap);
    CubeCoord picked = highestScore.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(moreLeft, picked);
    String mockLog = hm.getLog();
    // assert that all coordinates were checked
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -3, r: 0, s: 3"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -2, r: 0, s: 2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -1, r: 0, s: 1"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 0, r: 0, s: 0"));
  }

  @Test
  public void testAmbiguousTiePicksUL() {
    CubeCoord ideal = new CubeCoord(1, -3, 2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(ideal, 2);
    // coords where some are more left, some are more up
    scoreMap.put(new CubeCoord(-1, -1, 2, false), 2);
    scoreMap.put(new CubeCoord(1, -2, 1, false), 2);
    scoreMap.put(new CubeCoord(0, -2, 2, false), 2);
    ReversiMock hm = new ReversiMock(scoreMap);
    CubeCoord picked = highestScore.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(ideal, picked);
    String mockLog = hm.getLog();
    // assert that all coordinates were checked
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 1, r: -3, s: 2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -1, r: -1, s: 2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 1, r: -2, s: 1"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 0, r: -2, s: 2"));
  }

  @Test
  public void testTieInBottomRightPicksUL() {
    CubeCoord ideal = new CubeCoord(1, 1, -2, false);
    Map<CubeCoord, Integer> scoreMap = new HashMap<>();
    scoreMap.put(ideal, 2);
    // coords where some are more left, some are more up, but all in the bottom right quadrant
    scoreMap.put(new CubeCoord(0, 2, -2, false), 2);
    scoreMap.put(new CubeCoord(-1, 2, -1, false), 2);
    ReversiMock hm = new ReversiMock(scoreMap);
    CubeCoord picked = highestScore.chooseMove(hm, ITile.State.BLACK);
    Assert.assertEquals(ideal, picked);
    String mockLog = hm.getLog();
    // assert that all coordinates were checked
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 1, r: 1, s: -2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: 0, r: 2, s: -2"));
    Assert.assertTrue(mockLog.contains("Found the tile at coordinate: q: -1, r: 2, s: -1"));
  }
}
