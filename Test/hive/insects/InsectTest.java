package hive.insects;
import hive.game.Coordinate;
import hive.game.GameBoard;
import hive.game.GameLogic;
import hive.game.GameTile;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class InsectTest {
    private static final double XGRID = Coordinate.getXGRID();
    private static final double YGRID = Coordinate.getYGRID();
    private GameBoard board;
    private GameLogic gameLogic;

    private GameTile origoTile;

    private GameTile starterTile;

    private Insect whiteQueen;

    /**
     * A teszteléshez felállítunk 1 inicializált tile-t,
     * valamint annak déli (itt még inicializálatlan) szomszédját pingeljük
     * A későbbi tesztesetek azzal a starterTile-lal fognak futni.
     */
    @Before
    public void init(){
        board=GameBoard.getInstance();
        gameLogic = GameLogic.getInstance();
        gameLogic.newGameForTesting();
        gameLogic.setTurns(2);
        whiteQueen=new Queen(gameLogic.getOrangePlayer(), gameLogic);

        origoTile = new GameTile(board, new Coordinate(0,0));
        whiteQueen.place(origoTile);
        starterTile = origoTile.getNeighbour(3);
    }

    @Test
    public void insectPlaceTest(){
        Insect grasshopper = new Grasshopper(gameLogic.getBluePlayer(), gameLogic);
        Set<GameTile> expected = new HashSet<>(board.getUnInitializedTileSet());
        assertEquals("Incorrect gametiles pinged available for placing", expected, gameLogic.pingAvailableTilesForPlacing(gameLogic.getBluePlayer()));

        GameTile chosenTile = origoTile.getNeighbour(3);
        grasshopper.place(chosenTile);
        assertTrue("Insect was not placed as it should have been", chosenTile.isInitialized());
    }

    @Test
    public void InsectMoveTest1(){
        Insect grasshopper = new Grasshopper(gameLogic.getBluePlayer(), gameLogic);
        grasshopper.place(starterTile);
        Set<GameTile> expected = new HashSet<>();
        expected.add(origoTile.getNeighbour(0));

        assertEquals("Incorrect availableTiles!", expected, grasshopper.pingAvailableTiles());

        GameTile chosenTile = origoTile.getNeighbour(0);
        grasshopper.move(chosenTile);
        assertTrue("Insect was not moved as it should have been", chosenTile.isInitialized());
        assertFalse("Starter Tile was not uninitialized", starterTile.isInitialized());
    }

    @Test
    public void InsectMoveTest2(){
        Insect grasshopper = new Grasshopper(gameLogic.getBluePlayer(), gameLogic);
        grasshopper.place(starterTile);

        GameTile chosenTile = origoTile.getNeighbour(2);
        grasshopper.move(chosenTile);
        assertFalse("Insect should not have been moved!", chosenTile.isInitialized());
        assertTrue("Starter Tile was uninitialized even though insect did not move", starterTile.isInitialized());
    }
}
