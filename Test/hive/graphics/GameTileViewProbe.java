package hive.graphics;

import javax.swing.*;
import javax.swing.border.Border;

import hive.game.Coordinate;
import hive.game.GameLogic;
import hive.game.GameTile;
import hive.game.TileStates;
import hive.graphics.controllers.GameTileController;
import hive.insects.Ant;
import hive.insects.Insect;

import java.awt.*;

public class GameTileViewProbe {
    private static GameLogic gameLogic = GameLogic.getInstance();

    public static void main(String[] args) {
        gameLogic.newGameForTesting();
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("GameTileView Test");
        frame.setSize(700,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new GridLayout(1,1));

        TestJPanel mainPanel = new TestJPanel();

        Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
        mainPanel.setBorder(redBorder);

        frame.add(mainPanel);

        Insect testInsect = new Ant(gameLogic.getBluePlayer(), gameLogic);

        GameTile gameTile = new GameTile(testInsect);

        gameTile.setInitialized(true);
        gameTile.setState(TileStates.SELECTED);

        Coordinate c = new Coordinate(0,0);
        gameTile.setCoordinate(c);

        GameTileController<TestJPanel> gameTileController = new GameTileController<>(gameTile, mainPanel);


        // Center the frame on the screen and make it visible.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

