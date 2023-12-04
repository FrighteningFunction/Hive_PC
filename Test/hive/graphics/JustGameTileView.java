package hive.graphics;

import hive.game.GameLogic;
import hive.game.TileStates;
import hive.graphics.views.GameTileView;
import hive.insects.Insect;
import hive.insects.Queen;

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

        Insect testInsect = new Queen(gameLogic.getBluePlayer(), gameLogic);

        GameTileView gameTileView = new GameTileView();
        gameTileView.setInsect(testInsect);
        gameTileView.setInitialized(true);
        gameTileView.setState(TileStates.UNSELECTED);

        frame.add(gameTileView);


        // Center the frame on the screen and make it visible.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
