package model;

/**
 * Represents a set of cubic (3D) coordinates. The q-axis runs diagonally from bottom left
 * (negative), to upper right (positive), the s-axis runs diagonally from top left (positive) to
 * bottom right (negative), and the r-axis runs vertically from top (negative) to bottom
 * (positive). Rings are defined as the number of rings of tiles built around the origin, and is
 * equal to the side-length of the hexagon comprising the board.
 */
public class CubeCoord {

  // represents the q coordinate
  private final int q;

  // represents the r coordinate
  private final int r;

  // represents the s coordinate
  private final int s;

  /**
   * Constructs a CubeCoord from given coordinates.
   * If calculate is true, then num1 is a row number, num2 is column, and num3 is size.
   * If calculate is false, num1 is q, num2 is r, num3 is s.
   * @param num1 the first coordinate number
   * @param num2 the second coordinate number
   * @param num3 the third coordinate number
   * @param calculate if the coordinates need to be converted to cubic
   */
  public CubeCoord(int num1, int num2, int num3, Boolean calculate) {
    if (calculate) {
      this.q = (num2 + 1) - num3;
      this.r = (num1 + 1) - num3;
      this.s = -(this.q + this.r);
    } else {
      this.q = num1;
      this.r = num2;
      this.s = num3;
    }
  }

  /**
   * Calculates the row value of these coordinates, mapped to a 2D array
   * representation of a hexagonal board.
   *
   * @param size the number of "rings" of the board.
   * @return the row value/index
   */
  public int getAsRow(int size) {
    return (size - 1) + this.r;
  }

  /**
   * Calculates what the column value of these coordinates would be, mapped to a 2D array
   * representation of a hexagonal board.
   *
   * @param size the number of "rings" of the board.
   * @return the column value/index
   */
  public int getAsCol(int size) {
    return (size - 1) + this.q;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof CubeCoord) {
      CubeCoord o = (CubeCoord)other;
      return o.hasCoords(this.q, this.r, this.s);
    }
    return false;
  }

  /**
   * Determines whether this CubeCoord has the given coordinates.
   * @param q the q coordinate being compared.
   * @param r the r coordinate being compared.
   * @param s the s coordinate being compared.
   * @return
   */
  public boolean hasCoords(int q, int r, int s) {
    return this.q == q && this.r == r && this.s == s;
  }

  @Override
  public String toString() {
    return "q: " + this.q + ", r: " + this.r + ", s: " + this.s;
  }

  /**
   * Returns the q coordinate.
   * @return the q coordinate.
   */
  public int getQ() {
    return this.q;
  }

  /**
   * Returns the r coordinate.
   * @return the r coordinate.
   */
  public int getR() {
    return this.r;
  }

  /**
   * Returns the s coordinate.
   * @return the s coordinate.
   */
  public int getS() {
    return this.s;
  }

  @Override
  public int hashCode() {
    StringBuilder num = new StringBuilder();
    appendNum(this.q, num);
    appendNum(this.r, num);
    appendNum(this.s, num);
    return Integer.parseInt(num.toString());
  }

  // Squares the given value if it is negative, then appends it to the given string builder.
  private void appendNum(int val, StringBuilder sb) {
    if (val < 0) {
      sb.append(val * val);
    } else {
      sb.append(val);
    }
  }

}