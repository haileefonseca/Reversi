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
 * Class that renders a game of hexagonal Reversi and handles user interaction.
 */
public class HexPanel extends JPanel {

  // The game being visualized.
  private final ReadOnlyReversi model;

  // The list of hexagons making up the board image.
  private ArrayList<Polygon> hexes;

  // The cubic coordinate representing the currently selected hexagon.
  // See CubeCoord class for details on coordinate system.
  private CubeCoord clicked;

  // A map of polygons to their cubic coordinate locations on the game board.
  private final Map<Polygon, CubeCoord> locations;

  // Whether there is a currently selected hexagon.
  private boolean beenClicked;

  // Publishes the panel's messages.
  private final GraphicView publisher;

  // whether hints are enabled
  private boolean hints;

  // The Painter that draws the game tiles
  private final Painter artist;

  /**
   * Constructs the panel and gives it the ability to listen for events.
   * @param model a RO Reversi to model
   * @param publisher a Publisher to publish messages.
   * @param artist the Painter that draws the tiles.
   */
  public HexPanel(ReadOnlyReversi model, GraphicView publisher, Painter artist) {
    super();
    this.model = model;
    this.hexes = new ArrayList<>();
    MouseEventsListener mouseListener = new MouseEventsListener();
    this.addMouseListener(mouseListener);
    KeyEventsListener keyListener = new KeyEventsListener();
    this.addKeyListener(keyListener);
    this.locations = new HashMap<>();
    this.clicked = new CubeCoord(-1, 0, 0, false);
    setFocusable(true);
    this.publisher = publisher;
    this.hints = false;
    this.artist = artist.duplicate();
  }

  @Override
  protected void paintComponent(Graphics g) {
    this.hexes = new ArrayList<>();
    super.paintComponent(g);
    Rectangle bounds = this.getBounds();
    int size = model.getBoardSize();
    int minWidth = Math.min(bounds.height, bounds.width);
    int radius = (minWidth / (size * 2)) / 2;

    for (int r = -size + 1; r < size; r++) {
      for (int q = -size + 1; q < size; q++) {
        int s = 0 - r - q;
        if (Math.abs(s) <= size - 1) {
          CubeCoord here = new CubeCoord(q, r, s, false);
          Boolean selected = here.equals(this.clicked) && this.beenClicked;
          double dsin60 = (2 * radius * Math.sin(Math.toRadians(60.0)));
          double newq = (2 * radius * (q + 0.5 * r));
          Polygon added = this.drawHex(bounds, g, newq, r * dsin60, (int) (radius * 0.95),
                  selected, here);
          this.hexes.add(added);
          this.locations.put(added, here);
        }
      }
    }
  }

  // draws a hexagon at the given coordinates, with the given radius and color. if there is
  // a game piece, draws that as well. scales coordinates based on Rectangle bounds.
  private Polygon drawHex(Rectangle bounds, Graphics g, double x, double y, int radius,
                          Boolean selected, CubeCoord here) {
    int hypoteneuse = (int) (radius / Math.sin(Math.toRadians(60)));
    int deltaY = (int) (radius * Math.tan(Math.toRadians(30)));
    int[] xs = {0, radius, radius, 0, -radius, -radius};
    int[] ys = {hypoteneuse, deltaY, -deltaY, -hypoteneuse, -deltaY, deltaY};
    for (int points = 0; points < 6; points++) {
      xs[points] += x;
      ys[points] += y;
    }
    Polygon hex = new Polygon(xs, ys, 6);
    ITile.State active = this.publisher.getColor();
    g.translate(bounds.width / 2, bounds.height / 2);
    this.artist.draw(hex, g.create(), (int) x, (int) y, radius, selected, here, active);
    g.translate(-bounds.width / 2, -bounds.height / 2);
    return hex;
  }

  // re-assigns the currently selected tile to a new one, printing the new coordinates.
  protected void reassignClicked(Polygon hex) {
    this.clicked = locations.get(hex);
  }

  /**
   *  Class that implements the panels response to mouse events.
   */
  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      Point physLoc = e.getPoint();
      Point2D location = transformPhysicalToLogical().transform(physLoc, null);
      for (Polygon hex : hexes) {
        if (hex.contains(location)) {
          if (beenClicked && clicked.equals(locations.get(hex))) {
            beenClicked = false;
          } else {
            beenClicked = true;
            reassignClicked(hex);
          }
        }
      }
      repaint();
    }
  }

  /**
   *  Class that implements the panels response to key events.
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
      } else if (e.getKeyChar() == 'h') {
        hints = !hints;
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
    aff.translate(-getWidth() / 2., -getHeight() / 2.);
    return aff;
  }
}
