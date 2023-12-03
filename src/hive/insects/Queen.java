package hive.insects;

import hive.game.GameLogic;
import hive.game.Player;
import hive.graphics.ImageLoader;

import java.awt.*;

public class Queen extends Insect{

    public Queen(Player p, GameLogic gameLogic){
        super(p, gameLogic);
        maxstep=1;
    }

    public String toString(){
        return "queen";
    }

    @Override
    protected Image setImage() {
        return ImageLoader.loadImage("./Resources/queen.png");
    }
}
