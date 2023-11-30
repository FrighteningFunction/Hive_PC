package org.graphics;

import org.game.Coordinate;
import org.game.GameLogic;
import org.game.GameTile;
import org.graphics.controllers.GameTileController;
import org.insects.Insect;
import org.insects.Queen;

import javax.swing.*;

public class JustGameTileView {
    private static GameLogic gameLogic = GameLogic.getInstance();

    public static void main(String[] args) {
        gameLogic.newGameForTesting();
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("GameTileView Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add();

        Insect testInsect = new Queen(gameLogic.getBlackPlayer(), gameLogic);

        GameTile gameTile = new GameTile(testInsect);

        Coordinate c = new Coordinate(0,0);
        gameTile.setCoordinate(c);

        GameTileController<TestJPanel> gameTileController = new GameTileController<>(gameTile, mainPanel);


        // Center the frame on the screen and make it visible.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
