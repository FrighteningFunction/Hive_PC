package org.insects;

import org.game.GameTile;
import org.game.HiveLogger;
import org.game.Player;

import java.util.List;

public abstract class Insect {

    boolean initialized;
    Player player;

    GameTile location;

    protected Insect(Player p){
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

    public abstract void move();

    public void place(GameTile tile){
        if(initialized){
            HiveLogger.getLogger().fatal("Already initialized insect was to be placed!");
        }else{
            initialized=true;
            tile.initialize(this);
        }
    }

    /**
     * A rovar mozgási szabályai szerint visszaadja azon tile-ok listáját, ahová
     * a szabályok szerint léphet.
     * @return a támogatott tile-ok listája.
     */
    public abstract List<GameTile> pingAvailableTiles();

}
