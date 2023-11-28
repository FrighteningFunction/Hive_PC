package org.graphics.controllers;

import org.game.GameLogic;
import org.game.GameTile;
import org.game.GraphicLogger;
import org.graphics.views.GamePanel;

public class GamePanelController implements ModelListener{
    GameLogic gameLogic;

    GamePanel gamePanel;

    public GamePanelController(GameLogic gameLogic, GamePanel gamePanel) {
        this.gameLogic = gameLogic;
        this.gamePanel = gamePanel;

        new PlayerPanelController(gamePanel.getBlackPlayerPanelView(), gameLogic.getBlackPlayer());
        new PlayerPanelController(gamePanel.getWhitePlayerPanelView(), gameLogic.getWhitePlayer());
        new BoardController();

        GraphicLogger.getLogger().info("GamePanelController created successfully.");
    }

    public void onModelChange() {
        if (gameLogic.getGameState() == GameLogic.GameState.TERMINATED) {
            gamePanel.spawnVictoryDialog(gameLogic.getWinner());
        }
    }

    @Override
    public void onGameTileAdded(GameTile tile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGameTileRemoved(GameTile tile) {
        throw new UnsupportedOperationException();
    }
}
