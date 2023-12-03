package org.graphics.controllers;

import org.game.GraphicLogger;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ResizeListener extends ComponentAdapter {
    List<ModelListener> listeners = null;

    String containerName = null;

    public ResizeListener(List<ModelListener> listeners, String containerName) {
        this.listeners = listeners;
        this.containerName = containerName;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (listeners.isEmpty()) {
            GraphicLogger.getLogger().error("ResizeListener of {} got fired but found 0 listeners!", containerName);
        } else {
            for (ModelListener listener : listeners) {
                listener.onResizeEvent();
            }
            GraphicLogger.getLogger().info("Listeners for resizeevents of {} got notified properly.", containerName);
        }
    }
}
