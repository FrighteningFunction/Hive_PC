package org.insects;

import org.game.GameTile;
import org.game.HiveLogger;
import org.game.Player;

import java.util.HashSet;
import java.util.Set;

public abstract class Insect {
    protected final int maxstep;

    boolean initialized;
    Player player;

    GameTile location;

    protected Insect(Player p, int maxStep){
        maxstep = maxStep;
        initialized=false;
        player = p;
        location=null;
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

    public void move(GameTile chosenTile, Set<GameTile> availableTiles){
        if(availableTiles.contains(chosenTile)){
            chosenTile.initialize(this);
            location.uninitialize();
        }else{
            HiveLogger.getLogger().info("The player wanted to step to an incorrect Tile");
        }
    }

    public void place(GameTile tile){
        if(initialized){
            HiveLogger.getLogger().fatal("Already initialized insect was to be placed!");
        }else{
            initialized=true;
            tile.initialize(this);
        }
    }

    /**
     * todo: a pathfinder nem kezeli azt az esetet, amikor a rovar csúszni akarna.
     * Ez az alapértelmezett pathfindere az összes rovarnak:
     * nem engedi, hogy "átmenjen" más rovarokon
     * valamint nem engedi messzebb, sem közelebb annál, mint ahányat lépnie kell.
     * Ez utóbbi feltétel alól durván kivétel az Ant.
     * @param destinations azon tile-ok halmaza, ahová a rovar léphet. Ezt építgetjük.
     * @param steps eddig ennyit lépett a rovar.
     * @param root az aktuális kiindulópont.
     * @param isStart ha ez igaz, akkor ez a metódus első hívása, a legfelső gyökér.
     * @return a gyerekeiből összegyűjtött lehetséges céltile-ok halmaza.
     */
    protected Set<GameTile> pathFinder(Set<GameTile> destinations, int steps, GameTile root, boolean isStart){
        //ha nem az első meghívás, akkor ha inicializálva van, vagy őmaga null, visszatér
        if(root == null || root.isInitialized()&&!isStart){
            return destinations;
        }else{
            isStart=false; //első lefutás után ez minden gyereknél hamis lesz

            if(steps== maxstep){
                destinations.add(root);
                return destinations;
            }else{
                for(int i=0; i<6; ++i){
                    destinations.addAll(pathFinder(destinations, steps++, root.getNeigbour(i), isStart));
                }
            }
        }
        return destinations;
    }

    /**
     * A rovar mozgási szabályai szerint visszaadja azon tile-ok listáját, ahová
     * a szabályok szerint léphet.
     * @return a támogatott tile-ok listája.
     */
    public Set<GameTile> pingAvailableTiles(){
        Set<GameTile> availableTiles = new HashSet<>();
        availableTiles=pathFinder(availableTiles, 0, location, true);
        return availableTiles;
    }

}
