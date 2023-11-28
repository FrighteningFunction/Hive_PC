package org.insects;

import org.game.GameLogic;
import org.game.GameTile;
import org.game.Player;
import org.graphics.ImageLoader;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Queen extends Insect{

    public Queen(Player p, GameLogic gameLogic){
        super(p, gameLogic);
        maxstep=1;
    }

    @Override
    protected Image setImage() {
        return ImageLoader.loadImage("./Resources/queen.png");
    }
}
