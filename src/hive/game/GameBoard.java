package hive.game;

import hive.GraphicLogger;
import hive.HiveLogger;
import hive.graphics.controllers.ModelListener;

import java.util.*;

import static java.lang.System.exit;

public class GameBoard {
    private static GameBoard instance;

    private ModelListener listener;

    private GameBoard() {
    }

    private HashMap<Coordinate, GameTile> boardMap = new HashMap<>();

    /**
     * Visszaadja az egyetlen létező GameBoard példányt.
     * Ha a program futása alatt egy sem lett kész, akkor létrehoz egyet.
     *
     * @return a GameBoard singleton példány.
     */
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
            notifyOnGameTileAdded(tile);
        }
    }

    /**
     * The total amount of tiles on the board.
     *
     * @return the amount of tiles on the board.
     */
    public int getSize() {
        return boardMap.size();
    }

    /**
     * Visszaadja válogatás nélkül az összes GameTile-t egy halmazként.
     *
     * @return a Tile-ok halmaza.
     */
    public Set<GameTile> getTileSet() {
        return new HashSet<>(boardMap.values());
    }

    /**
     * Visszaadja a játéktábla összes Inicializált Tile-jét egy halmazként.
     *
     * @return a tile-ok halmaza.
     */
    public Set<GameTile> getInitializedTileSet() {
        Set<GameTile> initializedTiles = new HashSet<>();
        for (GameTile tile : boardMap.values()) {
            if (tile.isInitialized()) {
                initializedTiles.add(tile);
            }
        }
        if (initializedTiles.isEmpty()) {
            HiveLogger.getLogger().error("A getInitializedTileSet() nem talált egyetlen inicializált Tile-t sem!");
        }

        return initializedTiles;
    }

    /**
     * Visszaadja a játéktábla összes Nem inicializált Tile-jét egy halmazként.
     *
     * @return a tile-ok halmaza.
     */
    public Set<GameTile> getUnInitializedTileSet() {
        Set<GameTile> uninitializedTiles = new HashSet<>();
        for (GameTile tile : boardMap.values()) {
            if (!tile.isInitialized()) {
                uninitializedTiles.add(tile);
            }
        }
        if (uninitializedTiles.isEmpty()) {
            HiveLogger.getLogger().error("A getUnInitializedTileSet() nem talált egyetlen nem inicializált Tile-t sem!");
        }

        return uninitializedTiles;
    }

    /**
     * Kiad egy inicializált tile-t a GameBoard-ból, egy kivételével.
     * A GameLogic wouldHiveBeConnected() metódusa miatt kell elsősorban.
     * <p></p>
     * Megjegyzés:
     * A visszatért érték mindig null lesz, ha a táblán egy rovar van, vagy ha játékban nincs
     * (aktív) rovar.
     *
     * @return null ha a tábla üres; vagy nem az, csak nincs inicializált tile a kivételen kívül
     * <br>chosenOne ha a tábla tartalmaz legalább egy inicializált tile-t, ami nem a kivétel
     */
    public GameTile getInitializedTile(GameTile exception) {
        if (boardMap.isEmpty()) {
            HiveLogger.getLogger().warn("getInitializedTile was called for an empty board!");
            return null;
        } else if (!hasGameTile(exception)) {
            HiveLogger.getLogger().warn("getInitializedTile was called with a non-existant exception!");
        }
        Set<GameTile> initializedTiles = getInitializedTileSet();
        if (initializedTiles.isEmpty()) {
            HiveLogger.getLogger().warn("getInitializedTile did not find any initialized tiles in a non-empty board!");
            return null;
        }
        initializedTiles.remove(exception);
        if (initializedTiles.isEmpty()) {
            HiveLogger.getLogger().debug("getInitializedTile did not find any init. tiles besides the exception.");
            return null;
        }
        GameTile chosenOne = null;
        for (GameTile tile : initializedTiles) {
            if (tile.isInitialized()) {
                chosenOne = tile;
                break;
            }
        }
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

    /**
     * Válogatás nélkül kitöröl minden tile-t.
     * Felér egy új játék kezdésével.
     */
    public void clear() {
        Iterator<GameTile> iterator = boardMap.values().iterator();
        while (iterator.hasNext()) {
            GameTile tile = iterator.next();
            iterator.remove(); // Safely remove the tile from the map
            tile.setState(TileStates.TERMINATED);
        }
        HiveLogger.getLogger().info("Board was cleared.");
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

    public void notifyOnGameTileAdded(GameTile tile) {
        if(listener!=null) {
            listener.onGameTileAdded(tile);
        }else {
            GraphicLogger.getLogger().error("GameBoard did not notify controller: null instance");
        }
    }

    public void addListener(ModelListener listener){
        this.listener=listener;
    }
}
