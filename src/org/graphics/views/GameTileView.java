package org.graphics.views;

import org.game.Coordinate;
import org.game.GameTile;
import org.game.TileStates;
import org.insects.Insect;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.cos;

//todo: játékosszíneknek megfelelően is kéne rajzolni
public class GameTileView extends JComponent {
    private final transient Logger logger = LogManager.getLogger();

    private transient TileStates state = TileStates.UNSELECTED;

    private boolean initialized = false;

    /**
     * A JComponent abszolút koordinátája a konténerben.
     */
    private transient Coordinate c = null;

    private transient Insect insect = null;

    //Konstansok

    private Dimension fixedSize;
    private static final int BORDER_SIZE = 3;

    private static final float BORDER_WIDTH = 2.0f;

    private static final double TILE_HEIGHT = GameTile.getHexaTileHeight();

    private static final double TILE_RADIUS = GameTile.getTileRadius();


    /**
     * Megmutatja, hogy hol található a hatszög középpontja a JComponent koordinátáihoz képest.
     */
    private static final transient Coordinate INNER_ORIGO = new Coordinate(TILE_RADIUS, TILE_HEIGHT/2);

    public GameTileView(Coordinate c) {

        // Calculate the width and height of the hexagon's bounding rectangle
        int width = (int) (Math.round(2 * TILE_RADIUS)+BORDER_SIZE);
        int height = (int) Math.round(TILE_HEIGHT+BORDER_SIZE);
        fixedSize = new Dimension(width, height);

        // Set the size of the hex tile
        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        this.c=c;

        setBounds((int) c.getX(), (int) c.getY(), width, height);

        Border redBorder = BorderFactory.createLineBorder(Color.PINK, 2);
        this.setBorder(redBorder);

        setVisible(true);
        logger.info("GameTileView at coordinate x: {} y: {} was created.", c.getX(), c.getY());
    }

    /**
     * Konstruktor elsősorban tesztelésre.
     */
    public GameTileView(){
        // Calculate the width and height of the hexagon's bounding rectangle
        int width = (int) (Math.round(2 * TILE_RADIUS));
        int height = (int) Math.round(TILE_HEIGHT);
        fixedSize = new Dimension(width, height);

        // Set the size of the hex tile
        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        setBounds((int) c.getX(), (int) c.getY(), width, height);
        logger.warn("The GameTileView's constructor intended for testing was called!");
    }

    public void setState(TileStates state) {
        this.state = state;
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
            drawHexagonBorder(g2d, Color.BLACK, false);
            hexagonDrawn = true;
            logger.info("A black hexagon was drawn.");
        }

        // Draw border or full hexagon based on state
        switch (state) {
            case SELECTED:
                if (initialized) {
                    drawHexagonBorder(g2d, Color.GREEN, true);
                    logger.info("A green border was drawn.");
                } else {
                    drawHexagonBorder(g2d, Color.GREEN, false);
                    logger.info("A full green hexagon was drawn.");
                }
                hexagonDrawn = true;
                break;
            case PINGED:
                if (initialized) {
                    drawHexagonBorder(g2d, Color.RED, true);
                    logger.info("A red border was drawn.");
                } else {
                    drawHexagonBorder(g2d, Color.RED, false);
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

        //Draw insect image if initialized
        if (initialized && insect != null && insect.getImage() != null) {
            Image resizedImage = resizeImageToFitTile(insect.getImage());
            Coordinate imgCord = getCenteredImageCoordinate(resizedImage);
            g2d.drawImage(resizedImage, (int) imgCord.getX(), (int) imgCord.getY(), this);
            logger.info("An insect image was drawn.");
        }
    }

    private void drawHexagonBorder(Graphics2D g2d, Color borderColor, boolean isOuter) {
        double borderSize;

        if(isOuter) {
            borderSize = TILE_RADIUS + BORDER_SIZE;
        }else{
            borderSize=TILE_RADIUS;
        }
        g2d.setColor(borderColor);

        g2d.setStroke(new BasicStroke(BORDER_WIDTH));
        Shape borderHexagon = createHexagonWithSize(borderSize);
        g2d.draw(borderHexagon); // Draw border hexagon
    }

    private void drawHexagon(Graphics2D g2d, Color color) {
        g2d.setColor(color);
        Shape hexagon = createHexagonWithSize(TILE_RADIUS);
        g2d.fill(hexagon); // Fill hexagon for solid color
    }

    private Shape createHexagonWithSize(double size) {
        double x = INNER_ORIGO.getX();
        double y = INNER_ORIGO.getY();
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
        int maxSize = (int) (TILE_HEIGHT);

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

    /**
     * Calculates the top-left coordinate at which the image should be drawn to be centered.
     *
     * @param image The image to be centered.
     * @return The coordinate at which to start drawing the image.
     */
    public Coordinate getCenteredImageCoordinate(Image image) {
        // Get the width and height of the image
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        // Get the width and height of the component
        int componentWidth = this.getWidth();
        int componentHeight = this.getHeight();

        // Calculate the top-left x and y coordinates to start drawing the image
        int x = (componentWidth - imageWidth) / 2;
        int y = (componentHeight - imageHeight) / 2;

        // Return the calculated coordinates
        return new Coordinate(x, y);
    }

    public void addClickListener(MouseListener listener) {
        this.addMouseListener(listener);
    }
}
