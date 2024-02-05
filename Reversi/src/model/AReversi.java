package model;

import java.util.ArrayList;
import java.util.List;

import controller.ModelStatusListener;

/**
 * Represents an abstract game of Reversi with no particular board shape.
 */
public abstract class AReversi implements IReversi {

  // the actual representation of the game board
  protected final ITile[][] board;

  // The number of excess spaces + 1 before the tiles begin in the first row of the board.
  protected final int buffer;

  // the color of the player who last played.
  private ITile.State lastWent;

  // Whether the last move was a pass.
  private boolean justPassed;

  // The Controllers subscribed to messages from this game.
  private final List<ModelStatusListener> subs;

  // The differences in column-indices between a tile and its neighbors, starting at the top
  // left neighbor and moving clockwise.
  protected final List<Integer> cneighbors;

  // The differences in row-indices between a tile and its neighbors, starting at the top left
  // neighbor and moving clockwise.
  protected final List<Integer> rneighbors;

  /**
   * Sets up this abstract reversi game.
   * @param size the size of the game.
   */
  public AReversi(int size) {
    this.board = createValidBoard(size);
    this.buffer = determineBuffer(size);
    this.justPassed = false;
    this.lastWent = ITile.State.BLACK;
    this.subs = new ArrayList<>();
    this.cneighbors = getCNeighbors();
    this.rneighbors = getRNeighbors();
  }

  /**
   * Constructs a game of Reversi using the given board state.
   * @param madeBoard the board state.
   * @param whoseTurn whose turn it is in that board state.
   */
  public AReversi(ITile[][] madeBoard, ITile.State whoseTurn) {
    this.board = createValidBoard(madeBoard);
    this.buffer = determineBuffer((madeBoard.length + 1) / 2);
    this.justPassed = false;
    this.lastWent = this.flip(whoseTurn);
    this.subs = new ArrayList<>();
    this.cneighbors = getCNeighbors();
    this.rneighbors = getRNeighbors();
  }

  /**
   * Abstract implementations of methods used for Reversi construction. They are included here
   * and not in the IReversi interface as they should not be accessible outside the models
   * themselves - they are private methods shared by all versions of Reversi.
   */
  // creates a board with the start-of-game tiles in place.
  protected abstract ITile[][] createValidBoard(int size) throws IllegalArgumentException;

  // creates a board using the given board state.
  protected abstract ITile[][] createValidBoard(ITile[][] madeBoard)
          throws IllegalArgumentException;

  // determines the buffer to be used for this game's board representation.
  protected abstract int determineBuffer(int size);

  // returns the list of column offsets for a tile's neighbors
  protected abstract List<Integer> getCNeighbors();

  // returns the list of column offsets for a tile's neighbors
  protected abstract List<Integer> getRNeighbors();

  @Override
  public void move(CubeCoord cube, ITile.State color)
          throws IllegalStateException, IllegalArgumentException {
    if (color == this.lastWent) {
      throw new IllegalArgumentException("Not your turn!");
    }
    int r = cube.getAsRow(this.buffer);
    int c = cube.getAsCol(this.buffer);
    if (this.gameOver()) {
      throw new IllegalStateException("Cannot do a move when the game is over.");
    }
    if (color == ITile.State.NONE) {
      throw new IllegalArgumentException("Cannot play as empty board.");
    }
    if (this.validTile(r, c) && this.board[r][c].getState() != ITile.State.NONE) {
      throw new IllegalStateException("Cannot place a tile on top of another.");
    }
    // gets the list of all tiles that would be affected by the move
    List<ITile> toFlip = this.moveResults(cube, color);
    if (toFlip.size() <= 1) {
      throw new IllegalStateException("Invalid move.");
    }
    // flips all the affected tiles to the color of the placed piece.
    for (ITile tile : toFlip) {
      tile.setState(color);
    }
    this.lastWent = color;
    this.justPassed = false;
    this.notifyTurnChange();
  }

  @Override
  public void pass() throws IllegalStateException {
    if (!this.justPassed) {
      this.lastWent = this.flip(this.lastWent);
      this.justPassed = true;
      this.notifyTurnChange();
    } else {
      throw new IllegalStateException("Game over!");
    }
  }

  @Override
  public void startGame() {
    this.notifyTurnChange();
  }

  @Override
  public void notifyTurnChange() {
    ITile.State turn = this.flip(this.lastWent);
    for (ModelStatusListener sub : subs) {
      sub.turnBegins(turn);
    }
  }

  @Override
  public void subscribe(ModelStatusListener c) {
    this.subs.add(c);
  }

  @Override
  public List<ITile> returnResultsOfMove(CubeCoord cube, ITile.State moveColor) {
    // gets the list of tiles affected by the move
    List<ITile> tiles = this.moveResults(cube, moveColor);
    List<ITile> out = new ArrayList<>();
    // duplicates the tiles and adds them to a new list
    for (ITile tile : tiles) {
      out.add(new HexTile(tile.getState()));
    }
    return out;
  }

