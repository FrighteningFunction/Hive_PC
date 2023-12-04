package hive.graphics.views;

import hive.game.Coordinate;
import hive.game.GameTile;
import hive.game.HiveColor;
import hive.game.TileStates;
import hive.insects.Insect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.cos;

/**
 * A játék mezőit megjelenítő, primitív rajzolási logikával ellátott view.
 */
public class GameTileView extends JComponent {
    private final transient Logger logger = LogManager.getLogger();

    private transient TileStates state = null;

    private boolean initialized = false;

    /**
     * A tile kiszínezéséhez kell. Mindenképpen be kell állítani.
     */
    private Color color = null;

    /**
     * A JComponent abszolút koordinátája a konténerben.
     */
    private transient Coordinate c = null;

    private transient Insect insect = null;

    //Konstansok

    private final Dimension fixedSize;

    private static final float BORDER_WIDTH = 2.0f;

    private static final double TILE_HEIGHT = GameTile.getHexaTileHeight();

    private static final double TILE_RADIUS = GameTile.getTileRadius();

    //A komponens abszolút szélessége és magassága
    private static final int COMP_WIDTH = (int) (Math.round(2 * TILE_RADIUS)+BORDER_WIDTH);
    private static final int COMP_HEIGHT = (int) Math.round(TILE_HEIGHT+BORDER_WIDTH);


    /**
     * Megmutatja, hogy hol található a hatszög középpontja a JComponent koordinátáihoz képest.
     */
    private static final Coordinate INNER_ORIGO = new Coordinate(TILE_RADIUS, TILE_HEIGHT / 2);

    public GameTileView(Coordinate c) {
        fixedSize = new Dimension(COMP_WIDTH, COMP_HEIGHT);

        // Set the size of the hex tile
        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        this.c = c;

        setBounds((int) c.getX(), (int) c.getY(), COMP_WIDTH, COMP_HEIGHT);

        setVisible(true);
        logger.info("GameTileView at coordinate x: {} y: {} was created.", c.getX(), c.getY());
    }

    /**
     * Konstruktor elsősorban tesztelésre.
     */
    public GameTileView() {
        fixedSize = new Dimension(COMP_WIDTH, COMP_HEIGHT);

        // Set the size of the hex tile
        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        setBounds((int) c.getX(), (int) c.getY(), COMP_WIDTH, COMP_HEIGHT);
        logger.warn("The GameTileView's constructor intended for testing was called!");
    }

    public void setState(TileStates state) {
        this.state = state;
    }

    /**
     * Beállítja a színt a játékosnak megfelelően.
     * @param color a tulajdonos színe.
     */
    public void setColor(HiveColor color) {
        if (color == HiveColor.ORANGE) {
            this.color = Color.ORANGE;
        } else if (color == HiveColor.BLUE) {
            this.color = Color.CYAN;
        } else {
            this.color = Color.GREEN;
            logger.error("Invalid color was set for GameTileView at ({}, {})", c.getX(), c.getY());
        }
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setC(Coordinate c) {
        this.c = c;
        setBounds((int) c.getX(), (int) c.getY(), COMP_WIDTH, COMP_HEIGHT);
    }

    public void setInsect(Insect insect) {
        this.insect = insect;
    }

    public static int getBorderWidth() {
        return Math.round(BORDER_WIDTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        boolean hexagonDrawn = false;

        // Draw the main hexagon if the tile is initialized
        if (initialized) {
            drawHexagon(g2d, color);
            hexagonDrawn = true;
            logger.info("A {} hexagon was drawn.", color);
        }

        // Draw border or full hexagon based on state
        switch (state) {
            case SELECTED:
                if (initialized) {
                    drawHexagonBorder(g2d, Color.GREEN, true);
                    logger.info("A green outer border was drawn.");
                } else {
                    drawHexagonBorder(g2d, Color.GREEN, false);
                    logger.info("A green inner border was drawn.");
                }
                hexagonDrawn = true;
                break;
            case PINGED:
                if (initialized) {
                    drawHexagonBorder(g2d, Color.RED, true);
                    logger.info("A red outer border was drawn.");
                } else {
                    drawHexagonBorder(g2d, Color.RED, false);
                    logger.info("A red inner border was drawn.");
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

        //Kirajzoljuk a rovart, ha szükséges.
        if (initialized && insect != null && insect.getImage() != null) {
            Image resizedImage = resizeImageToFitTile(insect.getImage());
            Coordinate imgCord = getCenteredImageCoordinate(resizedImage);
            g2d.drawImage(resizedImage, (int) imgCord.getX(), (int) imgCord.getY(), this);
            logger.info("An insect image was drawn.");
        }
    }

    private void drawHexagonBorder(Graphics2D g2d, Color borderColor, boolean isOuter) {
        double borderSize;

        if (isOuter) {
            borderSize = TILE_RADIUS + BORDER_WIDTH;
        } else {
            borderSize = TILE_RADIUS;
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

    /**
     * Átméretezi a kapott képet, hogy elférjen a tile-ban.
     * @param originalImage az átméretezendő kép.
     * @return az átméretezett kép.
     */
    private Image resizeImageToFitTile(Image originalImage) {
        int maxSize = (int) (TILE_HEIGHT);

        int originalWidth = originalImage.getWidth(this);
        int originalHeight = originalImage.getHeight(this);

        // Fenntartjuk az arányt
        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth;
        int newHeight;

        if (originalWidth > originalHeight) {
            // A szélesség a gátoló tényező
            newWidth = maxSize;
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            // A magasság a gátoló tényező
            newHeight = maxSize;
            newWidth = (int) (newHeight * aspectRatio);
        }

        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    /**
     * Kiszámolja azt a bal felső koordinátát, ahol a képet rajzolni kell, hogy középen legyen.
     *
     * @param image A középre helyezendő kép.
     * @return A koordináta, ahol a rajzolást kezdjük.
     */
    public Coordinate getCenteredImageCoordinate(Image image) {
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        int componentWidth = this.getWidth();
        int componentHeight = this.getHeight();

        // Kiszámolju a bal felső x-y koordinátákat
        int x = (componentWidth - imageWidth) / 2;
        int y = (componentHeight - imageHeight) / 2;

        return new Coordinate(x, y);
    }

    public void addClickListener(MouseListener listener) {
        this.addMouseListener(listener);
    }
}
