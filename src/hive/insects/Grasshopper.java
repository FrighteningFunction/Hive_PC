package hive.insects;

import hive.game.*;
import hive.graphics.ImageLoader;
import org.game.*;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Grasshopper extends Insect{

    public Grasshopper(Player p, GameLogic gameLogic){
        super(p, gameLogic);
    }

    /**
     * A Grasshopper pahtfindere.
     * @param destinations azon tile-ok halmaza, ahová a rovar léphet. Ezt építgetjük.
     * @param root         az aktuális kiindulópont.
     * @param from         ahonnan jött
     * @return a gyerekeiből összegyűjtött lehetséges céltile-ok halmaza.
     */
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, GameTile root, int from) {
        if(!root.isInitialized()){
            destinations.add(root);
            return destinations;
        }else{
            destinations.addAll(pathFinder(destinations, root.getNeighbour(GameBoard.invertDirection(from)), from));
        }
        return destinations;
    }

    @Override
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, GameTile from, Set<GameTile> visitedTiles){
        throw new UnsupportedOperationException();
    }

    /**
     * A grasshopper mozgási szabályai szerint visszaadja azon tile-ok listáját, ahová
     * a szabályok szerint léphet.
     *
     * @return a támogatott tile-ok listája.
     */
    @Override
    public Set<GameTile> pingAvailableTiles() {
        Set<GameTile> availableTiles = new HashSet<>();
        if(gameLogic.wouldHiveBeConnected(location)) {
            for (int i = 0; i < 6; i++) {
                GameTile neighbour = location.getNeighbour(i);
                if (neighbour.isInitialized()) {
                    availableTiles.addAll(pathFinder(availableTiles, neighbour, GameBoard.invertDirection(i)));
                }
            }
            for (GameTile tile : availableTiles) {
                tile.setState(TileStates.PINGED);
            }
            HiveLogger.getLogger().info("{} insect of player {} pinged the available tiles", this.getClass(), this.player.getColor());
        }else{
            HiveLogger.getLogger().info("Player would have disconnected the hive.");
        }
        return availableTiles;
    }

    @Override
    protected Image setImage() {
        return ImageLoader.loadImage("./Resources/grasshopper.png");
    }
}
