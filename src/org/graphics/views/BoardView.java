package org.graphics.views;

import org.game.Coordinate;
import org.game.GameBoard;
import org.game.GraphicLogger;

import javax.swing.*;

public class BoardView extends JPanel {

    public BoardView() {
        this.setLayout(null);
        setVisible(true);
        GraphicLogger.getLogger().info("BoardView was successfully created.");
    }
}
