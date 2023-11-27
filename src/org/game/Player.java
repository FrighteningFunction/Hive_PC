package org.game;

import org.insects.*;
import org.insects.Insect;
import org.insects.Queen;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Queen queen;

    public final HiveColor color;

    private List<Insect> insects = new ArrayList<>();

    Player(HiveColor color, GameLogic gameLogic){
        this.color=color;

        queen = new Queen(this, gameLogic);

        insects.add(queen);

        insects.add(new Ant(this, gameLogic));
        insects.add(new Ant(this, gameLogic));
        insects.add(new Ant(this, gameLogic));

        insects.add(new Beetle(this, gameLogic));
        insects.add(new Beetle(this, gameLogic));

        insects.add(new Grasshopper(this, gameLogic));
        insects.add(new Grasshopper(this, gameLogic));
        insects.add(new Grasshopper(this, gameLogic));

        insects.add(new Spider(this, gameLogic));
        insects.add(new Spider(this, gameLogic));
    }

    public void removeInsect(Insect insect){
        if(!insects.remove(insect)){
            HiveLogger.getLogger().error("A nonexistent insect was to be removed from Player");
        }
    }

    public int getNeighboursOfQueen(){
        return queen.getTotalNeighbours();
    }


}
