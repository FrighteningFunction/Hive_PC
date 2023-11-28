package org.graphics.controllers;

import org.game.GameTile;
import org.game.GraphicLogger;
import org.game.Player;
import org.graphics.views.GameTileView;
import org.graphics.views.PlayerPanelView;

public class PlayerPanelController implements ModelListener{
    private Player p;

    private PlayerPanelView playerPanelView;

    public PlayerPanelController(PlayerPanelView playerPanelView, Player p){
        this.p=p;
        this.playerPanelView=playerPanelView;

        GraphicLogger.getLogger().info("PlayerPanelController for {} was created", p.getColor());
    }

    @Override
    public void onModelChange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGameTileAdded(GameTile tile){
        GameTileView gameTileView = new GameTileView(playerPanelView.refactorCoordinate(tile.getCoordinate()));
        new GameTileController<>(tile, playerPanelView, gameTileView);
        playerPanelView.add(gameTileView);
    }

    @Override
    public void onGameTileRemoved(GameTile tile) {
        throw new UnsupportedOperationException();
    }
}
