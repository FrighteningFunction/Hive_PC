package org.game;

import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class GameTile {
    private boolean initialized = false;

    private GameBoard board = null;

    private Insect insect = null;

    private Coordinate coordinate;
    private List<GameTile> neighbours = new LinkedList<>();

    /**
     * Ez a konstruktor intézi az új GameTile felvételt.
     * todo: implementálás, másrészt: kell ide a szülő, vagy mind a 6 szomszédot koordinátákkal csináljuk?
     * @param b
     * @param parent
     */
    public GameTile(GameBoard b, GameTile parent){
        this.board = b;
    }

    GameTile getNeigbour(int ind) {
        return neighbours.get(ind);
    }

    boolean isInitialized() {
        return initialized;
    }

    /**
     * todo: implement, és: talán a bogarat is itt kéne átadni?
     */
    void initialize() {
        if (initialized) {
            HiveLogger.getLogger().error("FATAL: GameTile already initialized.");
            exit(1);
        } else {

        }
    }
}
