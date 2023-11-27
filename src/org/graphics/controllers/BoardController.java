package org.graphics.controllers;

import org.game.Coordinate;
import org.game.GameTile;
import org.game.GraphicLogger;
import org.graphics.views.BoardView;
import org.graphics.views.GameTileView;

public class BoardController{
    BoardView boardView;

    public BoardController(){
        boardView=BoardView.getInstance();
    }

    public void onGameTileAdded(GameTile tile) {
        Coordinate viewCord = tile.getCoordinate();
        viewCord=boardView.refactorCoordinate(viewCord);

        GameTileView gtv = new GameTileView(viewCord);

        tile.addGameTileController(new GameTileController(tile, gtv));

        boardView.add(gtv);
    }

    public void onGameTileRemoved(GameTile tile) {
        if(tile.getGameTileController()==null){
            GraphicLogger.getLogger().fatal("No Controller instance was found.");
            System.exit(1);
        }
        GameTileController controller=tile.getGameTileController();
        boardView.remove(controller.getGameTileView());
    }
}
