package org.graphics.controllers;

import org.game.GameTile;

public interface ModelListener {
    void onModelChange();

    void onGameTileAdded(GameTile tile);

    void onGameTileRemoved(GameTile tile);
}
