package hive.graphics.controllers;

import hive.game.GameBoard;
import hive.game.GameTile;
import hive.GraphicLogger;
import hive.graphics.views.BoardView;

public class BoardController implements ModelListener{
    private BoardView boardView;

    private GameBoard board;

    public BoardController(BoardView boardView, GameBoard board){
        this.boardView=boardView;
        this.board=board;
        board.addListener(this);
        GraphicLogger.getLogger().info("BoardController was created successfully.");
    }

    public BoardView getBoardView(){
        return boardView;
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
    public void onGameTileRemoved(GameTile tile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onResizeEvent() {
        throw new UnsupportedOperationException();
    }
}
