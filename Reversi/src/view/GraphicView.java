package view;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import controller.PlayerActionListener;
import model.CubeCoord;
import model.ITile;
import model.ReadOnlyReversi;
import player.PlayerActionPub;

/**
 * Class for a graphics-based view of a Reversi game. GraphicView updates when there is a click
 * or key released, which is how the user selects/deselects a tile to input their desired move.
 */
public class GraphicView extends JFrame implements ViewPub, PlayerActionPub {

  // The frame in which the board is drawn.
  private final JFrame frame;

  // The list of Controllers subscribed to messages from this view.
  private final ArrayList<PlayerActionListener> subs;

  // The main panel depicting the game.
  private final JPanel mainPanel;

  // The color of the player for whom this frame is drawing.
  private ITile.State color;


  /**
   * Constructor for a GraphicsView, which will render a graphical representation of the Reversi.
   * board state.
   * @param model the game being visualized
   */
  public GraphicView(ReadOnlyReversi model, Painter artist) {
    super();
    setFocusable(true);
    this.frame = new JFrame("Reversi");
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.subs = new ArrayList<>();
    if (model.isHex()) {
      this.mainPanel = new HexPanel(model, this, artist);
    } else {
      this.mainPanel = new SquarePanel(model, this, artist);
    }
    this.mainPanel.setBackground(new Color(136, 184, 109));
    this.frame.add(this.mainPanel);
    this.frame.setSize(600, 600);
    this.color = ITile.State.WHITE;
  }

  @Override
  public void setVisible(boolean start) {
    this.frame.setVisible(start);
  }

  @Override
  public void subscribe(PlayerActionListener c) {
    this.subs.add(c);
  }

  @Override
  public void notifyClicked(CubeCoord c) {
    for (PlayerActionListener sub : this.subs) {
      sub.receiveMove(c);
    }
  }

  @Override
  public void notifyPass() {
    for (PlayerActionListener sub : this.subs) {
      sub.receivePass();
    }
  }

  @Override
  public void refreshView() {
    repaint();
    this.mainPanel.repaint();
  }

  @Override
  public void requestMove() {
    // does nothing in a View - views wait for player input
  }

  @Override
  public void setBackground(ITile.State col) {
    if (col == ITile.State.BLACK) {
      // changes the background of the frame, which affects the color of the header.
      this.frame.setBackground(Color.BLACK);
      this.color = col;
    }
  }

  @Override
  public void pushMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  /**
   * Returns the color of the player represented by this board.
   * @return the color of the player.
   */
  protected ITile.State getColor() {
    return this.color;
  }
}

