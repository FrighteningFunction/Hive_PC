package org.game;

import org.graphics.controllers.ModelListener;
import org.insects.Queen;

import java.util.HashSet;
import java.util.Set;

public class GameLogic {
    private static GameLogic instance = null;

    private ModelListener listener;

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

    private final Player whitePlayer;

    private final Player blackPlayer;

    private Player nextPlayer;

    private final  Set<GameTile> pingedTiles = new HashSet<>();

    private Player winner;

    private final GameBoard board;

    /**
     * Létrehoz egy új GameLogic példányt.
     * Vigyázat: itt még a játék inicializálatlan.
     * A newGame-el készíthetjük elő a játék komponenseit.
     */
    private GameLogic() {
        gameState = GameState.TERMINATED;
        turns = -1;
        whitePlayer = new Player(HiveColor.BLUE, this);
        blackPlayer = new Player(HiveColor.RED, this);
        nextPlayer = null;
        board = GameBoard.getInstance();
        winner = null;
    }

    /**
     * Visszaadja az éppen aktuális GameLogic példányt.
     * Ha még nem létezik, generál egyet.
     *
     * @return aktuális játékpéldány (GameLogic)
     */
    public static GameLogic getInstance() {
        HiveLogger.getLogger().debug("GameLogic getInstance called!");
        if (instance == null) {
            instance = new GameLogic();
        }
        return instance;
    }

    /**
     * Inicializálja a játékot, minden attribútumát előkészíti.
     * Ez felér egy resettel.
     * Figyelem: a tábla nem üres: egyetlen tile hozzáadódik!
     */
    public void newGame() {
        gameState = GameState.RUNNING;
        turns = 1;
        whitePlayer.initPlayer();
        blackPlayer.initPlayer();

        nextPlayer = whitePlayer;
        board.clear();
        new GameTile(board, new Coordinate(0, 0));
        winner = null;
        HiveLogger.getLogger().info("GameLogic: New Game was started successfully.\n" +
                "############################################");
    }

    /**
     * Tesztelési célokra létrehozott NewGame.
     * Ugyanaz, mint a NewGame, csak itt az első anonim tile az origóban
     * nincs lerakva automatikusan.
     *
     */
    public void newGameForTesting(){
        gameState = GameState.RUNNING;
        turns = 1;
        whitePlayer.initPlayer();
        blackPlayer.initPlayer();

        nextPlayer = whitePlayer;
        board.clear();
        winner = null;
    }

    public int getTurns() {
        return turns;
    }

    public Player getWinner() {
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

    public GameBoard getBoard() {
        return board;
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
            GameTile to = root.getNeighbour(i);
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
                    GameTile neighbour = tile.getNeighbour(i);
                    if (neighbour!=null && neighbour.isInitialized() && !neighbour.getInsect().getPlayer().equals(p)) {
                        noOpponentNeighbour = false;
                        break;
                    }
                }
                if (noOpponentNeighbour) {
                    availableTiles.add(tile);
                }
            }
        }

        //átállítjuk a kiválasztott Tile-ok állapotát PINGED-re
        for(GameTile tile : availableTiles){
            tile.setState(TileStates.PINGED);
        }

        return availableTiles;
    }

    //todo: ha nincs available tile for placing, akkor a másik játékos győzött
    private void checkQueenCondition() {
        if (!nextPlayer.isQueenDown() && turns >= 7) {
            Queen queen = nextPlayer.getQueen();
            startTile=queen.getLocation();
            startTile.setState(TileStates.SELECTED);
            pingedTiles.addAll(pingAvailableTilesForPlacing(nextPlayer));

            selectionState = SelectionState.PLACESELECT;
            HiveLogger.getLogger().debug("{} player has to place its queen now!", nextPlayer.getColor());
        }
    }

    private void checkEndGameCondition() {
        if (whitePlayer.getQueen().isInitialized()&&whitePlayer.getNeighboursOfQueen() == 6) {
            HiveLogger.getLogger().debug("The black Player won!");
            gameState = GameState.TERMINATED;
            notifyListeners();
        } else if (blackPlayer.getQueen().isInitialized()&&blackPlayer.getNeighboursOfQueen() == 6) {
            HiveLogger.getLogger().debug("The white Player won!");
            gameState = GameState.TERMINATED;
            notifyListeners();
        }
    }

    public void clickedTile(GameTile tile) {

        Player actor = nextPlayer;

        //ha először kattintunk
        if (selectionState == SelectionState.STARTSELECT) {

            //ha inicializált tile-ra kattintunk, és a sajátunkra
            if (tile.isInitialized() && actor.equals(tile.getInsect().getPlayer())) {

                //ha a kiválasztott rovart letettük már
                if (tile.getInsect().isInitialized()) {
                    selectionState = SelectionState.MOVESELECT;
                    pingedTiles.addAll(tile.getInsect().pingAvailableTiles());
                } else {
                    //ha a kiválaszott rovart nem tettük még le
                    selectionState = SelectionState.PLACESELECT;
                    pingedTiles.addAll(pingAvailableTilesForPlacing(actor));
                    HiveLogger.getLogger().info("for {} player the available tiles for placing were pinged", actor.color);
                }
                if(startTile!=null){
                    startTile.setState(TileStates.UNSELECTED);
                }
                startTile=tile;

                tile.setState(TileStates.SELECTED);
            } else {
                //semmi
            }

        } else {
            //ha másodszor kattintunk

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
                pingedTiles.clear();
                selectionState = SelectionState.STARTSELECT;

                incrementTurns();

                checkEndGameCondition();
                checkQueenCondition();
            }
        }
    }

    public void addListener(ModelListener listener){
        this.listener=listener;
    }

    public void notifyListeners() {
        if (listener != null) {
            listener.onModelChange();
        }else{
            GraphicLogger.getLogger().error("GameLogic: could not notify controller: null instance.");
        }
    }
}
