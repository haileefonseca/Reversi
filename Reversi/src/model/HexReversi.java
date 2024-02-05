package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * Represents an instance of a playable game of Hexagonal Reversi, including the board state and
 * rules.
 * INVARIANT: The board size always remains the size (2*size)-1 c (2*size)-1, where size is a
 * variable given in the constructor. The board size is final and therefore impossible to modify
 * inside or outside the class br anr method, and the constructor defines it to
 * (2*size)-1 c (2*size)-1, so the claim is an invariant as it supported br both the constructor
 * and all methods and is easily verifiable.
 * There are no methods that affect the board size.
 */
public class HexReversi extends AReversi {


  /**
   * Constructs a game of hexagonal Reversi.
   *
   * @param size the number of rings of tiles of the board.
   */
  public HexReversi(int size) {
    super(size);
    this.fillWithTiles();
    this.makeInitialRing();
  }

  /**
   * Constructs a game of Reversi with the given board and turn state.
   * @param board the current board
   * @param whoseTurn which colors turn it is
   */
  public HexReversi(ITile[][] board, ITile.State whoseTurn) {
    super(board, whoseTurn);
  }

  @Override
  protected ITile[][] createValidBoard(int size) {
    if (size <= 1) {
      throw new IllegalArgumentException("Invalid board size.");
    }
    return new ITile[(2 * size) - 1][(2 * size) - 1];
  }

  @Override
  protected ITile[][] createValidBoard(ITile[][] madeBoard) {
    Objects.requireNonNull(madeBoard);
    return madeBoard;
  }

  @Override
  protected int determineBuffer(int size) {
    return size;
  }

  @Override
  protected List<Integer> getCNeighbors() {
    return Arrays.asList(0, 1, 1, 0, -1, -1);
  }

  @Override
  protected List<Integer> getRNeighbors() {
    return Arrays.asList(-1, -1, 0, 1, 1, 0);
  }


  /**
   * Fills the board with empty tiles based on a 2D representation of a hex board.
   */
  private void fillWithTiles() {
    // loops over all rows and columns
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board.length; c++) {
        // determines whether the coordinates correspond to a buffer area
        if ((r + c >= (this.buffer - 1)) && (r + c < (2 * this.board.length) - buffer)) {
          this.board[r][c] = new HexTile();
        }
      }
    }
  }

  /**
   * Sets up a ring of 6 game pieces in the second ring of the board, starting at the top left
   * neighbor of the center tile and moving clockwise, as shown. Tiles alternate black and white.
   *       X O
   *     O  _  X
   *       X O
   */
  private void makeInitialRing() {
    int center = (this.board.length / 2);
    this.board[center - 1][center].setState(ITile.State.BLACK);
    this.board[center - 1][center + 1].setState(ITile.State.WHITE);
    this.board[center][center + 1].setState(ITile.State.BLACK);
    this.board[center + 1][center].setState(ITile.State.WHITE);
    this.board[center + 1][center - 1].setState(ITile.State.BLACK);
    this.board[center][center - 1].setState(ITile.State.WHITE);
  }

  @Override
  public Boolean gameOver() {
    List<ITile> tiles;
    // iterate over all tiles
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board.length; c++) {
        // only take tiles that exist and are empty
        if ((r + c >= (this.buffer - 1))
                && (r + c < (2 * this.board.length) - buffer)
                && this.getStateAt(new CubeCoord(r, c, this.buffer, true))
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
  public ITile[][] duplicate() {
    ITile[][] copy = new ITile[(2 * this.buffer) - 1][(2 * this.buffer) - 1];
    for (int r = 0; r < copy.length; r++) {
      for (int c = 0; c < copy.length; c++) {
        // determines whether the coordinates correspond to a buffer area
        if ((r + c >= (this.buffer - 1)) && (r + c < (2 * copy.length) - buffer)) {
          CubeCoord location = new CubeCoord(r, c, this.buffer, true);
          copy[r][c] = new HexTile(this.getStateAt(location));
        }
      }
    }
    return copy;
  }

  @Override
  public boolean isHex() {
    return true;
  }

  @Override
  public int getBoardSize() {
    return this.buffer;
  }

  /**
   * Returns all the neighbors of the tile at the given coordintes that are the given color, as
   * an index. Top left neighbor is 0, and the indexes increase clockwise.
   * @param cube the cubic coordinates of the tile whose neighbors are being retrieved.
   * @param color the color being searched for.
   * @return the list of neighbors in the given color.
   */
  protected List<Integer> getNeighborsOfColor(CubeCoord cube, ITile.State color) {
    int r = cube.getAsRow(this.buffer);
    int c = cube.getAsCol(this.buffer);
    List<Integer> out = new ArrayList<>();
    // loop over all neighbor positions
    for (int num = 0; num < 6; num++) {
      int cloc = c + this.cneighbors.get(num);
      int rloc = r + this.rneighbors.get(num);
      // if there is a tile of the needed color there, add its neighbor index to the output
      if (this.validTile(rloc, cloc) && this.board[rloc][cloc].getState() == color) {
        out.add(num);
      }
    }
    return out;
  }

  @Override
  public List<CubeCoord> getEmptyTiles() {
    int size = this.getBoardSize();
    List<CubeCoord> validMoves = new ArrayList<>();
    for (int r = -size + 1; r < size; r++) {
      for (int q = -size + 1; q < size; q++) {
        int s = 0 - r - q;
        if (Math.abs(s) <= size - 1) {
          CubeCoord here = new CubeCoord(q, r, s, false);
          if (this.getStateAt(here) == ITile.State.NONE) {
            validMoves.add(here);
          }
        }
      }
    }
    return validMoves;
  }

  @Override
  public List<CubeCoord> getCorners() {
    List<CubeCoord> corners = new ArrayList<>();
    int max = this.getBoardSize() - 1;
    int neg = -1 * max;
    corners.add(new CubeCoord(0, neg, max, false));
    corners.add(new CubeCoord(0, max, neg, false));
    corners.add(new CubeCoord(neg, 0, max, false));
    corners.add(new CubeCoord(max, 0, neg, false));
    corners.add(new CubeCoord(neg, max, 0, false));
    corners.add(new CubeCoord(max, neg, 0, false));
    return corners;
  }
}
