package mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.ModelStatusListener;
import model.CubeCoord;
import model.HexTile;
import model.IReversi;
import model.ITile;

/**
 * A mock model of a hexagonal reversi game, effective for testing Strategies.
 */
public class ReversiMock implements IReversi {

  // Maps CubeCoords to the score of a move at that coordinate
  private final Map<CubeCoord, Integer> tilesToEval;

  //  The log storing the method call information.
  private final List<String> log;
  private final List<CubeCoord> corners;

  /**
   * Constructor for a HexMock. Its only field, tilesToEval is a map of CubeCoord to Integer.
   * The CubeCoords represent what is considered to be a valid move in the mock, and the Integer
   * is the score of a move at that location.
   *
   * @param tilesToEval the given map of tiles and scores.
   */
  public ReversiMock(Map<CubeCoord, Integer> tilesToEval) {
    this.tilesToEval = tilesToEval;
    this.log = new ArrayList<>();
    this.corners = new ArrayList<>();
  }

  @Override
  public void move(CubeCoord cube, ITile.State color) {
    this.log.add("\nMove received!\n");
  }

  @Override
  public void pass() {
    this.log.add("\nPass received!\n");
  }

  @Override
  public void startGame() {
    return;
  }

  @Override
  public boolean hasMoves(ITile.State color) {
    return false;
  }

  @Override
  public ITile.State getActiveTurn() {
    return null;
  }

  @Override
  public boolean isHex() {
    return true;
  }

  @Override
  public int getBuffer() {
    return 0;
  }

  @Override
  public List<CubeCoord> getEmptyTiles() {
    ArrayList<CubeCoord> tiles = new ArrayList();
    tiles.addAll(this.tilesToEval.keySet());
    for (CubeCoord c : tiles) {
      log.add("Found the tile at coordinate: " + c.toString() + " that exists in the mock.");
    }
    return tiles;
  }

  @Override
  public List<CubeCoord> getCorners() {
    return this.corners;
  }

  // returns a list which represents tiles that would be flipped by a move at this cube-coord.
  // since the mock has no board, these tiles are only to represent possible scores.
  @Override
  public List<ITile> returnResultsOfMove(CubeCoord cube, ITile.State color) {
    List<ITile> valid = new ArrayList<>();
    for (int i = 0; i < this.tilesToEval.get(cube); i++) {
      valid.add(new HexTile(ITile.State.WHITE));
    }
    log.add("Results of move would flip " + valid.size() + " tiles.");
    return valid;
  }

  @Override
  public int calcScore(ITile.State color) {
    return 0;
  }

  @Override
  public ITile.State getStateAt(CubeCoord cube) {
    if (this.tilesToEval.containsKey(cube)) {
      log.add("Found the tile at coordinate: " + cube.toString() + " that exists in the mock.");
      return ITile.State.NONE;
    } else {
      return ITile.State.BLACK;
    }
  }

  @Override
  public Boolean gameOver() {
    return null;
  }

  @Override
  public int getBoardSize() {
    return this.tilesToEval.size();
  }


  @Override
  public ITile[][] duplicate() {
    return new ITile[0][];
  }

  @Override
  public boolean isLegalMove(CubeCoord move, ITile.State color) {
    return false;
  }

  /**
   * Returns the accumulated log of this mock.
   *
   * @return The StringBuilder log of this mock, as a string.
   */
  public String getLog() {
    return this.log.toString();
  }

  @Override
  public void notifyTurnChange() {
    return;
  }

  @Override
  public void subscribe(ModelStatusListener c) {
    return;
  }
}
