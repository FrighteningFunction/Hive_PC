package org.insects;

import org.game.GameTile;
import org.game.Player;

import java.util.HashSet;
import java.util.Set;

public class Queen extends Insect{
    public Queen(Player p){
        super(p);
        maxstep=1;
    }
}