  /**
   * Returns a list of all tiles that would be affected by a piece placed at the given coordinates.
   *
   * @param cube the cubic coordinates of the piece placed.
   * @param moveColor the color of the piece being placed.
   * @return the list of tiles that would change colors.
   */
  protected List<ITile> moveResults(CubeCoord cube, ITile.State moveColor) {
    int r = cube.getAsRow(this.buffer);
    int c = cube.getAsCol(this.buffer);
    if (!this.validTile(r, c) || this.board[r][c].getState() != ITile.State.NONE) {
      throw new IllegalArgumentException("Invalid tile position.");
    }
    // gets the color of tile that would be flipped
    ITile.State oppCol = this.flip(moveColor);
    // gets a list of the neighbors with the opposite color of the placed piece
    List<Integer> moves = this.getNeighborsOfColor(cube, oppCol);
    if (moves.isEmpty()) {
      throw new IllegalStateException("Invalid move.");
    }
    // retrieve all tiles that would actually be affected by the move
    List<ITile> toFlip = new ArrayList<>();
    for (int n : moves) {
      List<ITile> sandResults = this.getSandwich(r, c, this.rneighbors.get(n),
              this.cneighbors.get(n), moveColor);
      for (ITile sand : sandResults) {
        toFlip.add(sand);
      }
    }
    // adds the tile being placed to the list of affected tiles
    toFlip.add(this.board[r][c]);
    return toFlip;
  }

  /**
   * Returns the tiles in a given direction that would be sandwiched, if the piece at the
   * given coordinates was placed.
   * @param c the c-coordinate of the placed piece.
   * @param r the r-coordinate of the given piece.
   * @param dirc the c coordinate offset of a tile and its neighbor in a specific direction.
   * @param dirr the r coordinate offset of a tile and its neighbor in a specific direction.
   * @param endState the color of the tile that would end the sandwich.
   * @return the list of sandwiched tiles.
   */
  protected List<ITile> getSandwich(int r, int c, int dirr, int dirc, ITile.State endState) {
    List<ITile> toFlip = new ArrayList<>();
    // keep going until you hit a tile of the given color
    while (c >= 0 && r >= 0 &&
            c <= (this.board.length - 1) && r <= (this.board.length - 1)) {
      c += dirc;
      r += dirr;
      // if the next tile in the given direction exists:
      if (this.validTile(r, c)) {
        ITile tile = this.board[r][c];
        // if it is the color we are searching for, the sandwich is complete
        if (tile.getState() == endState) {
          return toFlip;
          // if it is an empty tile, no sandwich exists in that direction
        } else if (tile.getState() == ITile.State.NONE) {
          break;
        }
        // otherwise add the tile to the list of affected and keep searching for the end
        toFlip.add(tile);
      }
    }
    // if the edge is reached without hitting a tile, return empty list.
    return new ArrayList<ITile>();
  }

  @Override
  public int calcScore(ITile.State color) {
    int score = 0;
    // iterate over the entire board
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board.length; c++) {
        // if the tile exists and is the color being searched for, increase score
        try {
          if (this.board[r][c].getState() == color) {
            score++;
          }
        } catch (NullPointerException e) {
          // do nothing, tile doesnt exist
        }
      }
    }
    return score;
  }

  @Override
  public ITile.State getStateAt(CubeCoord cube) throws IllegalArgumentException {
    int r = cube.getAsRow(this.buffer);
    int c = cube.getAsCol(this.buffer);
    if (this.validTile(r, c)) {
      return this.board[r][c].getState();
    }
    throw new IllegalArgumentException("No tile there.");
  }

  @Override
  public boolean isLegalMove(CubeCoord move, ITile.State color) {
    try {
      return this.returnResultsOfMove(move, color).size() > 1;
    } catch (IllegalStateException e) {
      return false;
    }
  }

  @Override
  public boolean hasMoves(ITile.State color) {
    for (int r = 0; r < this.board.length; r++) {
      for (int c = 0; c < this.board.length; c++) {
        // check if there is a valid move at that location
        try {
          CubeCoord location = new CubeCoord(r, c, this.buffer, true);
          if (this.isLegalMove(location, color)) {
            return true;
          }
        } catch (Exception ignored) {
          // exception means move is invalid
        }
      }
    }
    return false;
  }

  // Returns the opposite of the given color (black -> white, white -> black).
  protected ITile.State flip(ITile.State col) {
    if (col == ITile.State.BLACK) {
      return ITile.State.WHITE;
    } else {
      return ITile.State.BLACK;
    }
  }

  @Override
  public ITile.State getActiveTurn() {
    return this.flip(this.lastWent);
  }

  /**
   * Determines whether there is a tile at the given coordinates of the board.
   * @param c the c-coordinate being evaluated
   * @param r the r-coordinate being evaluated
   * @return whether the coordinates are valid
   */
  protected boolean validTile(int r, int c) {
    return !(c < 0 || r < 0 || c >= this.board.length || r >= this.board.length
        || (c + r < (this.buffer - 1)) || (c + r >= (2 * this.board.length) - this.buffer));
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
    for (int num = 0; num < this.cneighbors.size(); num++) {
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
  public int getBuffer() {
    return this.buffer;
  }
}
