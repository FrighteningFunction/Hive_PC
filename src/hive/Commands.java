package hive;

import hive.game.GameLogic;
import hive.graphics.controllers.BoardController;
import hive.graphics.controllers.GamePanelController;
import hive.graphics.controllers.PlayerPanelController;
import hive.graphics.views.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Commands {
    private static final CardLayout mainCardLayout = HiveMain.getCardLayout();

    private Commands(){}

    public static void startNewGame(){
        GameLogic gameLogic = GameLogic.getInstance();
        GamePanel gamePanel = HiveMain.getGamePanel();
        new PlayerPanelController(gamePanel.getBlackPlayerPanelView(), gameLogic.getBlackPlayer());
        new PlayerPanelController(gamePanel.getWhitePlayerPanelView(), gameLogic.getWhitePlayer());
        new BoardController(gamePanel.getBoardView(), gameLogic.getBoard());
        new GamePanelController(gameLogic, gamePanel);

        gameLogic.newGame();

        mainCardLayout.show(HiveMain.getMainPanel(), "Game");
    }

    //todo: implementálás

    public static void loadGame(){
        throw new UnsupportedOperationException();
    }

    public static void saveGame(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(HiveMain.getMainPanel());

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            String directoryPath = selectedDirectory.getAbsolutePath();
            String filename = JOptionPane.showInputDialog("Enter the filename:");

            String nameToPass = directoryPath+'\\'+filename;

            try {
                HiveGameXMLWriter.writeGameState(GameLogic.getInstance(), nameToPass);
                MainAppLogger.getLogger().info("Output to XML successful.");
            }catch(IOException e){
                MainAppLogger.getLogger().error("Unsuccesful export to XML! {}", e.getMessage());
            }
        }
    }
}
