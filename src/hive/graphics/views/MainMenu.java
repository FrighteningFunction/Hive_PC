package hive.graphics.views;

import hive.Commands;
import hive.GraphicLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;

public class MainMenu extends JPanel {
    private transient JButton newGame = new JButton("New game");

    private transient JButton exit = new JButton ("exit");

    private transient MenuButtonsPressed menuListener = new MenuButtonsPressed();

    JPanel mainPanel = new JPanel();

    public MainMenu(){
        this.setLayout(new GridBagLayout());
        this.add(mainPanel);
        newGame.setActionCommand("newGame");
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setActionCommand("exit");
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(newGame);
        mainPanel.add(exit);

        newGame.addActionListener(menuListener);
        exit.addActionListener(menuListener);

        GraphicLogger.getLogger().info("MainMenu created successfully.");
    }

    class MenuButtonsPressed implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String c = e.getActionCommand();
            switch (c) {
                case "newGame":
                    Commands.startNewGame();
                    break;
                case "exit":
                    exit(0);
                    break;
                default:
                    GraphicLogger.getLogger().fatal("Unknown MainMenu Command issued!");
                    exit(1);
                    break;
            }
        }
    }
}
