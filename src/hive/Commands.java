package hive;

import hive.game.GameLogic;
import hive.graphics.controllers.BoardController;
import hive.graphics.controllers.GamePanelController;
import hive.graphics.controllers.PlayerPanelController;
import hive.graphics.views.GamePanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Commands {
    private static final CardLayout mainCardLayout = HiveMain.getCardLayout();

    private Commands() {
    }

    public static void startNewGame() {
        GameLogic gameLogic = GameLogic.getInstance();
        GamePanel gamePanel = HiveMain.getGamePanel();
        new PlayerPanelController(gamePanel.getBlackPlayerPanelView(), gameLogic.getBluePlayer());
        new PlayerPanelController(gamePanel.getWhitePlayerPanelView(), gameLogic.getOrangePlayer());
        new BoardController(gamePanel.getBoardView(), gameLogic.getBoard());
        new GamePanelController(gameLogic, gamePanel);

        gameLogic.newGame();

        mainCardLayout.show(HiveMain.getMainPanel(), "Game");
    }

    //todo: implementálás

    public static void loadGame() {


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(HiveMain.getMainPanel());

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            GameLogic gameLogic = GameLogic.getInstance();
            GamePanel gamePanel = HiveMain.getGamePanel();

            try {
                gameLogic.clearGame();

                new PlayerPanelController(gamePanel.getBlackPlayerPanelView(), gameLogic.getBluePlayer());
                new PlayerPanelController(gamePanel.getWhitePlayerPanelView(), gameLogic.getOrangePlayer());
                new BoardController(gamePanel.getBoardView(), gameLogic.getBoard());
                new GamePanelController(gameLogic, gamePanel);

                HiveGameXMLReader.readGameState(filePath);

                //Ezt azért adjuk hozzá, mert a readGameState csak inicializált tile-okat "jegyez" meg,
                //vagyis ha új játékot mentene el, akkor nem lesz középen egy inicializálatlan tile sem.
                //new GameTile(gameLogic.getBoard(), new Coordinate(0, 0));

                gameLogic.setGameState(GameLogic.GameState.RUNNING);
                gameLogic.getOrangePlayer().initializePlaceHolders();
                gameLogic.getBluePlayer().initializePlaceHolders();

                MainAppLogger.getLogger().info("Game loaded successfully.\n" +
                        "###########################");
            } catch (Exception e) {
                MainAppLogger.getLogger().error("Error when loading XML file: {}; loading new Game instead", e.getMessage());
                Commands.startNewGame();
            }
            mainCardLayout.show(HiveMain.getMainPanel(), "Game");
            HiveMain.getMainPanel().revalidate();
            HiveMain.getMainPanel().repaint();
        }
    }

    public static void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(HiveMain.getMainPanel());

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            String directoryPath = selectedDirectory.getAbsolutePath();
            String filename = JOptionPane.showInputDialog("Enter the filename:");

            String nameToPass = directoryPath + '\\' + filename;

            try {
                HiveGameXMLWriter.writeGameState(GameLogic.getInstance(), nameToPass);
                MainAppLogger.getLogger().info("Output to XML successful.");
            } catch (IOException e) {
                MainAppLogger.getLogger().error("Unsuccesful export to XML! {}", e.getMessage());
            }
        }
    }
}
