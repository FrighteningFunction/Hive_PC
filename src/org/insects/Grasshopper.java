package org.insects;

import org.game.GameBoard;
import org.game.GameLogic;
import org.game.GameTile;
import org.game.Player;

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
            destinations.addAll(pathFinder(destinations, root.getNeigbour(GameBoard.invertDirection(from)), from));
        }
        return destinations;
    }

    @Override
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, GameTile from){
        throw new UnsupportedOperationException();
    }

    /**
     * A rovar mozgási szabályai szerint visszaadja azon tile-ok listáját, ahová
     * a szabályok szerint léphet.
     *
     * @return a támogatott tile-ok listája.
     */
    @Override
    public Set<GameTile> pingAvailableTiles() {
        Set<GameTile> availableTiles = new HashSet<>();
        for(int i=0; i<6; i++){
            GameTile neighbour = location.getNeigbour(i);
            if(neighbour.isInitialized()){
                availableTiles.addAll(pathFinder(availableTiles, neighbour, GameBoard.invertDirection(i)));
            }
        }
        return availableTiles;
    }
}
