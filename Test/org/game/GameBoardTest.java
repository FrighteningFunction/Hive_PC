package org.game;

import org.insects.Insect;
import org.insects.Queen;
import org.junit.*;

import static org.junit.Assert.*;

public class GameBoardTest {
    private final double HEIGHT = GameTile.getHexaTileHeight();

    private static final double XGRID = Coordinate.getXGRID(); // assuming these are accessible
    private static final double YGRID = Coordinate.getYGRID();
    private GameBoard board;

    private GameLogic gameLogic;

    private Insect getInsect() {
        return new Queen(new Player(HiveColor.BLUE, gameLogic), gameLogic);
    }

    @Before
    public void init() {
        board = GameBoard.getInstance();
    }

    @After
    public void cleanUp() {
        board.clear();
    }

    @Test
    public void testEqualsMethod() {
        Coordinate coord1 = new Coordinate(0, 0);

        // Test for exact same object
        assertEquals("Same objects should be equal", coord1, coord1);

        // Test for null and different class
        assertFalse("Coordinate should not be equal to null", coord1.equals(null));
        assertFalse("Coordinate should not be equal to a different class", coord1.equals(new Object()));

        // Test for two coordinates that are the same
        Coordinate coord2 = new Coordinate(0, 0);
        assertEquals("Coordinates with same values should be equal", coord1, coord2);

        // Test for coordinates that are different but round to the same value
        Coordinate coord3 = new Coordinate(XGRID / 2 - 0.001, YGRID / 2 - 0.001);
        assertEquals("Coordinates rounding to the same value should be equal", coord1, coord3);

        // Test for coordinates that are just over the rounding threshold
        Coordinate coord4 = new Coordinate(XGRID / 2 + 0.001, YGRID / 2 + 0.001);
        assertFalse("Coordinates rounding to different values should not be equal", coord1.equals(coord4));
    }


    @Test
    public void testHashingAtDeltaBoundaries() {
        new GameTile(board, new Coordinate(0, 0));

        double xDelta = Coordinate.getXGRID() / 2; //Ez pont a kerekítés határa
        double yDelta = Coordinate.getYGRID() / 2;

        // Test coordinates just within the delta
        assertTrue("Tile should exist just within XDELTA", board.hasGameTile(new Coordinate(xDelta - 0.001, 0)));
        assertTrue("Tile should exist just within YDELTA", board.hasGameTile(new Coordinate(0, yDelta - 0.001)));

        // Test coordinates just outside the delta
        assertTrue("Tile should not exist just outside XDELTA", !board.hasGameTile(new Coordinate(xDelta + 0.001, 0)));
        assertTrue("Tile should not exist just outside YDELTA", !board.hasGameTile(new Coordinate(0, yDelta + 0.001)));
    }

    @Test
    public void newTile() {
        GameTile starter = new GameTile(board, new Coordinate(0, 0));

        boolean noNeighbour = true;
        for (int i = 0; i < 6; i++) {
            if (starter.getNeighbour(i) != null) {
                noNeighbour = false;
            }
        }
        assertTrue("uninitialized starter Tile has a neighbour!", noNeighbour);
        assertTrue("The new Tile was not added to GameBoard or the search was incorrect", board.hasGameTile(starter));
    }

    @Test
    public void linkTilesTest1() {
        GameTile origo = new GameTile(board, new Coordinate(0, 0));
        GameTile north = new GameTile(board, new Coordinate(0, HEIGHT));
        assertEquals("Neighbour tiles weren't linked consistently!", origo, north.getNeighbour(3));
        assertEquals("Neighbour tiles weren't linked consistently!", north, origo.getNeighbour(0));
    }

    @Test
    public void linkTilesTest2() {
        GameTile origo = new GameTile(board, new Coordinate(0, 0));
        origo.initialize(getInsect());
        for (int i = 0; i < 6; i++) {
            assertEquals("", origo.getNeighbour(i).getNeighbour(GameBoard.invertDirection(i)), origo);
        }
    }

    @Test
    public void initialize_NoNullNeighbours() {
        GameTile tile = new GameTile(board, new Coordinate(0, 0));
        tile.initialize(getInsect());
        int nullneighbours = 0;
        for (int i = 0; i < 6; i++) {
            if (tile.getNeighbour(i) == null) {
                nullneighbours++;
            }
        }
        assertEquals("initialized tile has" + nullneighbours + " null neighbours!", 0, nullneighbours);
    }

    @Test
    public void initialize_ConsistentLinks() {
        GameTile tile = new GameTile(board, new Coordinate(0, 0));
        tile.initialize(getInsect());
        int inconsistentLinks = 0;
        for (int i = 0; i < 6; i++) {
            if (!tile.equals(tile.getNeighbour(i).getNeighbour(GameBoard.invertDirection(i)))) {
                inconsistentLinks++;
            }
        }
        assertEquals(inconsistentLinks + " neighbour links are inconsistent!", 0, inconsistentLinks);
    }

    @Test(expected = DoubleTileException.class)
    public void DoubleTileTest() throws DoubleTileException {
        GameTile tile = new GameTile(board, new Coordinate(0, 0));
        board.addGameTile(new GameTile(board, new Coordinate(0, 0)));
    }

    @Test
    public void removeGameTileCheck() {
        GameTile tile = new GameTile(board, new Coordinate(0, 0));
        tile.initialize(getInsect());
        GameTile[] neighbours = new GameTile[6];
        for (int i = 0; i < 6; i++) {
            neighbours[i] = tile.getNeighbour(i);
        }
        tile.setInitialized(false);
        tile.deleteGameTileFromBoard();
        boolean referenceDeleted = true;
        int wrongreferences = 0;
        int k = 3; //mert az első szomszéd a 3-as irányban rendelkezett a tile-al
        for (int i = 0; i < 6; i++) {
            if (neighbours[i].getNeighbour(k % 6) != null) {
                referenceDeleted = false;
                wrongreferences++;
            }
            k++;
        }
        assertTrue("Not all neighbours references were set to null. Non-null refs: " + wrongreferences, referenceDeleted);
    }

    @Test
    public void uninitializeGameTileTest() {
        GameTile tile1 = new GameTile(board, new Coordinate(0, 0));
        GameTile tile2 = new GameTile(board, new Coordinate(0, HEIGHT));
        tile1.initialize(getInsect());
        tile2.initialize(getInsect());
        assertEquals("Not correct amount of tiles on board", 10, board.getSize());
        tile2.uninitialize();
        assertEquals("Tiles were not deleted/set correctly after uninitialization", 7, board.getSize());
    }
}
