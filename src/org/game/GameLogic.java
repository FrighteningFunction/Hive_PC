package org.game;

import org.graphics.controllers.GamePanelController;
import org.insects.Queen;

import java.util.HashSet;
import java.util.Set;

public class GameLogic {
    private static GameLogic instance = null;

    private GamePanelController gamePanelController;

    enum SelectionState {
        STARTSELECT,

        MOVESELECT,

        PLACESELECT,
    }

    public enum GameState {
        RUNNING,

        SAVEABLE,

        TERMINATED
    }

    private GameState gameState;

    private GameTile startTile;

    private SelectionState selectionState = SelectionState.STARTSELECT;

    private int turns;

    private Player whitePlayer;

    private Player blackPlayer;

    private Player nextPlayer;

    private Player winner;

    private GameBoard board;

    private GameLogic() {
        gameState = GameState.RUNNING;
        turns = 1;
        whitePlayer = new Player(HiveColor.WHITE, this);
        blackPlayer = new Player(HiveColor.BLACK, this);

        nextPlayer = whitePlayer;
        board = GameBoard.getInstance();
        board.clear();
        new GameTile(board, new Coordinate(0,0));
        winner=null;
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

    public Player getWinner(){
        return winner;
    }

    public void setTurns(int val) {
        HiveLogger.getLogger().info("GameLogic setTurns called!");
        turns = val;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void incrementTurns() {
        turns++;
        if (nextPlayer == whitePlayer) {
            nextPlayer = blackPlayer;
        } else {
            nextPlayer = whitePlayer;
        }
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
     * @param visited   a meglátogatott tile-ok halmaza.
     * @param root      a gyökér, ahonnan éppen indulunk
     * @param exception a tile, ami nélküli adathalmazt vizsgálunk
     */
    private static void pathFinder(Set<GameTile> visited, GameTile root, final GameTile exception) {
        visited.add(root);
        for (int i = 0; i < 6; i++) {
            GameTile to = root.getNeigbour(i);
            if (to.isInitialized() && !visited.contains(to) && to != exception) {
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
        if (newState.size() <= 2) {
            return true;
        }
        newState.remove(moveStart);
        GameTile checkOrigo = board.getInitializedTile(moveStart);
        if (checkOrigo == null) {
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

    private void checkQueenCondition() {
        if (!nextPlayer.isQueenDown() && turns == 4) {
            Queen queen = nextPlayer.getQueen();
            queen.getLocation().setState(TileStates.SELECTED);
            pingAvailableTilesForPlacing(nextPlayer);

            selectionState = SelectionState.PLACESELECT;
            HiveLogger.getLogger().debug("{} player has to place its queen now!", nextPlayer.getColor().toString());
        }
    }

    private void checkEndGameCondition() {
        //todo: felugró ablak generálása (nem itt)
        if (whitePlayer.getNeighboursOfQueen() == 6) {
            HiveLogger.getLogger().debug("The black Player won!");
            gameState = GameState.TERMINATED;
            notifyListeners();
        } else if (blackPlayer.getNeighboursOfQueen() == 6) {
            HiveLogger.getLogger().debug("The white Player won!");
            gameState = GameState.TERMINATED;
            notifyListeners();
        }
    }

    public void clickedTile(GameTile tile) {

        Set<GameTile> pingedTiles = new HashSet<>();

        //ha először kattintunk
        if (selectionState == SelectionState.STARTSELECT) {
            Player actor = tile.getInsect().getPlayer();

            //ha inicializált tile-ra kattintunk, és a sajátunkra
            if (tile.isInitialized() && actor.equals(nextPlayer)) {

                //ha a kiválasztott rovart letettük már
                if (tile.getInsect().isInitialized()) {
                    selectionState = SelectionState.MOVESELECT;
                    pingedTiles.addAll(tile.getInsect().pingAvailableTiles());
                } else {
                    //ha a kiválaszott rovart nem tettük még le
                    selectionState = SelectionState.PLACESELECT;
                    pingedTiles.addAll(pingAvailableTilesForPlacing(actor));
                }
                startTile = tile;

                tile.setState(TileStates.SELECTED);
            } else {
                //semmi
            }

        } else {

            boolean success;
            if (selectionState == SelectionState.MOVESELECT) {
                success = startTile.getInsect().move(tile);
            } else {
                success = startTile.getInsect().place(tile);
            }


            if (success) {
                startTile.setState(TileStates.UNSELECTED);
                for (GameTile g : pingedTiles) {
                    g.setState(TileStates.UNSELECTED);
                }
                selectionState = SelectionState.STARTSELECT;

                turns++;

                checkEndGameCondition();
                checkQueenCondition();
            }
        }
    }

    public void notifyListeners() {
        if (gamePanelController != null) {
            gamePanelController.onModelChange();
        }
    }
}
