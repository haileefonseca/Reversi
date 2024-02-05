package mocks;

import controller.PlayerActionListener;
import model.CubeCoord;
import model.IReversi;
import model.ITile;
import view.GraphicView;
import view.ShapePainter;
import view.ViewPub;

/**
 * Mocks the behavior of a view by constructing a log and then delegating behavior to a real view.
 */
public class ViewPubMock implements ViewPub {
  private final StringBuilder log;
  private final GraphicView delegate;

  public ViewPubMock(IReversi game) {
    this.log = new StringBuilder();
    this.delegate = new GraphicView(game, new ShapePainter(game));
  }

  @Override
  public void requestMove() {
    this.log.append("requestMove called on the view");
    this.delegate.requestMove();
  }

  @Override
  public void subscribe(PlayerActionListener c) {
    this.log.append("added a subscriber\n");
    this.delegate.subscribe(c);
  }

  @Override
  public void notifyClicked(CubeCoord c) {
    this.log.append("click occurred in the view at ").append(c.toString()).append("\n");
    this.delegate.notifyClicked(c);
  }

  @Override
  public void notifyPass() {
    this.log.append("the view received a pass\n");
    this.delegate.notifyPass();
  }

  @Override
  public void refreshView() {
    this.log.append("view refreshed\n");
    this.delegate.refreshView();
  }

  @Override
  public void setBackground(ITile.State color) {
    this.log.append("set background to ").append(color.toString()).append("\n");
    this.delegate.setBackground(color);
  }

  @Override
  public void pushMessage(String message) {
    this.log.append("pushed the message ").append(message).append("\n");
    this.delegate.pushMessage(message);
  }

  public String getLog() {
    return this.log.toString();
  }

  @Override
  public void setVisible(boolean start) {
    this.log.append("visualized!\n");
  }
}