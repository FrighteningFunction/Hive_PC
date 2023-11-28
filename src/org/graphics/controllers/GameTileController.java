package org.graphics.controllers;

import org.game.GameTile;
import org.game.TileStates;
import org.graphics.views.GameTileView;
import org.graphics.views.NullPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameTileController<T extends JComponent & NullPanel> implements ModelListener {
    private Logger logger = LogManager.getLogger();

    private GameTileView gameTileView;

    private T container;

    private GameTile tile;

    //todo: Nem kellene kapásból itt hozzáadni a konstruktorban kapott ui komponenst a konténerhez?
    public GameTileController(GameTile tile, T container, GameTileView gameTileView) {
        this.tile = tile;
        this.gameTileView=gameTileView;
        this.container = container;

        setUpGameTileView();

        gameTileView.addClickListener(new ClickListener());
        logger.info("GameTileController successfully created.");
    }

    private void setUpGameTileView(){
        gameTileView.setInitialized(tile.isInitialized());
        gameTileView.setStates(tile.getState());
        gameTileView.setC(container.refactorCoordinate(tile.getCoordinate()));
        gameTileView.setInsect(tile.getInsect());
    }

    public GameTileView getGameTileView() {
        return gameTileView;
    }

    @Override
    public void onModelChange() {
        if (tile.getState() == TileStates.TERMINATED) {
            container.remove(gameTileView);
        } else {

            setUpGameTileView();

            gameTileView.repaint();
        }
    }

    @Override
    public void onGameTileAdded(GameTile tile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGameTileRemoved(GameTile tile) {
        throw new UnsupportedOperationException();
    }

    class ClickListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            tile.gotClicked();
            logger.info("Tile at x: {} y: {} got clicked.", tile.getCoordinate().getX(), tile.getCoordinate().getY());
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
