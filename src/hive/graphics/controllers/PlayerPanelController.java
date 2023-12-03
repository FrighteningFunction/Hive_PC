package hive.graphics.controllers;

import hive.game.GraphicLogger;
import hive.game.Player;
import hive.game.GameTile;
import hive.graphics.views.PlayerPanelView;

public class PlayerPanelController implements ModelListener {
    private Player p;

    private PlayerPanelView playerPanelView;

    public PlayerPanelController(PlayerPanelView playerPanelView, Player p) {
        this.p = p;
        this.playerPanelView = playerPanelView;
        p.addListener(this);

        GraphicLogger.getLogger().info("PlayerPanelController for {} was created", p.getColor());
    }

    @Override
    public void onModelChange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGameTileAdded(GameTile tile) {
        new GameTileController<>(tile, playerPanelView);

        playerPanelView.revalidate();
        playerPanelView.repaint();
    }

    @Override
    public void onGameTileRemoved(GameTile tile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onResizeEvent() {
        throw new UnsupportedOperationException();
    }
}
