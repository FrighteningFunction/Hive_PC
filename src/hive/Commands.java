package hive;

import hive.game.GameLogic;
import hive.graphics.controllers.BoardController;
import hive.graphics.controllers.GamePanelController;
import hive.graphics.controllers.PlayerPanelController;
import hive.graphics.views.GamePanel;

import java.awt.*;

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
        throw new UnsupportedOperationException();
    }
}
