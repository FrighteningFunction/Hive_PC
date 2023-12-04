package hive.game;

import hive.insects.Queen;
import hive.insects.Grasshopper;
import hive.insects.Insect;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class GameLogicTest {
    private GameBoard board;

    private GameLogic gameLogic;

    private Insect queen;

    GameTile origoTile;

    @Before
    public void init(){
        board = GameBoard.getInstance();
        gameLogic = GameLogic.getInstance();
        gameLogic.newGameForTesting();
        origoTile = new GameTile(board, new Coordinate(0,0));
        queen = new Queen(gameLogic.getBluePlayer(), gameLogic);
        queen.place(origoTile);
    }

    @Test
    public void initializedTileSetTest(){
        Insect grasshopper = new Grasshopper(gameLogic.getOrangePlayer(), gameLogic);
        grasshopper.place(origoTile.getNeighbour(0));
        Set<GameTile> expected = new HashSet<>();
        expected.add(origoTile);
        expected.add(origoTile.getNeighbour(0));
        assertEquals("Incorrect initializedTile set", expected, board.getInitializedTileSet());
    }

    @Test
    public void uninitializedTileSetTest(){
        Set<GameTile> expected = new HashSet<>();
        for(int i=0; i<6; i++){
            expected.add(origoTile.getNeighbour(i));
        }
        assertEquals("Incorrect uninitializedTile set", expected, board.getUnInitializedTileSet());
    }

    @Test
    public void hiveConnectionTest(){
        Insect grasshopper1 = new Grasshopper(gameLogic.getOrangePlayer(), gameLogic);
        GameTile grasshopper1Tile = origoTile.getNeighbour(0);
        grasshopper1.place(grasshopper1Tile);
        Insect grasshopper2 = new Grasshopper(gameLogic.getOrangePlayer(), gameLogic);

        GameTile grasshopper2Tile = origoTile.getNeighbour(3);
        grasshopper2.place(grasshopper2Tile);

        assertTrue("False alert for hiveConnection problem", gameLogic.wouldHiveBeConnected(grasshopper2Tile));
        assertFalse("No Alert for hiveConnection problem when there is one", gameLogic.wouldHiveBeConnected(origoTile));
    }
}
