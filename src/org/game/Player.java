package org.game;

import org.graphics.controllers.ModelListener;
import org.insects.*;
import org.insects.Insect;
import org.insects.Queen;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;

public class Player {
    private Queen queen;

    public final HiveColor color;

    private List<Insect> insects = new ArrayList<>();

    private List<GameTile> insectHolders = new ArrayList<>();

    private ModelListener listener;

    private static final double SPACE_BETWEEN_TILES = 10;

    Player(HiveColor color, GameLogic gameLogic) {
        this.color = color;

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

        final double HEIGHT = GameTile.getHexaTileHeight();
        final double WIDTH = (HEIGHT / 2 * cos(GameTile.getDir())) * 2;

        int i = 0;
        for (Insect insect : insects) {
            GameTile gameTile = new GameTile(insect);
            Coordinate c = new Coordinate(i * WIDTH + SPACE_BETWEEN_TILES, 0);
            i++;
            gameTile.setCoordinate(c);

            notifyOnAdd(gameTile);
        }
        HiveLogger.getLogger().info("{} Player created successfully", color);
    }

    public void removeGameTile(GameTile tile) {
        if (insectHolders.remove(tile)) {
            HiveLogger.getLogger().info("placeholder tile from {} was successfully deleted.", color);
            notifyOnRemove(tile);
        } else {
            HiveLogger.getLogger().fatal("placeholder tile specified was not found for {} player", color);
        }
    }

    public void removeInsect(Insect insect) {
        if (!insects.remove(insect)) {
            HiveLogger.getLogger().error("A nonexistent insect was to be removed from Player");
        }
    }

    public int getNeighboursOfQueen() {
        return queen.getTotalNeighbours();
    }

    public boolean isQueenDown() {
        return !insects.contains(queen);
    }

    public Queen getQueen() {
        return queen;
    }

    public HiveColor getColor() {
        return color;
    }

    public void addListener(ModelListener listener) {
        this.listener = listener;
    }

    public List<Insect> getInsects() {
        return insects;
    }

    public List<GameTile> getInsectHolders() {
        return insectHolders;
    }

    public void notifyListeners() {
        if (listener != null) {
            listener.onModelChange();
        }
    }

    public void notifyOnRemove(GameTile tile) {
        if (listener != null) {
            listener.onGameTileRemoved(tile);
        }
    }

    private void notifyOnAdd(GameTile tile) {
        if (listener != null) {
            listener.onGameTileAdded(tile);
        }
    }
}
