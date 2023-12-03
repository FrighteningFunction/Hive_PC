package org.insects;

import org.game.*;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class Insect {
    protected GameLogic gameLogic;
    protected int maxstep;

    private boolean initialized;

    protected final Player player;

    public final HiveColor color;

    protected GameTile location;

    protected final Image insectImage;

    protected Insect(Player p, GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        initialized = false;
        player = p;
        location = null;
        color = p.color;
        insectImage = setImage();
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

    protected abstract Image setImage();

    public Image getImage() {
        return insectImage;
    }

    public boolean move(GameTile chosenTile) {
        Set<GameTile> availableTiles = pingAvailableTiles();
        if (availableTiles.contains(chosenTile)) {
            location.uninitialize();
            chosenTile.initialize(this);
            location = chosenTile;
            return true;
        }
        HiveLogger.getLogger().info("The player wanted to step to an incorrect Tile");
        return false;
    }

    public boolean place(GameTile tile) {
        if (initialized) {
            HiveLogger.getLogger().fatal("Already initialized insect was to be placed!");
        } else {
            Set<GameTile> availableTiles = gameLogic.pingAvailableTilesForPlacing(this.player);
            if (availableTiles.contains(tile)) {
                initialized = true;
                player.removeGameTile(location);
                player.removeInsect(this);
                tile.initialize(this);
                location = tile;
                HiveLogger.getLogger().info("An insect was sucessfully placed.");
                return true;
            } else {
                HiveLogger.getLogger().info("Player wanted to place an insect to an invalid place");
            }
        }
        return false;
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
        GameTile toTheLeft = tile.getNeighbour((direction - 1 + 6) % 6); //+6, hogy ne kapjunk mínusz számot
        GameTile toTheRight = tile.getNeighbour((direction + 1) % 6);

        //elősször kiértékeljük, hogy nullok-e, nehogy nullpointerexceptiont kapjunk.
        return toTheLeft != null && toTheRight != null && toTheLeft.isInitialized() && toTheRight.isInitialized();
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
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, GameTile from, Set<GameTile> visitedTiles) {
        visitedTiles.add(root);
        if (steps == maxstep) {
            destinations.add(root);
        } else {
            for (int i = 0; i < 6; ++i) {
                GameTile to = root.getNeighbour(i);
                if (to != null && !visitedTiles.contains(to) && !to.isInitialized() && !to.equals(from) && !to.wouldBeOrphan(location) && !isItSliding(root, i)) {
                    destinations.addAll(pathFinder(destinations, steps + 1, root.getNeighbour(i), root, visitedTiles));
                }
            }
        }
        return destinations;
    }

    //todo: valamiért bizonyos esetekben nem jelöli ki pirossal az egyes tile-okat, de közben legálisan enged odalépni

    /**
     * A rovar mozgási szabályai szerint visszaadja azon tile-ok listáját, ahová
     * a szabályok szerint léphet.
     *
     * @return a támogatott tile-ok halmaza.
     */
    public Set<GameTile> pingAvailableTiles() {
        Set<GameTile> availableTiles = new HashSet<>();
        if (gameLogic.wouldHiveBeConnected(location)) {
            Set<GameTile> visitedTiles = new HashSet<>();
            visitedTiles.add(location);

            availableTiles = pathFinder(availableTiles, 0, location, null, visitedTiles);
            for (GameTile tile : availableTiles) {
                tile.setState(TileStates.PINGED);
            }
            HiveLogger.getLogger().info("{} insect of player {} pinged the available tiles", this.getClass(), this.player.getColor());
        } else {
            HiveLogger.getLogger().info("Player would have disconnected the hive.");
        }
        return availableTiles;
    }

    /**
     * Megmondja, hány rovar szomszédja van az adott rovarnak.
     *
     * @return rovarszomszédok száma.
     */
    public int getTotalNeighbours() {
        int num = 0;
        for (int i = 0; i < 6; i++) {
            if (location.getNeighbour(i).isInitialized()) {
                num++;
            }
        }
        return num;
    }

    public void setLocation(GameTile tile) {
        location = tile;
    }

}
