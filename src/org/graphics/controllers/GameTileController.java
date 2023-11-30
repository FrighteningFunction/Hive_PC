package org.graphics.controllers;

import org.game.Coordinate;
import org.game.GameTile;
import org.game.TileStates;
import org.graphics.views.GameTileView;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graphics.views.ViewUtil;

public class GameTileController<T extends JComponent> implements ModelListener {
    private Logger logger = LogManager.getLogger();

    private GameTileView gameTileView;

    private T container;

    private GameTile tile;


    public GameTileController(GameTile tile, T container) {
        this.tile = tile;

        Coordinate c = ViewUtil.refactorCoordinate(tile.getCoordinate(), container);

        this.gameTileView=new GameTileView(c);
        container.add(gameTileView);
        this.container = container;

        setUpGameTileView();

        gameTileView.addClickListener(new ClickListener());
        logger.info("GameTileController successfully created.");
    }

    private void setUpGameTileView(){
        gameTileView.setInitialized(tile.isInitialized());
        gameTileView.setStates(tile.getState());
        gameTileView.setC(ViewUtil.refactorCoordinate(tile.getCoordinate(), container));
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
