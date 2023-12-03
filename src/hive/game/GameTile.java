package hive.game;

import hive.graphics.controllers.ModelListener;
import hive.insects.Insect;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static java.lang.System.exit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameTile {

    private final Logger logger = LogManager.getLogger();
    private List<ModelListener> listeners = new ArrayList<>();

    private TileStates state;

    private static final double TILE_RADIUS = 50;

    //the height of the tile
    private static final double HEIGHT = 2 * sqrt(pow(TILE_RADIUS, 2) - pow(TILE_RADIUS, 2) / 4);

    //the degree of the directions of the neighbouring hexatiles
    //in degrees
    private static final double DIR = PI / 6;

    private boolean initialized;

    //todo: ezt megnézni
    private final boolean isPlaceHolder;

    private GameBoard board;

    private Insect insect;

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    private Coordinate coordinate;
    private GameTile[] neighbours = new GameTile[6];

    /**
     * Ez a konstruktor intézi az új GameTile felvételt.
     * Mind a 6 oldalon csekkolja, hogy össze kell-e kötni más Tile-okkal, és
     * ha kell, össze is köti velük
     * Ekkor a GameTile nem inicializált!
     *
     * @param b a játéktábla.
     * @param c a létrehozandó GameTile koordinátája.
     */
    public GameTile(GameBoard b, Coordinate c) {
        initialized = false;
        isPlaceHolder = false;
        insect = null;
        state = TileStates.UNSELECTED;
        this.board = b;
        this.coordinate = c;
        for (int i = 0; i < 6; i++) {
            board.linkTile(this, getCoordinateOfNeighbourAt(i), i);
        }
        try {
            board.addGameTile(this);
        } catch (DoubleTileException e) {
            logger.error("A new tile was created at an existing coordinate.");
        }
    }

    /**
     * A még inicializálatlan rovarok tárolására használandó
     * tile-ok konstruktora.
     *
     * @param insect a rovar, amit tartalmaznia kell.
     */
    public GameTile(Insect insect) {
        neighbours = null;
        isPlaceHolder = true;
        initialized = true;
        state = TileStates.UNSELECTED;
        coordinate = null;
        this.insect = insect;
        insect.setLocation(this);
    }

    public static double getHexaTileHeight() {
        return HEIGHT;
    }

    public static double getTileRadius() {
        return TILE_RADIUS;
    }

    public static double getDir() {
        return DIR;
    }

    public TileStates getState() {
        return state;
    }

    public void setState(TileStates val) {
        state = val;
        notifyListeners();
    }

    /**
     * Kitöröl a játékból egy tile-t.
     * Kizárólag inicializálatlan (ak. üres) tile-ok törölhetők ki a játék közben. Egyébként fatális veszély lép fel.
     * Az összes rá mutató szomszéd-linket is kitörli.
     */
    public void deleteGameTileFromBoard() {
        if (initialized) {
            logger.fatal("deleteGameTile call to an initialized GameTile!");
        } else {

            board.removeGameTile(this);
            for (int i = 0; i < 6; i++) {
                if (neighbours[i] == null) {
                    logger.info("At a direction the blank tile found no neighbour.");
                } else {
                    neighbours[i].removeNeighbour(GameBoard.invertDirection(i));
                }
            }
            state = TileStates.TERMINATED;
            notifyListeners();
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public GameTile getNeighbour(int direction) {
        return neighbours[direction];
    }

    public Insect getInsect() {
        return insect;
    }

    public void setInsect(Insect insect) {
        this.insect = insect;
        notifyListeners();
    }

    public void removeNeighbour(int direction) {
        neighbours[direction] = null;
    }

    public void setNeighbour(GameTile neighbour, int direction) {
        if (neighbour == null) {
            logger.warn("null GameTile-t kapott a setNeighbour");
        }
        neighbours[direction] = neighbour;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean val) {
        initialized = val;
        logger.warn("GameTile setInitialized metódusa meghívva!");
        notifyListeners();
    }

    /**
     * Visszaadja, hogy az adott tile árva-e.
     * Egy tile akkor árva, ha nem létezik inicializált szomszédja.
     * Működés szerint a játékban ilyen nem létezhet.
     *
     * @return true, ha árva, hamis, ha nem az.
     */
    public boolean isOrphan() {
        for (int i = 0; i < 6; i++) {
            if (neighbours[i] != null && neighbours[i].isInitialized()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Visszaadja, hogy a paraméterként megadott Tile eltávolítása esetén
     * az adott tile árvává válna-e.
     *
     * @param tile az eltávolítandó tile
     * @return true, ha árvává válna, false, ha nem
     */
    public boolean wouldBeOrphan(GameTile tile) {
        for (int i = 0; i < 6; i++) {
            if (neighbours[i] != null && !neighbours[i].equals(tile) && neighbours[i].isInitialized()) {
                return false;
            }
        }
        return true;
    }

    public Coordinate getCoordinateOfNeighbourAt(int direction) {
        double x1 = coordinate.getX();
        double y1 = coordinate.getY();
        double x2 = 0;
        double y2 = 0;

        switch (direction) {
            case 0:
                x2 = x1;
                y2 = y1 + HEIGHT;
                break;
            case 1:
                x2 = x1 + HEIGHT * cos(DIR);
                y2 = y1 + HEIGHT * sin(DIR);
                break;
            case 2:
                x2 = x1 + HEIGHT * cos(DIR);
                y2 = y1 - HEIGHT * sin(DIR);
                break;
            case 3:
                x2 = x1;
                y2 = y1 - HEIGHT;
                break;
            case 4:
                x2 = x1 - HEIGHT * cos(DIR);
                y2 = y1 - HEIGHT * sin(DIR);
                break;
            case 5:
                x2 = x1 - HEIGHT * cos(DIR);
                y2 = y1 + HEIGHT * sin(DIR);
                break;
            default:
                logger.fatal("Fatal: invalid direction got at coordinate calculation");
                exit(1);
        }
        return new Coordinate(x2, y2);
    }

    /**
     * Inicializálja a GameTile-t, vagyis ezen a ponton minden szomszédja létezni fog,
     * benne magában pedig bogárnak kell lennie.
     */
    public void initialize(Insect insect) {
        if (initialized) {
            logger.fatal("GameTile already initialized.");
            exit(1);
        } else {
            initialized = true;
            this.insect = insect;
            for (int i = 0; i < 6; i++) {
                if (neighbours[i] == null) {
                    neighbours[i] = new GameTile(board, getCoordinateOfNeighbourAt(i));
                    board.linkTile(this, neighbours[i].getCoordinate(), i);
                } else {
                    logger.info("At a direction there already was a Tile.");
                }
            }
        }
        notifyListeners();
    }

    /**
     * Uninicializálja a tile-t: vagyis a rovart eltávolítja belőle, valamint az összes olyan szomszédját kitörli,
     * amely így árvává vált.
     */
    public void uninitialize() {
        if (!initialized) {
            logger.fatal("An already unitialized tile was to be uninitialized!");
        } else {
            initialized = false;
            insect = null;
            for (int i = 0; i < 6; i++) {
                if (!this.getNeighbour(i).isInitialized() && this.getNeighbour(i).isOrphan()) {
                    this.getNeighbour(i).deleteGameTileFromBoard();
                } else {
                    logger.info("A neighbour was not an orphan while uninitialize, so was not deleted");
                }
            }
        }
        notifyListeners();
    }

    public void gotClicked() {
        GameLogic.getInstance().clickedTile(this);
        notifyListeners();
    }

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        if (!listeners.isEmpty()) {
            for (ModelListener l : listeners) {
                l.onModelChange();
            }
        } else {
            GraphicLogger.getLogger().error("GameTile did not notify listener(s) : no listeners!");
        }
    }
}
