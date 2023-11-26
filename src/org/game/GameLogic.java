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
     * @param exception a tile, ami nélküli adathalmazt vizsgálunk
     */
    private static void pathFinder(Set<GameTile> visited, GameTile root, final GameTile exception) {
        visited.add(root);
        for (int i = 0; i < 6; i++) {
            GameTile to = root.getNeigbour(i);
            if (to.isInitialized() && !visited.contains(to) && to!=exception) {
                pathFinder(visited, to, exception);
            }
        }
    }

    /**
     * DFS-t segítségül hívva megállapítja, hogy a játéktábla összefüggő lenne-e,
     * ha az adott rovart elmozdítanánk (ak. a tile-ját uninicializálnánk)
     * <p></p>
     * <b>Megjegyzés:</b>
     * Minden esetben, ha a tábla üres, összefüggő. Az üres gráf is az. Egyetlen
     * rovar esetén is az, azt akárhogy "mozgathatnánk". 2 rovart definíció szerint
     * nem lehet úgy mozgatni, hogy elszakadjanak, ezért azt az esetet sem nézzük.
     *
     * @param moveStart a tile, ahonnan a rovar mozogna.
     * @return true, ha összefüggő maradna vagy a tábla a tábla legfeljebb 2 inicializált tile-t tartalmaz,
     * <br>hamis, ha size>2 és legalább 2 részre szétszakadna a mozgás miatt.
     */
    public boolean wouldHiveBeConnected(final GameTile moveStart) {
        Set<GameTile> newState = board.getInitializedTileSet();
        if(newState.size()<=2){
            return true;
        }
        newState.remove(moveStart);
        GameTile checkOrigo = board.getInitializedTile(moveStart);
        if(checkOrigo==null){
            HiveLogger.getLogger().fatal("getInitializedTile returned null, even though board had more than 2 initialized tiles!");
            return false;
        }

        Set<GameTile> reachables = new HashSet<>();
        pathFinder(reachables, checkOrigo, moveStart);
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
