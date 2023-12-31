package hive.game;

import hive.GraphicLogger;
import hive.HiveLogger;
import hive.graphics.controllers.ModelListener;
import hive.insects.Queen;

import java.util.HashSet;
import java.util.Set;

/**
 * A Hive játéklogikájáért felelős osztály.
 * Az Insect osztály is kezel alapvető ellenőrzéseket (pl. az útkeresés az ő feladata),
 * de a globális helyességet ez az osztály kezeli.
 * Singleton osztály.
 */
public class GameLogic {
    private static GameLogic instance = null;

    private ModelListener listener;

    /**
     * A tile kiválasztás állapotait rögzítő enum.
     */
    enum SelectionState {
        STARTSELECT,

        MOVESELECT,

        PLACESELECT,
    }

    /**
     * A játék állapotait rögzítő enum.
     */
    public enum GameState {
        RUNNING,

        TERMINATED
    }

    private GameState gameState;

    private GameTile startTile;

    private SelectionState selectionState = SelectionState.STARTSELECT;

    private int turns;

    private final Player orangePlayer;

    private final Player bluePlayer;

    private Player nextPlayer;

    private final Set<GameTile> pingedTiles = new HashSet<>();

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
        orangePlayer = new Player(HiveColor.ORANGE, this);
        bluePlayer = new Player(HiveColor.BLUE, this);
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
     * Figyelem: a tábla nem üres: egyetlen inicializálatlan tile hozzáadódik!
     */
    public void newGame() {
        clearGame();
        new GameTile(board, new Coordinate(0, 0));
        bluePlayer.initPlayer();
        orangePlayer.initPlayer();
        gameState = GameState.RUNNING;
        HiveLogger.getLogger().info("GameLogic: New Game was started successfully.\n" +
                "############################################");
    }

    /**
     * Reseteli a játékot.
     * A különbség a newGame és a newGameForTesting és eközött az,
     * hogy ez nem állítja át a játék állapotát TERMINATED-ről,
     * valamint ez reseteli a játékosokat.
     */
    public void clearGame() {
        turns = 1;
        orangePlayer.resetPlayer();
        bluePlayer.resetPlayer();
        nextPlayer = orangePlayer;
        board.clear();
        winner = null;
        HiveLogger.getLogger().info("GameLogic: Game properties were reset successfully.");
    }

    /**
     * Tesztelési célokra létrehozott NewGame.
     * Ugyanaz, mint a NewGame, csak itt az első anonim tile az origóban
     * nincs lerakva automatikusan.
     */
    public void newGameForTesting() {
        clearGame();
        gameState = GameState.RUNNING;
    }

    public int getTurns() {
        return turns;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player p) {
        this.nextPlayer = p;
    }

    public Player getWinner() {
        return winner;
    }

    public void setTurns(int val) {
        HiveLogger.getLogger().debug("GameLogic setTurns called!");
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
        if (nextPlayer == orangePlayer) {
            nextPlayer = bluePlayer;
        } else {
            nextPlayer = orangePlayer;
        }
    }

    public Player getOrangePlayer() {
        return orangePlayer;
    }

