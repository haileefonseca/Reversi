package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a standard game of Reversi using a square board. All rule-keeping, including move
 * validity and active players, are tracked here.
 */
public class SquareReversi extends AReversi {

  /**
   * Constructs a square game of Reversi with the given size. The size mut be an even number.
   * @param size the size of the board.
   */
  public SquareReversi(int size) {
    super(size);
    this.fillWithTiles();
    this.startupTiles();
  }

  @Override
  protected ITile[][] createValidBoard(int size) {
    if (size <= 1 || size % 2 != 0) {
      throw new IllegalArgumentException("Invalid board size.");
    }
    return new ITile[size][size];
  }

  @Override
  protected ITile[][] createValidBoard(ITile[][] madeBoard) {
    return madeBoard;
  }

  @Override
  protected int determineBuffer(int size) {
    return 1;
  }

  @Override
  protected List<Integer> getRNeighbors() {
    return Arrays.asList(-1, 0, 1, 1, 1, 0, -1,-1);
  }

  @Override
  protected List<Integer> getCNeighbors() {
    return Arrays.asList(1, 1, 1, 0, -1, -1, -1 ,0);
  }

  // fill this game board with empty Tiles
  private void fillWithTiles() {
    // loops over all rows and columns
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board.length; c++) {
        this.board[r][c] = new HexTile();
      }
    }
  }

  // adds the four tiles at the center of the board needed for a new game.
  private void startupTiles() {
    int center = (this.board.length / 2) - 1;
    this.board[center][center].setState(ITile.State.BLACK);
    this.board[center][center + 1].setState(ITile.State.WHITE);
    this.board[center + 1][center].setState(ITile.State.WHITE);
    this.board[center + 1][center + 1].setState(ITile.State.BLACK);
  }

  @Override
  public Boolean gameOver() {
    List<ITile> tiles;
    // iterate over all tiles
    for (int r = 0; r < this.board.length; r++) {
      for (int c = 0; c < this.board.length; c++) {
        // only take tiles that exist and are empty
        if (this.getStateAt(new CubeCoord(r, c, this.buffer, true))
                == ITile.State.NONE) {
          CubeCoord cube = new CubeCoord(r, c, this.buffer, true);
          // check the results of a valid white move, and catch if it is an invalid move
          try {
            tiles = this.returnResultsOfMove(cube, ITile.State.WHITE);
            if (tiles.size() > 1) {
              return false;
            }
          } catch (IllegalStateException | IllegalArgumentException e) {
            // do nothing - exception means there is no valid white move at this tile.
          }
          // check the results of a valid black move, and catch if it is an invalid move
          try {
            tiles = this.returnResultsOfMove(cube, ITile.State.BLACK);
            if (tiles.size() > 1) {
              return false;
            }
          } catch (IllegalStateException | IllegalArgumentException e) {
            // do nothing - exception means there is no valid black move at this tile.
          }
        }
      }
    }
    // if no moves found, return true for game over.
    return true;
  }

  @Override
  public int getBoardSize() {
    return this.board.length;
  }

  @Override
  public ITile[][] duplicate() {
    ITile[][] copy = new ITile[this.board.length][this.board.length];
    for (int r = 0; r < copy.length; r++) {
      for (int c = 0; c < copy.length; c++) {
        CubeCoord location = new CubeCoord(r, c, 1, true);
        copy[r][c] = new HexTile(this.getStateAt(location));
      }
    }
    return copy;
  }

  @Override
  public boolean isHex() {
    return false;
  }

  @Override
  public List<CubeCoord> getEmptyTiles() {
    int size = getBuffer();
    List<CubeCoord> validMoves = new ArrayList<>();
    for (int r = 0; r < board.length; r++) {
      for (int q = 0; q < board.length; q++) {
        try {
          CubeCoord here = new CubeCoord(q, r, size, true);
          if (this.getStateAt(here) == ITile.State.NONE) {
            validMoves.add(here);
          }
        } catch (IllegalArgumentException | IllegalStateException ignore) {
          // no effect - move is not valid
        }
      }
    }
    return validMoves;
  }

  @Override
  public List<CubeCoord> getCorners() {
    List<CubeCoord> corners = new ArrayList<>();
    int max = this.getBoardSize() - 1;
    corners.add(new CubeCoord(0, 0, 1, false));
    corners.add(new CubeCoord(0, max, 1, false));
    corners.add(new CubeCoord(max, max, 1, false));
    corners.add(new CubeCoord(max, 0, 1, false));
    return corners;
  }
}

