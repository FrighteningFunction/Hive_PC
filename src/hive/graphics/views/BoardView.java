package hive.graphics.views;

import hive.GraphicLogger;
import hive.graphics.controllers.ModelListener;
import hive.graphics.controllers.ResizeListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BoardView extends JPanel implements TileView{
    private static final String name = "BoardView";

    private List<ModelListener> listeners = new ArrayList<>();

    public BoardView() {
        this.setLayout(null);
        setVisible(true);
        GraphicLogger.getLogger().info("BoardView was successfully created.");

        this.addComponentListener(new ResizeListener(listeners, name));
    }

    public void addListener(ModelListener listener){
        listeners.add(listener);
    }
}
