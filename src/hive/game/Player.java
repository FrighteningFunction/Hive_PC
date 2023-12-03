package hive.game;

import hive.graphics.controllers.ModelListener;
import hive.insects.*;
import org.insects.*;
import hive.insects.Queen;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Queen queen = null;

    private final GameLogic gameLogic;

    public final HiveColor color;

    private final List<Insect> insects = new ArrayList<>();

    private final List<GameTile> insectHolders = new ArrayList<>();

    private ModelListener listener;

    private static final double SPACE_BETWEEN_TILES = 10;

    Player(HiveColor color, GameLogic gameLogic) {
        this.color = color;
        this.gameLogic = gameLogic;

        HiveLogger.getLogger().info("{} Player created successfully", color);
    }

    /**
     * Inicializálja az adott játékost, így készen áll új játékra.
     * Felér egy resettel.
     */
    public void initPlayer() {
        queen = new Queen(this, gameLogic);
        insects.clear();

        insects.add(queen);

        insects.add(new Ant(this, gameLogic));
        insects.add(new Ant(this, gameLogic));
        insects.add(new Ant(this, gameLogic));

        insects.add(new Beetle(this, gameLogic));
        insects.add(new Beetle(this, gameLogic));

        insects.add(new Grasshopper(this, gameLogic));
        insects.add(new Grasshopper(this, gameLogic));
        insects.add(new Grasshopper(this, gameLogic));

        insects.add(new Spider(this, gameLogic));
        insects.add(new Spider(this, gameLogic));

        //jobbról kezdve felváltva balra-jobbra feltöltjük a játékos készletét
        int i = 0;
        int leftNum = 1;
        int rightNum = 1;
        for (Insect insect : insects) {
            GameTile gameTile = new GameTile(insect);
            Coordinate c;
            if (i == 0) {
                //az elsőt középre tesszük le
                c = new Coordinate(i * GameTile.getTileRadius() * 2 + SPACE_BETWEEN_TILES, 0);
            } else {
                //balra tesszük
                if (i % 2 == 0) {
                    c = new Coordinate(-1 * leftNum * GameTile.getTileRadius() * 2 + SPACE_BETWEEN_TILES, 0);
                    leftNum++;
                } else {
                    //jobbra tesszük
                    c = new Coordinate(rightNum * GameTile.getTileRadius() * 2 + SPACE_BETWEEN_TILES, 0);
                    rightNum++;
                }
            }
            i++;
            gameTile.setCoordinate(c);
            insectHolders.add(gameTile);

            notifyOnAdd(gameTile);
        }

        HiveLogger.getLogger().info("{} Player initialized successfully", color);
    }

    public void removeGameTile(GameTile tile) {
        if (insectHolders.remove(tile)) {
            tile.setState(TileStates.TERMINATED); //ezzel a GameTileController automatikusan kitörli majd
            HiveLogger.getLogger().info("placeholder tile from {} was successfully deleted.", color);
        } else {
            HiveLogger.getLogger().fatal("placeholder tile specified was not found for {} player", color);
        }
    }

    public void removeInsect(Insect insect) {
        if (!insects.remove(insect)) {
            HiveLogger.getLogger().error("A nonexistent insect was to be removed from Player");
        }
    }

    public int getNeighboursOfQueen() {
        return queen.getTotalNeighbours();
    }

    public boolean isQueenDown() {
        return !insects.contains(queen);
    }

    public Queen getQueen() {
        return queen;
    }

    public HiveColor getColor() {
        return color;
    }

    public void addListener(ModelListener listener) {
        this.listener = listener;
    }

    public List<Insect> getInsects() {
        return insects;
    }

    public List<GameTile> getInsectHolders() {
        return insectHolders;
    }

    public void notifyListeners() {
        if (listener != null) {
            listener.onModelChange();
        } else {
            GraphicLogger.getLogger().error("Player did not notify controller on notifyListeners: null instance");
        }
    }

    private void notifyOnAdd(GameTile tile) {
        if (listener != null) {
            listener.onGameTileAdded(tile);
        } else {
            GraphicLogger.getLogger().error("Player did not notify controller on add: null instance");
        }
    }
}
