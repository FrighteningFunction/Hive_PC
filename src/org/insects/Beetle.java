package org.insects;

import org.game.GameLogic;
import org.game.GameTile;
import org.game.HiveLogger;
import org.game.Player;

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
    public void move(GameTile chosenTile) {
        Set<GameTile> availableTiles=pingAvailableTiles();
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
            }else {
                chosenTile.initialize(this);
            }
        } else {
            HiveLogger.getLogger().info("Beetle : the player wanted to step to an incorrect Tile");
        }
    }

    @Override
    protected boolean isItSliding(GameTile tile, int direction) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, GameTile from) {
        throw new UnsupportedOperationException();
    }

    /**
     * A Beetle pathfindere. Egy egyszintű rekurzió az egységesség kedvéért.
     *
     * @param destinations azon tile-ok halmaza, ahová a rovar léphet. Ezt építgetjük.
     * @param steps        eddig ennyit lépett a rovar.
     * @param root         az aktuális kiindulópont.
     * @return a gyerekeiből összegyűjtött lehetséges céltile-ok halmaza.
     */
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root) {
        if (steps == maxstep) {
            destinations.add(root);
        } else {
            for (int i = 0; i < 6; ++i) {
                destinations.addAll(pathFinder(destinations, steps++, root));
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
        availableTiles = pathFinder(availableTiles, 0, location);
        return availableTiles;
    }
}
