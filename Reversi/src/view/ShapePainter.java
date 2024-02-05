package view;

import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Color;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;

/**
 * Painter that is able to draw the given shape.
 */
public class ShapePainter implements Painter {

  // the model whose tile is being painted.
  private final ReadOnlyReversi model;

  /**
   * Constructs a ShapePainter.
   * @param model the game this painter corresponds to.
   */
  public ShapePainter(ReadOnlyReversi model) {
    this.model = model;
  }

  @Override
  public void draw(Polygon hex, Graphics g, int x, int y, int radius,
                   boolean selected, CubeCoord here, ITile.State active) {
    ITile.State color = this.model.getStateAt(here);
    if (selected) {
      g.setColor(Color.GREEN);
    } else {
      g.setColor(new Color(0,102, 0));
    }
    g.fillPolygon(hex);
    this.drawPiece(color, g, x, y, radius);
  }

  @Override
  public void handleButtonPressed(KeyEvent k) {
    // no effect in normal hexagon
  }

  @Override
  public Painter duplicate() {
    return new ShapePainter(this.model);
  }

  // draws a game piece at the given coordinates, with the given radius and color.
  private void drawPiece(ITile.State c, Graphics g, int x, int y, int radius) {
    x -= radius / 2;
    y -= radius / 2;
    switch (c.toString()) {
      case "BLACK":
        g.setColor(Color.RED);
        break;
      case "WHITE":
        g.setColor(Color.WHITE);
        break;
      default:
        return;
    }
    g.fillOval(x, y, radius, radius);
  }
}
