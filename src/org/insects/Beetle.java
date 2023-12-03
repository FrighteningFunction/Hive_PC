package org.insects;

import org.game.*;
import org.graphics.ImageLoader;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Beetle extends Insect {

    private Insect controlledInsect;

    public Beetle(Player p, GameLogic gameLogic) {
        super(p, gameLogic);
        maxstep = 1;
        controlledInsect = null;
    }

    @Override
    public boolean move(GameTile chosenTile) {
        Set<GameTile> availableTiles = pingAvailableTiles();
        if (availableTiles.contains(chosenTile)) {
            if (controlledInsect != null) {
                location.setInsect(controlledInsect);
                controlledInsect = null;
            } else {
                location.uninitialize();
            }

            if (chosenTile.isInitialized()) {
                controlledInsect = chosenTile.getInsect();
                chosenTile.setInsect(this);
            } else {
                chosenTile.initialize(this);
            }
            location = chosenTile;
            return true;
        }
        HiveLogger.getLogger().info("Beetle : the player wanted to step to an incorrect Tile");
        return false;
    }

    /**
     * A Beetle pathfindere. Egy egyszintű rekurzió az egységesség kedvéért.
     *
     * @param destinations azon tile-ok halmaza, ahová a rovar léphet. Ezt építgetjük.
     * @param steps        eddig ennyit lépett a rovar.
     * @param root         az aktuális kiindulópont.
     * @param from         ahonnan a pathfinder utoljára jött (itt nem használt)
     * @param visitedTiles a már meglátogatott tile-ok halmaza
     * @return a gyerekeiből összegyűjtött lehetséges céltile-ok halmaza.
     */
    @Override
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, GameTile from, Set<GameTile> visitedTiles) {
        visitedTiles.add(root);
        if (steps == maxstep) {
            destinations.add(root);
        } else {
            if (controlledInsect == null) {
                for (int i = 0; i < 6; ++i) {
                    GameTile to = root.getNeighbour(i);
                    if (to != null && !visitedTiles.contains(to) && !to.wouldBeOrphan(location) && !isItSliding(root, i)) {
                        destinations.addAll(pathFinder(destinations, steps + 1, to, root, visitedTiles));
                    }
                }
            } else {
                //ha initialized tile-t hagy maga után, bárhova léphet körülötte
                for (int i = 0; i < 6; ++i) {
                    GameTile to = root.getNeighbour(i);
                    if (!visitedTiles.contains(to)) {
                        destinations.addAll(pathFinder(destinations, steps + 1, to, root, visitedTiles));
                    }
                }
            }
        }
        return destinations;
    }

    /**
     * A rovar mozgási szabályai szerint visszaadja azon tile-ok listáját, ahová
     * a szabályok szerint léphet.
     *
     * @return a támogatott tile-ok halmaza.
     */
    @Override
    public Set<GameTile> pingAvailableTiles() {
        Set<GameTile> availableTiles = new HashSet<>();
        if (controlledInsect!=null || gameLogic.wouldHiveBeConnected(location)) {
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

    @Override
    protected Image setImage() {
        return ImageLoader.loadImage("./Resources/beetle.png");
    }
}
