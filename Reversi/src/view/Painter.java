package view;

import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import model.CubeCoord;
import model.ITile;

/**
 * Represents a painter that can draw a polygon.
 */
public interface Painter {

  /**
   * Draws the given polygon at the given coordinates with the given size.
   * @param toDraw The polygon being drawn.
   * @param g The Graphics object drawing the polygon.
   * @param x the x-coordinate at which the polygon is drawn.
   * @param y the y-coordinate at which the polygon is drawn.
   * @param radius the size of the polygon.
   * @param selected whether the tile is selected or not.
   * @param here the location of this tile in the model.
   * @param active the player this shape is being drawn for.
   */
  void draw(Polygon toDraw, Graphics g, int x, int y, int radius,
            boolean selected, CubeCoord here, ITile.State active);

  /**
   * Processes the given key event.
   * @param k the key event
   */
  void handleButtonPressed(KeyEvent k);

  /**
   * Returns a copy of this Painter.
   * @return the copy produced
   */
  Painter duplicate();
}
