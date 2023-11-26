package org.insects;

import org.game.GameLogic;
import org.game.GameTile;
import org.game.Player;

import java.util.HashSet;
import java.util.Set;

public class Ant extends Insect{

    public Ant(Player p, GameLogic gameLogic){
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
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, GameTile root, GameTile from) {
        if(!root.isInitialized()) destinations.add(root); //hogy a kiindulópontot ne adjuk hozzá
        for (int i = 0; i < 6; ++i) {
            GameTile to = root.getNeigbour(i);
            if (to!=null && isItSliding(root, i) && !to.isInitialized() && !to.equals(from)) {
                destinations.addAll(pathFinder(destinations, root.getNeigbour(i), root));
            }
        }
        return destinations;
    }

    @Override
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, GameTile from){
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<GameTile> pingAvailableTiles() {
        Set<GameTile> availableTiles = new HashSet<>();
        availableTiles = pathFinder(availableTiles, location, null);
        return availableTiles;
    }
}
