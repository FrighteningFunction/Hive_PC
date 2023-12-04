package hive.graphics.controllers;

import hive.GraphicLogger;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ResizeListener extends ComponentAdapter {
    private final List<ModelListener> listeners;

    private final String containerName;

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
            GraphicLogger.getLogger().info("Listeners for resize-events of {} got notified properly.", containerName);
        }
    }
}