    public Player getBluePlayer() {
        return bluePlayer;
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

    /**
     * Visszaadja azon inicializálatlan tile-ok halmazát, amelyekre a játékos a rovart
     * a szabályok szerint leteheti.
     * @param p az aktuális játékos
     * @return a lehetséges tile-ok halmaza
     */
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
                    if (neighbour != null && neighbour.isInitialized() && !neighbour.getInsect().getPlayer().equals(p)) {
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
        for (GameTile tile : availableTiles) {
            tile.setState(TileStates.PINGED);
        }

        return availableTiles;
    }

    /**
     * Ellenőrzi, hogy a következő játékosnak le kell-e már tennie a királynőjét.
     * Ha igen, akkor kényszeríti erre a lépésre, ha pedig lehetetlen letenni a királynőt,
     * akkor az ellenfele győzött.
     */
    private void checkQueenCondition() {
        if (!nextPlayer.isQueenDown() && turns >= 7) {
            Queen queen = nextPlayer.getQueen();
            startTile = queen.getLocation();
            startTile.setState(TileStates.SELECTED);
            pingedTiles.addAll(pingAvailableTilesForPlacing(nextPlayer));

            if(pingedTiles.isEmpty()){
                //hisz annak az ellenfele lesz a győztes, aki nem tudta letenni a királynőjét
                Player p = nextPlayer.equals(orangePlayer) ? bluePlayer : orangePlayer;
                endGame(p);
            }

            selectionState = SelectionState.PLACESELECT;
            HiveLogger.getLogger().debug("{} player has to place its queen now!", nextPlayer.getColor());
        }
    }

    /**
     * Elindítja a játékvége-láncolatot.
     * @param winner a győztes játékos
     */
    private void endGame(Player winner){
        if (winner.equals(bluePlayer)) {
            HiveLogger.getLogger().info("The black Player won!");
            this.winner = bluePlayer;
            gameState = GameState.TERMINATED;
            notifyListeners();
        } else if (winner.equals(orangePlayer)) {
            HiveLogger.getLogger().info("The white Player won!");
            this.winner = orangePlayer;
            gameState = GameState.TERMINATED;
            notifyListeners();
        }

    }

    /**
     * Ellenőrzi, hogy valamelyik játékos királynője be lett-e már kerítve.
     */
    private void checkEndGameCondition() {
        if (orangePlayer.getQueen().isInitialized() && orangePlayer.getNeighboursOfQueen() == 6) {
            endGame(bluePlayer);
        } else if (bluePlayer.getQueen().isInitialized() && bluePlayer.getNeighboursOfQueen() == 6) {
            endGame(orangePlayer);
        }
    }

    /**
     * Az első kattintás esetén végrehajtja az állapotgép utasításait.
     * @param tile a tile, amire a játékos kattintott
     * @param actor a kattintó játékos
     */
    private void startSelectSettings(GameTile tile, Player actor) {
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


            if (startTile != null) {
                startTile.setState(TileStates.UNSELECTED);
            }
            startTile = tile;

            tile.setState(TileStates.SELECTED);
        }

        //egyébként nem csinálunk semmit
    }

    /**
     * A második kattintás során végrehajtandó állapotgépet kezeli.
     * @param tile a kiválasztott tile
     * @param actor a kattintó játékos
     */
    private void secondSelectSettings(GameTile tile, Player actor){
        boolean success = performActionBasedOnSelectionState(tile);

        if (success) {
            resetSelection();
            incrementTurns();
            checkGameConditions();
        } else if (shouldResetSelection(tile)) {
            resetSelection();
            startSelectSettings(tile, actor);
        }
    }

    /**
     * A második kattintás során megnézi, hogy most egy rovar mozgatásáról,
     * vagy elhelyezéséről van szó, és eszerint regisztrálja az akció sikerességét.
     * @param tile a kiválasztott tile
     * @return sikeres volt-e az akció.
     */
    private boolean performActionBasedOnSelectionState(GameTile tile) {
        if (selectionState == SelectionState.MOVESELECT) {
            return startTile.getInsect().move(tile);
        } else {
            return startTile.getInsect().place(tile);
        }
    }

    /**
     * Visszaállítja a kattintás miatt kijelölt tile-okat
     * eredeti állapotukba.
     */
    private void resetSelection() {
        startTile.setState(TileStates.UNSELECTED);
        for (GameTile g : pingedTiles) {
            g.setState(TileStates.UNSELECTED);
        }
        pingedTiles.clear();
        selectionState = SelectionState.STARTSELECT;
    }

    /**
     * Ellenőriz 2 fontos játékfeltételt:
     * a királynőt letették-e;
     * a királynőt bekerítették-e.
     */
    private void checkGameConditions() {
        checkEndGameCondition();
        checkQueenCondition();
    }

    /**
     * Második kattintásnál releváns.
     * Megnézi, hogy a második kattintás egy releváns rovarra történt-e,
     * ekkor az állapotgép úgy értelmezi, hogy a kattintó meggondolta magát,
     * és egy másik rovarral szeretne inkább lépni.
     * @param tile a kiválasztott tile
     * @return resetelendőek-e a tile-ok "meggondolás" miatt
     */
    private boolean shouldResetSelection(GameTile tile) {
        return (selectionState == SelectionState.MOVESELECT || selectionState == SelectionState.PLACESELECT)
                && tile.isInitialized();
    }

    /**
     * Végrehajtja a kattintáshoz kötődő állapotgépet.
     * @param tile a tile, amire az egyik játékos kattintott.
     */
    public void clickedTile(GameTile tile) {

        Player actor = nextPlayer;

        if (selectionState == SelectionState.STARTSELECT) {

            startSelectSettings(tile, actor);

        } else {
            secondSelectSettings(tile, actor);
        }
    }

    public void addListener(ModelListener listener) {
        this.listener = listener;
    }

    public void notifyListeners() {
        if (listener != null) {
            listener.onModelChange();
        } else {
            GraphicLogger.getLogger().error("GameLogic: could not notify controller: null instance.");
        }
    }
}
