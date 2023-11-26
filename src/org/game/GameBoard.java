package org.game;

import java.util.*;

import static java.lang.System.exit;

public class GameBoard {
    private static GameBoard instance;

    private GameBoard() {
    }

    private static HashMap<Coordinate, GameTile> boardMap = new HashMap<>();

    public static GameBoard getInstance() {
        HiveLogger.getLogger().debug("GameBoard getInstance called!");
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }

    public void addGameTile(GameTile tile) throws DoubleTileException {
        if (boardMap.containsKey(tile.getCoordinate())) {
            throw new DoubleTileException();
        } else {
            boardMap.put(tile.getCoordinate(), tile);
        }
    }

    /**
     * The total amount of tiles on the board.
     * @return the amount of tiles on the board.
     */
    public int getSize(){
        return boardMap.size();
    }

    /**
     * Visszaadja a játéktábla összes Inicializált Tile-jét egy halmazként.
     * @return a tile-ok halmaza.
     */
    public Set<GameTile> getInitializedTileSet(){
       Set<GameTile> initializedTiles = new HashSet<>();
       for(GameTile tile : boardMap.values()){
           if(tile.isInitialized()){
               initializedTiles.add(tile);
           }
       }
       if(initializedTiles.isEmpty()){
           HiveLogger.getLogger().error("A getInitializedTileSet() nem talált egyetlen inicializált Tile-t sem!");
       }

       return initializedTiles;
    }

    /**
     * Visszaadja a játéktábla összes Nem inicializált Tile-jét egy halmazként.
     * @return a tile-ok halmaza.
     */
    public Set<GameTile> getUnInitializedTileSet(){
        Set<GameTile> uninitializedTiles = new HashSet<>();
        for(GameTile tile : boardMap.values()){
            if(!tile.isInitialized()){
                uninitializedTiles.add(tile);
            }
        }
        if(uninitializedTiles.isEmpty()){
            HiveLogger.getLogger().error("A getUnInitializedTileSet() nem talált egyetlen nem inicializált Tile-t sem!");
        }

        return uninitializedTiles;
    }

    /**
     * Kiad egy inicializált tile-t a GameBoard-ból.
     *
     * @return null ha a tábla üres; vagy nem az, csak nincs inicializált tile (ez utóbbi fatális hiba)
     * chosenOne ha a tábla tartalmaz legalább egy inicializált tile-t
     */
    public GameTile getInitializedTile() {
        if (boardMap.isEmpty()) {
            HiveLogger.getLogger().warn("getRandomTile was called for an empty board!");
            return null;
        }
        GameTile chosenOne = null;
        for (GameTile tile : boardMap.values()) {
            if (tile.isInitialized()) {
                chosenOne = tile;
                break;
            }
        }
        if (chosenOne == null) HiveLogger.getLogger().fatal("There are only uninitialized tiles on the GameBoard!");
        return chosenOne;
    }

    public void removeGameTile(GameTile tile) {
        if (boardMap.containsKey(tile.getCoordinate())) {
            boardMap.remove(tile.getCoordinate());
        } else {
            HiveLogger.getLogger().warn("Requested removable GameTile not found!");
        }
    }

    public boolean hasGameTile(GameTile tile) {
        return boardMap.containsKey(tile.getCoordinate());
    }

    public boolean hasGameTile(Coordinate c) {
        return boardMap.containsKey(c);
    }

    public void clear() {
        boardMap.clear();
    }

    /**
     * Visszatér az irány invertáltjával.
     * Ez a hexatile túlsó oldalát jelenti.
     *
     * @param direction Egy irány: 0,1,2,3,4,5
     * @return az irány invertáltja
     */
    public static int invertDirection(int direction) {
        return (direction + 3) % 6;
    }

    /**
     * Összeköti a hívó játékmezőt a megadott koordinátáknál elhelyezkedő gametile-al a megadott irányban.
     * Ha nincs mit összekötni (ak. az adott koordinátánál nincs GameTile) akkor nem csinál semmit).
     * Vigyázat: az irány a hívó irányát jelöli!
     *
     * @param caller       a hívó GameTile
     * @param atCoordinate az összekötendő GameTile
     * @param atDirection  milyen irányban található a koordináta a hívóhoz képest? (0,1,2,3,4,5)
     */
    public void linkTile(GameTile caller, Coordinate atCoordinate, int atDirection) {
        if (atDirection < 0 || atDirection > 5) {
            HiveLogger.getLogger().fatal("invalid direction at linkTile");
            exit(1);
        }
        if (boardMap.containsKey(atCoordinate)) {
            GameTile linkable = boardMap.get(atCoordinate);
            caller.setNeighbour(linkable, atDirection);
            linkable.setNeighbour(caller, invertDirection(atDirection));
        }
    }
}
