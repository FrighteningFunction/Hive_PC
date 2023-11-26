package org.insects;

import org.game.GameLogic;
import org.game.GameTile;
import org.game.Player;

public class Spider extends Insect{
    public Spider(Player p, GameLogic gameLogic){
        super(p, gameLogic);
        maxstep=3;
    }
}
