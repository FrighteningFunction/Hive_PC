package org.graphics.views;

import org.game.Coordinate;
import org.game.GameBoard;
import org.game.GraphicLogger;

import javax.swing.*;

public class BoardView extends JPanel implements NullPanel{

    public BoardView() {
        this.setLayout(null);
        setVisible(true);
        GraphicLogger.getLogger().info("BoardView was successfully created.");
    }

    @Override
    public Coordinate refactorCoordinate(Coordinate c) {
        double w = this.getWidth();
        double h = this.getHeight();

        double x = w / 2 + c.getX();
        double y = h / 2 - c.getY();

        return new Coordinate(x, y);
    }
}
