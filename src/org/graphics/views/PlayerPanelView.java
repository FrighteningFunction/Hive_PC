package org.graphics.views;

import org.game.Coordinate;
import org.game.GraphicLogger;

import javax.swing.*;
import java.awt.*;

public class PlayerPanelView extends JPanel{

    public PlayerPanelView(){
        this.setLayout(null);
        setVisible(true);
        GraphicLogger.getLogger().info("PlayerPanelView created successfully.");
    }
}
