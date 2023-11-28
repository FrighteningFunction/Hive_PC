package org.graphics.views;

import org.game.Coordinate;
import org.game.GameTile;
import org.game.TileStates;
import org.insects.Insect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.cos;

public class GameTileView extends JComponent {
    private final transient Logger logger = LogManager.getLogger();

    private transient TileStates states = TileStates.UNSELECTED;

    private boolean initialized = false;

    private transient Coordinate c;

    private transient Insect insect = null;

    private static final double TILE_HEIGHT = GameTile.getHexaTileHeight();

    private static final double DIR = GameTile.getDir();

    private static final double TILE_RADIUS = TILE_HEIGHT / 2 * cos(DIR);

    public GameTileView(Coordinate c) {
        this.c = c;
    }

    public void setStates(TileStates states) {
        this.states = states;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setC(Coordinate c) {
        this.c = c;
    }

    public void setInsect(Insect insect) {
        this.insect = insect;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        boolean hexagonDrawn = false;

        // Draw the main hexagon if the tile is initialized
        if (initialized) {
            drawHexagon(g2d, Color.BLACK);
            hexagonDrawn = true;
        }

        // Draw border or full hexagon based on state
        switch (states) {
            case SELECTED:
                if (initialized) {
                    drawHexagonBorder(g2d, Color.GREEN);
                    logger.info("A black hexagon with a green border was drawn.");
                } else {
                    drawHexagon(g2d, Color.GREEN);
                    logger.info("A full green hexagon was drawn.");
                }
                hexagonDrawn = true;
                break;
            case PINGED:
                if (initialized) {
                    drawHexagonBorder(g2d, Color.RED);
                    logger.info("A black hexagon with a red border was drawn.");
                } else {
                    drawHexagon(g2d, Color.RED);
                    logger.info("A full red hexagon was drawn.");
                }
                hexagonDrawn = true;
                break;
            case UNSELECTED:
                //Nem rajzolunk, nem loggolunk semmit.
                break;
            case TERMINATED:
                logger.fatal("Terminated tile was to be drawn!");
                break;
        }

        if (!hexagonDrawn) {
            logger.info("No hexatile was drawn.");
        }


        // Draw insect image if initialized
        if (initialized && insect != null && insect.getImage() != null) {
            Image resizedImage = resizeImageToFitTile(insect.getImage());
            g2d.drawImage(resizedImage, (int) c.getX(), (int) c.getY(), this);
            logger.info("An insect image was drawn.");
        }
    }

    private void drawHexagonBorder(Graphics2D g2d, Color borderColor) {
        double borderSize = TILE_RADIUS + 3; // Adjust the border size as needed
        g2d.setColor(borderColor);
        Shape borderHexagon = createHexagonWithSize(borderSize);
        g2d.draw(borderHexagon); // Draw border hexagon
    }

    private void drawHexagon(Graphics2D g2d, Color color) {
        g2d.setColor(color);
        Shape hexagon = createHexagonWithSize(TILE_RADIUS);
        g2d.fill(hexagon); // Fill hexagon for solid color
    }

    private Shape createHexagonWithSize(double size) {
        double x = c.getX();
        double y = c.getY();
        Path2D path = new Path2D.Double();
        double angleStep = Math.PI / 3;
        path.moveTo(x + size * cos(0), y + size * Math.sin(0));
        for (int i = 1; i <= 6; i++) {
            path.lineTo(x + size * cos(i * angleStep), y + size * Math.sin(i * angleStep));
        }
        path.closePath();
        return path;
    }

    private Image resizeImageToFitTile(Image originalImage) {
        int maxSize = (int) (TILE_RADIUS * 2);

        int originalWidth = originalImage.getWidth(this);
        int originalHeight = originalImage.getHeight(this);

        // Maintain aspect ratio
        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth;
        int newHeight;

        if (originalWidth > originalHeight) {
            // Width is the limiting dimension
            newWidth = maxSize;
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            // Height is the limiting dimension
            newHeight = maxSize;
            newWidth = (int) (newHeight * aspectRatio);
        }

        // Resize the image
        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    public void addClickListener(MouseListener listener) {
        this.addMouseListener(listener);
    }
}
