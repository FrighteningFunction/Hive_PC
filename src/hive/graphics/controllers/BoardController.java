package hive.graphics.controllers;

import hive.game.GameBoard;
import hive.game.GameTile;
import hive.GraphicLogger;
import hive.graphics.views.BoardView;

public class BoardController implements ModelListener{
    private final BoardView boardView;

    public BoardController(BoardView boardView, GameBoard board){
        this.boardView=boardView;
        board.addListener(this);
        GraphicLogger.getLogger().info("BoardController was created successfully.");
    }
    @Override
    public void onModelChange() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onGameTileAdded(GameTile tile) {

        new GameTileController<>(tile, boardView);

        boardView.revalidate();
        boardView.repaint();
    }

    @Override
    public void onResizeEvent() {
        throw new UnsupportedOperationException();
    }
}
