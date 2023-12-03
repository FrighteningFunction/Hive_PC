package hive.graphics.controllers;

import hive.game.GameTile;

public interface ModelListener {
    void onModelChange();

    void onGameTileAdded(GameTile tile);

    void onGameTileRemoved(GameTile tile);

    void onResizeEvent();
}
