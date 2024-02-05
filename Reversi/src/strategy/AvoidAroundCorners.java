package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;

/**
 * Represents a strategy that avoids moves that neighbor a corner tile. Of the remaining moves,
 * the move with the highest score is chosen. If there is no such move, it reverts to a backup
 * strategy, or throws an exception if there is none.
 */
public class AvoidAroundCorners extends AStrategy {

  // The backup strategy, if there is one.
  private final IStrategy backup;

  /**
   * Constructs an AvoidAroundCorners with no backup strategy.
   */
  public AvoidAroundCorners() {
    this.backup = null;
  }

  /**
   * Constructs an AvoidAroundCorners with the given backup strategy.
   *
   * @param backup The given backup strategy.
   */
  public AvoidAroundCorners(IStrategy backup) {
    this.backup = backup;
  }

  @Override
  public CubeCoord chooseMove(ReadOnlyReversi model, ITile.State color)
          throws IllegalStateException {
    Map<CubeCoord, Integer> moves = super.getValidMoves(model, color);
    Map<CubeCoord, Integer> allowableMoves = new HashMap<>();
    List<CubeCoord> corners = this.getCorners(model);
    List<CubeCoord> badSpots = this.getNeighbors(corners);
    // loops over all the possible moves
    for (CubeCoord move : moves.keySet()) {
      boolean isBad = false;
      // if the possible moves aren't near a corner, add them to the allowable moves
      for (CubeCoord bad : badSpots) {
        if (move.equals(bad)) {
          isBad = true;
        }
      }
      if (!isBad) {
        allowableMoves.put(move, moves.get(move));
      }
    }
    // if there are no allowable moves, call the default strategy if there is one.
    if (allowableMoves.isEmpty()) {
      if (backup == null) {
        throw new IllegalStateException("No possible moves");
      } else {
        return this.backup.chooseMove(model, color);
      }
    }
    return this.getHighestScoring(allowableMoves);
  }

  // returns a list of the coordinates of all the corners of the given reversi game.
  private List<CubeCoord> getCorners(ReadOnlyReversi model) {
    List<CubeCoord> corners = new ArrayList<>();
    int max = model.getBoardSize() - 1;
    int neg = -1 * max;
    corners.add(new CubeCoord(0, neg, max, false));
    corners.add(new CubeCoord(0, max, neg, false));
    corners.add(new CubeCoord(neg, 0, max, false));
    corners.add(new CubeCoord(max, 0, neg, false));
    corners.add(new CubeCoord(neg, max, 0, false));
    corners.add(new CubeCoord(max, neg, 0, false));
    return corners;
  }

  // Returns a list containing all 6 neighbors of all the coords in the given list.
  private List<CubeCoord> getNeighbors(List<CubeCoord> corners) {
    List<Integer> cornerQ = Arrays.asList(0, 1, 1, 0, -1, -1);
    List<Integer> cornerR = Arrays.asList(-1, -1, 0, 1, 1, 0);
    List<Integer> cornerS = Arrays.asList(1, 0, -1, -1, 0, 1);
    int newQ;
    int newR;
    int newS;
    List<CubeCoord> neighbors = new ArrayList<>();
    for (CubeCoord corner : corners) {
      for (int neighbor = 0; neighbor < 6; neighbor++) {
        newQ = corner.getQ() + cornerQ.get(neighbor);
        newR = corner.getR() + cornerR.get(neighbor);
        newS = corner.getS() + cornerS.get(neighbor);
        neighbors.add(new CubeCoord(newQ, newR, newS, false));
      }
    }
    return neighbors;
  }
}
