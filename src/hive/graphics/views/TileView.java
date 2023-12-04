package hive.graphics.views;

import hive.graphics.controllers.ModelListener;

/**
 * A ResizeEvent kezelése érdekében ezt az érintett paneleknek
 * implementálniuk kell.
 */
public interface TileView {
    void addListener(ModelListener listener);
}
