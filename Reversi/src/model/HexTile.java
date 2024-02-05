package model;

/**
 * Represents a hexagonal game tile, with an enum representing a piece placed on it.
 */
public class HexTile implements ITile {

  // a State representing the color on this tile.
  private State state;

  /**
   * Constructs a HexTile with a default/no color.
   */
  public HexTile() {
    this(ITile.State.NONE);
  }

  /**
   * Constructs a model.HexTile with a given state.
   * @param state the state of the tile.
   */
  public HexTile(State state) {
    this.state = state;
  }

  @Override
  public State getState() {
    return this.state;
  }

  @Override
  public void setState(State newState) {
    this.state = newState;
  }
}
