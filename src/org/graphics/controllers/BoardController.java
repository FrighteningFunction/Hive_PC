package org.graphics.controllers;

import org.game.Coordinate;
import org.game.GameBoard;
import org.game.GameTile;
import org.game.GraphicLogger;
import org.graphics.views.BoardView;
import org.graphics.views.GameTileView;

public class BoardController implements ModelListener{
    private BoardView boardView;

    private GameBoard board;

    public BoardController(BoardView boardView, GameBoard board){
        this.boardView=boardView;
        this.board=board;
        board.addListener(this);
        GraphicLogger.getLogger().info("BoardController was created successfully.");
    }

    @Override
    public void onModelChange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGameTileAdded(GameTile tile) {
        Coordinate viewCord = tile.getCoordinate();

        GameTileView gtv = new GameTileView();

        tile.addGameTileController(new GameTileController<BoardView>(tile, boardView, gtv));

        boardView.add(gtv);

        boardView.revalidate();
        boardView.repaint();
    }

    @Override
    public void onGameTileRemoved(GameTile tile) {
        throw new UnsupportedOperationException();
    }
}
