package org.graphics.controllers;

import org.game.GameLogic;
import org.game.TileStates;
import org.graphics.views.Game;

public class GameController {
    GameLogic gameLogic;

    Game game;

    public GameController(GameLogic gameLogic, Game game) {
        this.gameLogic = gameLogic;
        this.game = game;
    }

    public void onModelChange() {
        if (gameLogic.getGameState() == GameLogic.GameState.TERMINATED) {
            game.spawnVictoryDialog(gameLogic.getWinner());
        }
    }
}
