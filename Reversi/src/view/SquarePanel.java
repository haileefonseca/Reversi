package view;

import java.awt.Rectangle;
import java.awt.Polygon;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;

/**
 * Represents a panel depicting a square game of Reveri.
 */
public class SquarePanel extends JPanel {

  // The game being visualized.
  private final ReadOnlyReversi model;

  // The list of hexagons making up the board image.
  private ArrayList<Polygon> squares;

  // The cubic coordinate representing the currently selected hexagon.
  // See CubeCoord class for details on coordinate system.
  private CubeCoord clicked;

  // A map of polygons to their cubic coordinate locations on the game board.
  private final Map<Polygon, CubeCoord> locations;

  // Whether there is a currently selected hexagon.
  private boolean beenClicked;

  // Publishes the panel's messages.
  private final GraphicView publisher;

  // The Painter drawing the tiles of the game.
  private final Painter artist;

  /**
   * Constructs the panel and gives it the ability to listen for events.
   *
   * @param model     a RO Reversi to model
   * @param publisher a Publisher to publish messages.
   * @param artist    a Painter to draw the tiles.
   */
  public SquarePanel(ReadOnlyReversi model, GraphicView publisher, Painter artist) {
    super();
    this.model = model;
    this.squares = new ArrayList<>();
    view.SquarePanel.MouseEventsListener mouseListener = new view.SquarePanel.MouseEventsListener();
    this.addMouseListener(mouseListener);
    view.SquarePanel.KeyEventsListener keyListener = new view.SquarePanel.KeyEventsListener();
    this.addKeyListener(keyListener);
    this.locations = new HashMap<>();
    this.clicked = new CubeCoord(0, 0, 0, false);
    setFocusable(true);
    this.publisher = publisher;
    this.artist = artist.duplicate();
  }

  @Override
  protected void paintComponent(Graphics g) {
    this.squares = new ArrayList<>();
    super.paintComponent(g);
    Rectangle bounds = this.getBounds();
    int size = this.model.getBoardSize();
    int minWidth = Math.min(bounds.height, bounds.width);
    int width = (minWidth / (size * 2));
    for (int r = 0; r < size; r++) {
      for (int q = 0; q < size; q++) {
        CubeCoord here = new CubeCoord(q, r, 0, false);
        boolean selected = here.equals(this.clicked) && this.beenClicked;
        Polygon added = this.drawRect(bounds, g, (int) q * width * 1.8, (int) r * width * 1.8,
                (int) (width * 1.6), selected, here);
        this.squares.add(added);
        this.locations.put(added, here);
      }
    }
  }

  // Draws a rectangle based on the given parameters.
  private Polygon drawRect(Rectangle bounds, Graphics g, double x, double y, int width,
                           Boolean selected, CubeCoord here) {
    width /= 2;
    int[] xs = { width, width, -width, -width};
    int[] ys = { width, -width, -width, width};
    for (int points = 0; points < 4; points++) {
      xs[points] += x;
      ys[points] += y;
    }
    Polygon square = new Polygon(xs, ys, 4);
    ITile.State active = this.publisher.getColor();
    g.translate(bounds.width / 7, bounds.height / 7);
    this.artist.draw(square, g.create(), (int) x, (int) y, width, selected, here, active);
    g.translate(-bounds.width / 7, -bounds.height / 7);
    return square;
  }

  // re-assigns the currently selected tile to a new one, printing the new coordinates.
  protected void reassignClicked(Polygon square) {
    this.clicked = locations.get(square);
  }

  /**
   * Class that implements the panels response to mouse events.
   */
  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      Point physLoc = e.getPoint();
      Point2D location = transformPhysicalToLogical().transform(physLoc, null);
      for (Polygon square : squares) {
        if (square.contains(location)) {
          if (beenClicked && clicked.equals(locations.get(square))) {
            beenClicked = false;
          } else {
            beenClicked = true;
            reassignClicked(square);
          }
        }
      }
      repaint();
    }
  }

  /**
   * Class that implements the panels response to key events.
   */
  private class KeyEventsListener extends KeyAdapter {
    @Override
    public void keyReleased(KeyEvent e) {
      if (beenClicked && e.getKeyChar() == KeyEvent.VK_ENTER) {
        publisher.notifyClicked(clicked);
        beenClicked = false;
      } else if (e.getKeyChar() == 'p') {
        publisher.notifyPass();
        beenClicked = false;
      }
      artist.handleButtonPressed(e);
      repaint();
    }
  }

  // transforms physical coordinates from the display into logical, board coordinates.
  private AffineTransform transformPhysicalToLogical() {
    AffineTransform aff = new AffineTransform();
    Rectangle bounds = this.getBounds();
    Dimension preferred = new Dimension(bounds.width, bounds.height);
    aff.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    aff.translate(-getWidth() / 7., -getHeight() / 7.);
    return aff;
  }

}
