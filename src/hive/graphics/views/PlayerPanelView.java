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
    private static final String name = "PlayerPanelView";

    List<ModelListener> listeners = new ArrayList<>();

    public PlayerPanelView(){
        this.setLayout(null);

        //beállítjuk az összes métetparaméterét, hogy az anya boardlayoutja ne zsugorítsa össze
        int hexaTileViewWidth = (int) (Math.round(2 * GameTile.getTileRadius())+GameTileView.getBorderSize());
        int hexaTileViewHeight = (int) Math.round(GameTile.getHexaTileHeight()+GameTileView.getBorderSize());

        int width = hexaTileViewWidth*11;
        int height = hexaTileViewHeight;
        Dimension fixedSize = new Dimension(width, height);

        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        this.addComponentListener(new ResizeListener(listeners, name));

        setVisible(true);
        GraphicLogger.getLogger().info("PlayerPanelView created successfully.");
    }

    @Override
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }
}
