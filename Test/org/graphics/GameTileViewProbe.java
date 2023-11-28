package org.graphics;

import javax.swing.*;

import org.game.Coordinate;
import org.game.GameLogic;
import org.game.GameTile;
import org.game.TileStates;
import org.graphics.controllers.GameTileController;
import org.graphics.views.GameTileView;
import org.graphics.views.PlayerPanelView;
import org.insects.Insect;
import org.insects.Queen;

public class GameTileViewProbe {
    private static GameLogic gameLogic = GameLogic.getInstance();

    public static void main(String[] args) {
        gameLogic.newGameForTesting();
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("GameTileView Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlayerPanelView playerPanelView = new PlayerPanelView();
        frame.add(playerPanelView);


        GameTileView gameTileView = new GameTileView();

        Insect testInsect = new Queen(gameLogic.getBlackPlayer(), gameLogic);

        GameTile gameTile = new GameTile(testInsect);

        GameTileController<PlayerPanelView> gameTileController = new GameTileController<>(gameTile, playerPanelView, gameTileView);


        // Center the frame on the screen and make it visible.
        //frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

