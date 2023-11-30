package org.graphics;

import org.game.Coordinate;
import org.game.GameLogic;
import org.game.GameTile;
import org.game.TileStates;
import org.graphics.controllers.GameTileController;
import org.graphics.views.GameTileView;
import org.insects.Insect;
import org.insects.Queen;

import javax.swing.*;
import java.awt.*;

public class JustGameTileView {
    private static GameLogic gameLogic = GameLogic.getInstance();

    public static void main(String[] args) {
        gameLogic.newGameForTesting();
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("GameTileView Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        Insect testInsect = new Queen(gameLogic.getBlackPlayer(), gameLogic);

        GameTileView gameTileView = new GameTileView();
        gameTileView.setInsect(testInsect);
        gameTileView.setInitialized(true);
        gameTileView.setStates(TileStates.UNSELECTED);

        frame.add(gameTileView);


        // Center the frame on the screen and make it visible.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
