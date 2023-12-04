package hive.graphics.controllers;

import hive.GraphicLogger;
import hive.game.TileStates;
import hive.graphics.views.GameTileView;
import hive.graphics.views.TileView;
import hive.game.Coordinate;
import hive.game.GameTile;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import hive.graphics.views.ViewUtil;

public class GameTileController<T extends JComponent & TileView> implements ModelListener {
    private final Logger logger = LogManager.getLogger();

    private final GameTileView gameTileView;

    private final GameTile tile;

    private final T container;


    public GameTileController(GameTile tile, T container) {
        this.tile = tile;

        Coordinate c = ViewUtil.refactorCoordinate(tile.getCoordinate(), container);

        this.gameTileView = new GameTileView(c);
        this.container = container;

        container.add(gameTileView);
        tile.addListener(this);

        container.addListener(this);

        setUpGameTileView();

        gameTileView.addClickListener(new ClickListener());

        logger.info("GameTileController successfully created.");
    }

    private void setUpGameTileView() {
        gameTileView.setInitialized(tile.isInitialized());
        gameTileView.setState(tile.getState());
        gameTileView.setC(ViewUtil.refactorCoordinate(tile.getCoordinate(), container));
        gameTileView.setInsect(tile.getInsect());

        if (tile.isInitialized()) {
            gameTileView.setColor(tile.getInsect().color);
        }

        gameTileView.repaint();
        container.revalidate();
        container.repaint();
    }

    @Override
    public void onModelChange() {
        if (tile.getState() == TileStates.TERMINATED) {
            container.remove(gameTileView);
            container.revalidate();
            container.repaint();
            GraphicLogger.getLogger().info("GameTileView at ({},{}) in {} was removed successfully.", gameTileView.getX(), gameTileView.getY(), container);
        } else {
            setUpGameTileView();

            gameTileView.repaint();
            logger.info("GameTileView at ({},{}) got updated.", gameTileView.getX(), gameTileView.getY());
        }
    }

    @Override
    public void onGameTileAdded(GameTile tile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onResizeEvent() {
        setUpGameTileView();
    }

    class ClickListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            tile.gotClicked();
            logger.info("Tile at ({}, {}) got clicked.", gameTileView.getX(), gameTileView.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // Erre nincs szükség
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Erre nincs szükség
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // Erre nincs szükség
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Erre nincs szükség
        }
    }
}
