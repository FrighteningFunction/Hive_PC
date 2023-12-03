package hive.insects;

import hive.game.GameLogic;
import hive.game.Player;
import hive.graphics.ImageLoader;

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
