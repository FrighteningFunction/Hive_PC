package org.game;

import java.util.HashMap;

import static java.lang.System.exit;
import static java.lang.System.in;

public class GameBoard {
    private static GameBoard  instance;

    private GameBoard(){}
    private static HashMap<Coordinate, GameTile> board = new HashMap<>();

    private static GameBoard getInstance(){
        if(instance==null){
            instance = new GameBoard();
        }
        return instance;
    }

    public static void addGameTile(GameTile tile, Coordinate coordinate) throws DoubleTileException{
        if(board.containsKey(coordinate)){
            throw  new DoubleTileException();
        }else{
            board.put(coordinate, tile);
        }
    }

    /**
     * todo: implement
     * @param coordinate
     */
    public static void removeGameTile(Coordinate coordinate){}
}
