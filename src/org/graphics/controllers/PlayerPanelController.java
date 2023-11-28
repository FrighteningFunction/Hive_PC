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
        p.addListener(this);

        GraphicLogger.getLogger().info("PlayerPanelController for {} was created", p.getColor());
    }

    @Override
    public void onModelChange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGameTileAdded(GameTile tile){
        GameTileView gameTileView = new GameTileView();
        new GameTileController<>(tile, playerPanelView, gameTileView);
        playerPanelView.add(gameTileView);

        playerPanelView.revalidate();
        playerPanelView.repaint();
    }

    @Override
    public void onGameTileRemoved(GameTile tile) {
        throw new UnsupportedOperationException();
    }
}
