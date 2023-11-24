package org.game;

import java.util.HashMap;

import static java.lang.System.exit;

public class GameBoard {
    private static GameBoard  instance;

    private GameBoard(){}
    private static HashMap<Coordinate, GameTile> boardMap = new HashMap<>();

    public static GameBoard getInstance(){
        if(instance==null){
            instance = new GameBoard();
        }
        return instance;
    }

    public void addGameTile(GameTile tile) throws DoubleTileException{
        if(boardMap.containsKey(tile.getCoordinate())){
            throw  new DoubleTileException();
        }else{
            boardMap.put(tile.getCoordinate(), tile);
        }
    }

    public void removeGameTile(GameTile tile){
        if (boardMap.containsKey(tile.getCoordinate())) {
            boardMap.remove(tile.getCoordinate());
        }else{
            HiveLogger.getLogger().warn("Requested removable GameTile not found!");
        }
    }

    public boolean hasGameTile(GameTile tile){
        return boardMap.containsKey(tile.getCoordinate());
    }

    public void clear(){
        boardMap.clear();
    }

    /**
     * Visszatér az irány invertáltjával.
     * Ez a hexatile túlsó oldalát jelenti.
     * @param direction Egy irány: 0,1,2,3,4,5
     * @return az irány invertáltja
     */
    public static int invertDirection(int direction){
        return (direction+3)%6;
    }

    /**
     * Összeköti a hívó játékmezőt a megadott koordinátáknál elhelyezkedő gametile-al a megadott irányban.
     * Ha nincs mit összekötni (ak. az adott koordinátánál nincs GameTile) akkor nem csinál semmit).
     * Vigyázat: az irány a hívó irányát jelöli!
     * @param caller a hívó GameTile
     * @param atCoordinate az összekötendő GameTile
     * @param atDirection milyen irányban található a koordináta a hívóhoz képest? (0,1,2,3,4,5)
     */
    public void linkTile(GameTile caller, Coordinate atCoordinate, int atDirection){
        if(atDirection<0 || atDirection>5) {
            HiveLogger.getLogger().error("FATAL: invalid direction at linkTile");
            exit(1);
        }
        if(boardMap.containsKey(atCoordinate)){
            GameTile linkable = boardMap.get(atCoordinate);
            caller.setNeighbour(linkable,atDirection);
            linkable.setNeighbour(caller, invertDirection(atDirection));
        }else{
            HiveLogger.getLogger().info("Egy irányban talált a linkTile szomszédot.");
        }
    }
}
