package org.insects;

import org.game.GameTile;
import org.game.HiveLogger;
import org.game.Player;

import java.util.HashSet;
import java.util.Set;

public abstract class Insect {
    protected int maxstep;

    boolean initialized;
    Player player;

    GameTile location;

    protected Insect(Player p) {
        initialized = false;
        player = p;
        location = null;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public GameTile getLocation() {
        return location;
    }

    public Player getPlayer() {
        return player;
    }

    public void move(GameTile chosenTile, Set<GameTile> availableTiles) {
        if (availableTiles.contains(chosenTile)) {
            chosenTile.initialize(this);
            location.uninitialize();
        } else {
            HiveLogger.getLogger().info("The player wanted to step to an incorrect Tile");
        }
    }

    public void place(GameTile tile) {
        if (initialized) {
            HiveLogger.getLogger().fatal("Already initialized insect was to be placed!");
        } else {
            initialized = true;
            tile.initialize(this);
        }
    }

    /**
     * Ellenőrizzük, hogy a kérdéses szomszédhoz képesti 2 körbevevő
     * szomszéd inicializált-e. Ha igen, akkor csúszás következne be,
     * csúszni pedig nem tudunk.
     *
     * @param tile      a kiinduló anyatile.
     * @param direction az irány, amerre mennénk
     * @return true ha csúszna, false ha rendben van
     */
    protected boolean isItSliding(GameTile tile, int direction) {
        GameTile toTheLeft = tile.getNeigbour((direction - 1) % 6);
        GameTile toTheRight = tile.getNeigbour((direction + 1) % 6);

        //elősször kiértékeljük, hogy nullok-e, nehogy nullpointerexceptiont kapjunk.
        return toTheLeft != null && toTheRight != null && !(toTheLeft.isInitialized() && toTheRight.isInitialized());
    }

    /**
     * Ez az alapértelmezett pathfindere az összes rovarnak:
     * nem engedi, hogy "átmenjen" más rovarokon
     * valamint nem engedi messzebb, sem közelebb annál, mint ahányat lépnie kell.
     * Ez utóbbi feltétel alól durván kivétel az Ant.
     *
     * @param destinations azon tile-ok halmaza, ahová a rovar léphet. Ezt építgetjük.
     * @param steps        eddig ennyit lépett a rovar.
     * @param root         az aktuális kiindulópont.
     * @param from         ahonnan jött
     * @return a gyerekeiből összegyűjtött lehetséges céltile-ok halmaza.
     */
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, GameTile from) {
        if (steps == maxstep) {
            destinations.add(root);
        } else {
            for (int i = 0; i < 6; ++i) {
                GameTile to = root.getNeigbour(i);
                if (to!=null && isItSliding(root, i) && !to.isInitialized() && !to.equals(from)) {
                    destinations.addAll(pathFinder(destinations, steps++, root.getNeigbour(i), root));
                }
            }
        }
        return destinations;
    }

    /**
     * A rovar mozgási szabályai szerint visszaadja azon tile-ok listáját, ahová
     * a szabályok szerint léphet.
     *
     * @return a támogatott tile-ok listája.
     */
    public Set<GameTile> pingAvailableTiles() {
        Set<GameTile> availableTiles = new HashSet<>();
        availableTiles = pathFinder(availableTiles, 0, location, null);
        return availableTiles;
    }

}
