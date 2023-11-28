package org.insects;

import org.game.GameLogic;
import org.game.GameTile;
import org.game.Player;
import org.graphics.ImageLoader;

import java.awt.*;

public class Spider extends Insect{
    public Spider(Player p, GameLogic gameLogic){
        super(p, gameLogic);
        maxstep=3;
    }

    @Override
    protected Image setImage() {
        return ImageLoader.loadImage("./Resources/spider.png");
    }
}
