package view;

import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;

/**
 * Enables the ability to turn hints on or off on the game being played, by drawing the number of
 * tiles that would be flipped if the selected move was done.
 */
public class HintFeature implements Painter {

  // The Painter that draws the actual tile
  private final Painter shape;

  // The game being played
  private final ReadOnlyReversi model;

  // Whether hints are enabled
  private boolean hintsOn;

  /**
   * Constructs a HintFeature.
   * @param shape the shape drawing the actual tile.
   * @param model the game being played.
   */
  public HintFeature(Painter shape, ReadOnlyReversi model) {
    this.shape = shape;
    this.model = model;
    this.hintsOn = false;
  }

  @Override
  public void draw(Polygon toDraw, Graphics g, int x, int y, int radius,
                   boolean selected, CubeCoord here, ITile.State active) {
    this.shape.draw(toDraw, g, x, y, radius, selected, here, active);
    if (selected && this.hintsOn) {
      String score = "0";
      try {
        score = (this.model.returnResultsOfMove(here, active).size() - 1) + "";
      } catch (IllegalStateException ignore) {
        // do nothing - invalid move has score of 0
      } catch (IllegalArgumentException ignore) {
        return;
      }
      g.setColor(Color.BLACK);
      g.drawString(score, x, y);
    }
  }

  @Override
  public void handleButtonPressed(KeyEvent k) {
    if (k.getKeyChar() == 'h') {
      this.hintsOn = !this.hintsOn;
    }
  }

  @Override
  public Painter duplicate() {
    return new HintFeature(this.shape, this.model);
  }
}
