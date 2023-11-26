package org.game;

import java.util.HashSet;
import java.util.Set;

public class GameLogic {
    private static GameLogic instance = null;

    private int turns;

    private Player whitePlayer;

    private Player blackPlayer;

    private GameBoard board;

    private GameLogic() {
        turns = 1;
        whitePlayer = new Player(HiveColor.WHITE);
        blackPlayer = new Player(HiveColor.BLACK);
        board = GameBoard.getInstance();
        board.clear();
    }

    /**
     * Visszaadja az éppen aktuális GameLogic példányt.
     * Vigyázat, első alkalommal nem generál példányt:
     * először a newGame() statikus metódust kell ehhez meghívni.
     *
     * @return aktuális játékpéldány (GameLogic)
     */
    public static GameLogic getInstance() {
        HiveLogger.getLogger().debug("GameLogic getInstance called!");
        if (instance == null) HiveLogger.getLogger().fatal("GameLogic getInstance called without starting newGame!");
        return instance;
    }

    /**
     * Létrehoz egy új GameLogic példányt.
     * Ezzel minden attribútuma újragenerálódik.
     * (pl. a tábla is kitörlődik).
     */
    public static void newGame() {
        HiveLogger.getLogger().debug("GameLogic newGame called!");
        instance = new GameLogic();
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int val) {
        HiveLogger.getLogger().info("GameLogic setTurns called!");
        turns = val;
    }

    public void incrementTurns() {
        turns++;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * DFS algoritmussal bejárjuk a játéktáblát a megadott tile-tól kezdve.
     * A végén a paraméterként megadott visited az összes elérhető tile-t kiadja.
     *
     * @param visited a meglátogatott tile-ok halmaza.
     * @param root    a gyökér, ahonnan éppen indulunk
     */
    private static void pathFinder(Set<GameTile> visited, GameTile root) {
        visited.add(root);
        for (int i = 0; i < 6; i++) {
            GameTile to = root.getNeigbour(i);
            if (to.isInitialized() && !visited.contains(to)) {
                pathFinder(visited, to);
            }
        }
    }

    /**
     * DFS-t segítségül hívva megállapítja, hogy a játéktábla összefüggő lenne-e,
     * ha az adott rovart elmozdítanánk (ak. a tile-ját uninicializálnánk)
     *
     * @param moveStart a tile, ahonnan a rovar mozogna.
     * @return true, ha összefüggő maradna vagy a tábla a tábla legfeljebb egy inicializált tile-t tartalmaz,
     * hamis, ha legalább 2 részre szétszakadna.
     */
    public boolean wouldHiveBeConnected(GameTile moveStart) {
        Set<GameTile> newState = board.getInitializedTileSet();
        newState.remove(moveStart);
        GameTile checkOrigo;

        while (true) {
            GameTile assign = board.getInitializedTile();
            if (assign == null) {
                HiveLogger.getLogger().error("a wouldHiveBeConnected() was called for an either empty or 1-insect board.");
                return true;
            }
            if (!assign.equals(moveStart)) {
                checkOrigo = assign;
                break;
            }
        }

        Set<GameTile> reachables = new HashSet<>();
        pathFinder(reachables, checkOrigo);
        return newState.equals(reachables);
    }

    public Set<GameTile> pingAvailableTilesForPlacing(Player p) {
        Set<GameTile> unInitializedTiles = board.getUnInitializedTileSet();
        Set<GameTile> availableTiles = new HashSet<>();

        if (turns <= 2) {
            availableTiles.addAll(unInitializedTiles);
        } else {
            for (GameTile tile : unInitializedTiles) {
                boolean noOpponentNeighbour = true;
                for (int i = 0; i < 6; i++) {
                    GameTile neighbour = tile.getNeigbour(i);
                    if (neighbour.isInitialized() && !neighbour.getInsect().getPlayer().equals(p)) {
                        noOpponentNeighbour = false;
                        break;
                    }
                }
                if (noOpponentNeighbour) {
                    availableTiles.add(tile);
                }
            }
        }

        return availableTiles;
    }
}
