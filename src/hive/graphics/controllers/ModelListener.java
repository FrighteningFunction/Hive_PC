package hive.graphics.controllers;

import hive.game.GameTile;

/**
 * Az összes a játékban előforduló kontrollernek ezt a felületet kell használnia.
 */
public interface ModelListener {
    void onModelChange();

    void onGameTileAdded(GameTile tile);

    void onResizeEvent();
}
