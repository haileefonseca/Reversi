package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;

/**
 * Abstract class that generalizes the functionality of a Strategy.
 */
public abstract class AStrategy implements IStrategy {

  @Override
  public abstract CubeCoord chooseMove(ReadOnlyReversi model, ITile.State color)
          throws IllegalStateException;

  /**
   * Returns a list of all tile locations that don't already have a piece on them.
   * @param model the game being assessed.
   * @return The list of empty tile locations.
   */
  private List<CubeCoord> getPossibleMoves(ReadOnlyReversi model) {
    int size = model.getBoardSize();
    List<CubeCoord> validMoves = new ArrayList<>();
    for (int r = -size + 1; r < size; r++) {
      for (int q = -size + 1; q < size; q++) {
        int s = 0 - r - q;
        if (Math.abs(s) <= size - 1) {
          CubeCoord here = new CubeCoord(q, r, s, false);
          if (model.getStateAt(here) == ITile.State.NONE) {
            validMoves.add(here);
          }
        }
      }
    }
    return validMoves;
  }

  /**
   * Iterates through all open spaces, and finds/returns all viable moves.
   *
   * @param model model to find moves from
   * @param color color that will be executing a move
   * @return a map from possible moves, as CubeCoords,
   *         to the score that executing this move would gain, as an Integer.
   */
  protected Map<CubeCoord, Integer> getValidMoves(ReadOnlyReversi model, ITile.State color) {
    List<CubeCoord> moves = model.getEmptyTiles();
    Map<CubeCoord, Integer> goodMoves = new HashMap<>();
    for (CubeCoord c : moves) {
      try {
        int score = model.returnResultsOfMove(c, color).size();
        if (score > 1) {
          goodMoves.put(c, score);
        }
      } catch (Exception e) {
        // do nothing - not a legitimate move
      }
    }
    return goodMoves;
  }

  /**
   * Returns the upper-leftmost of two cubic coordinates. Averages the s and -r coordinates to
   * calculate an "upper-left score", and takes the coordinate with a lower r value to break a tie.
   *
   * @param move1 the first coordinate
   * @param move2 the other coordinate, if it exists
   * @return the most "upper-left" coordinate
   */
  protected CubeCoord getUpperLeft(CubeCoord move1, CubeCoord move2) {
    if (move2 == null) {
      return move1;
    }
    double move1ULScore = (move1.getS() - move1.getR()) / 2.0;
    double move2ULScore = (move2.getS() - move2.getR()) / 2.0;
    if (move1ULScore > move2ULScore) {
      return move1;
    } else if (move1ULScore < move2ULScore) {
      return move2;
    } else if (move1.getR() < move2.getR()) {
      return move1;
    }
    return move2;
  }

  // Returns the highest-scoring move in the map of moves.
  protected CubeCoord getHighestScoring(Map<CubeCoord, Integer> moves) {
    CubeCoord bestMove = null;
    int maxPoints = 0;
    for (CubeCoord c : moves.keySet()) {
      int score = moves.get(c);
      if (score > maxPoints) {
        maxPoints = score;
        bestMove = c;
      } else if (score == maxPoints) {
        bestMove = this.getUpperLeft(c, bestMove);
      }
    }
    if (maxPoints == 0) {
      throw new IllegalStateException("No possible moves");
    }
    return bestMove;
  }
}