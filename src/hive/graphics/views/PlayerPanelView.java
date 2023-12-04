package hive.graphics.views;

import hive.game.GameTile;
import hive.GraphicLogger;
import hive.graphics.controllers.ModelListener;
import hive.graphics.controllers.ResizeListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import java.util.List;

public class PlayerPanelView extends JPanel implements TileView{
    private static final String NAME = "PlayerPanelView";

    private final List<ModelListener> listeners = new ArrayList<>();

    public PlayerPanelView(){
        this.setLayout(null);

        //beállítjuk az összes métetparaméterét, hogy az anya boardlayoutja ne zsugorítsa össze
        int hexaTileViewWidth = (int) (Math.round(2 * GameTile.getTileRadius())+GameTileView.getBorderWidth());
        int hexaTileViewHeight = (int) Math.round(GameTile.getHexaTileHeight()+GameTileView.getBorderWidth());

        int width = hexaTileViewWidth*11;
        int height = hexaTileViewHeight;
        Dimension fixedSize = new Dimension(width, height);

        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        this.addComponentListener(new ResizeListener(listeners, NAME));

        setVisible(true);
        GraphicLogger.getLogger().info("PlayerPanelView created successfully.");
    }

    @Override
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }
}
