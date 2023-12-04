package hive.graphics.controllers;

import hive.graphics.views.GamePanel;
import hive.game.GameLogic;
import hive.game.GameTile;
import hive.GraphicLogger;
import hive.HiveMain;

public class GamePanelController implements ModelListener{
    private final GameLogic gameLogic;

    private final GamePanel gamePanel;

    public GamePanelController(GameLogic gameLogic, GamePanel gamePanel) {
        this.gameLogic = gameLogic;
        this.gamePanel = gamePanel;
        gameLogic.addListener(this);

        GraphicLogger.getLogger().info("GamePanelController created successfully.");
    }

    public void onModelChange() {
        if (gameLogic.getGameState() == GameLogic.GameState.TERMINATED) {
            gamePanel.spawnVictoryDialog(gameLogic.getWinner());
            GraphicLogger.getLogger().info("VicoryDialog spawned.");

            HiveMain.getCardLayout().show(HiveMain.getMainPanel(), "Menu");
        }
    }

    @Override
    public void onGameTileAdded(GameTile tile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onResizeEvent() {
        throw new UnsupportedOperationException();
    }
}
