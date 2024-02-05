package strategy;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;
import java.util.Map;

/**
 * Strategy class which returns the coordinates of the move that scores the most points out of
 * all possible executable moves. In the case of a tie, it picks the uppermost-leftmost coordinate.
 */
public class HighestScore extends AStrategy {
  @Override
  public CubeCoord chooseMove(ReadOnlyReversi model, ITile.State color)
          throws IllegalStateException {
    Map<CubeCoord, Integer> moves = super.getValidMoves(model, color);
    return this.getHighestScoring(moves);
  }
}
