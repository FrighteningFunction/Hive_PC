package org.graphics.controllers;

import org.game.GameTile;
import org.graphics.views.GameTileView;

public class GameTileController{

    private GameTileView gameTileView;

    private GameTile tile;

    public GameTileController(GameTile tile, GameTileView gameTileView) {
        this.tile = tile;

        this.gameTileView = gameTileView;
    }

    public GameTileView getGameTileView() {
        return gameTileView;
    }

    public void onModelChange(){
        gameTileView.setInitialized(tile.isInitialized());
        gameTileView.setStates(tile.getState());
        gameTileView.setC(tile.getCoordinate());
        gameTileView.setInsect(tile.getInsect());

        gameTileView.repaint();
    }
}
