package hive.insects;

import hive.HiveLogger;
import hive.game.*;
import hive.graphics.ImageLoader;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Ant extends Insect {

    public Ant(Player p, GameLogic gameLogic) {
        super(p, gameLogic);
    }

    /**
     * Az Ant speckó megvalósítása. Akármennyit léphet. A többi szabály ugyanaz.
     *
     * @param destinations azon tile-ok halmaza, ahová a rovar léphet. Ezt építgetjük.
     * @param root         az aktuális kiindulópont.
     * @param from         ahonnan jött
     * @return a gyerekeiből összegyűjtött lehetséges céltile-ok halmaza.
     */
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, GameTile root, GameTile from, Set<GameTile> visitedTiles) {
        visitedTiles.add(root);
        if (!root.isInitialized()) destinations.add(root); //hogy a kiindulópontot ne adjuk hozzá
        for (int i = 0; i < 6; ++i) {
            GameTile to = root.getNeighbour(i);
            if (to != null && !visitedTiles.contains(to) && !to.isInitialized() && !to.equals(from) && !to.wouldBeOrphan(location) && !isItSliding(root, i)) {
                destinations.addAll(pathFinder(destinations, root.getNeighbour(i), root, visitedTiles));
            }
        }
        return destinations;
    }

    @Override
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, GameTile from, Set<GameTile> visitedTiles) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<GameTile> pingAvailableTiles() {
        Set<GameTile> availableTiles = new HashSet<>();

        if (gameLogic.wouldHiveBeConnected(location)) {
            Set<GameTile> visitedTiles = new HashSet<>();


            availableTiles = pathFinder(availableTiles, location, null, visitedTiles);
            for (GameTile tile : availableTiles) {
                tile.setState(TileStates.PINGED);
                HiveLogger.getLogger().info("{} insect of player {} pinged the available tiles", this.getClass(), this.player.getColor());
            }
        } else {
            HiveLogger.getLogger().info("Player would have disconnected the hive.");
        }
        return availableTiles;
    }

    @Override
    protected Image setImage() {
        return ImageLoader.loadImage("./Resources/ant.png");
    }
}
