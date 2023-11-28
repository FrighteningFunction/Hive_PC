package org.graphics.controllers;

import jdk.dynalink.linker.GuardedInvocationTransformer;
import org.game.Coordinate;
import org.game.GameTile;
import org.game.GraphicLogger;
import org.graphics.views.BoardView;
import org.graphics.views.GameTileView;

public class BoardController implements ModelListener{
    BoardView boardView;

    public BoardController(){
        boardView=BoardView.getInstance();
        GraphicLogger.getLogger().info("BoardController was created successfully.");
    }

    @Override
    public void onModelChange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGameTileAdded(GameTile tile) {
        Coordinate viewCord = tile.getCoordinate();
        viewCord=boardView.refactorCoordinate(viewCord);

        GameTileView gtv = new GameTileView(viewCord);

        tile.addGameTileController(new GameTileController<BoardView>(tile, boardView, gtv));

        boardView.add(gtv);
    }

    @Override
    public void onGameTileRemoved(GameTile tile) {
        throw new UnsupportedOperationException();
    }
}
