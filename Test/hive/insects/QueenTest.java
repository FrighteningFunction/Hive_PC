package hive.insects;

import hive.game.Coordinate;
import hive.game.GameLogic;
import hive.game.GameTile;
import hive.game.Player;
import org.game.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class QueenTest {
    private GameLogic gameLogic=GameLogic.getInstance();
    private GameTile origoTile;
    private Player whitePlayer= gameLogic.getWhitePlayer();

    private Player blackPlayer= gameLogic.getWhitePlayer();

    private Queen whiteQueen=new Queen(whitePlayer, gameLogic);

    private Insect dummyInsect = new Grasshopper(blackPlayer, gameLogic);

    @Before
    public void init() {
        gameLogic.newGameForTesting();
        origoTile = new GameTile(gameLogic.getBoard(), new Coordinate(0, 0));
    }

    @Test
    public void testPlaceQueen() {
        // Test placing the Queen on the board, considering game turns
        gameLogic.setTurns(1); // Simulating turn 1
        assertTrue("Queen should be placed in the first turn", whiteQueen.place(origoTile));
    }

    @Test
    public void testMoveQueen() {
        // Test moving the Queen, considering hive connectivity and turn rules
        gameLogic.setTurns(1);
        whiteQueen.place(origoTile);
        gameLogic.setTurns(2);
        dummyInsect.place(origoTile.getNeighbour(1));
        gameLogic.setTurns(3);
        GameTile adjacentTile = origoTile.getNeighbour(0); // Assuming neighbour 0 is valid and adjacent
        assertTrue("Queen should be able to move to an adjacent tile", whiteQueen.move(adjacentTile));
    }

    @Test
    public void testPingAvailableTiles() {
        // Test available tiles for Queen's movement, respecting hive rules
        gameLogic.setTurns(1);
        whiteQueen.place(origoTile);
        gameLogic.setTurns(2);
        dummyInsect.place(origoTile.getNeighbour(1));
        Set<GameTile> expected = new HashSet<>();
        expected.add(origoTile.getNeighbour(0));
        expected.add(origoTile.getNeighbour(2));
        Set<GameTile> availableTiles = whiteQueen.pingAvailableTiles();
        assertEquals("Available tiles should include adjacent tiles", expected, availableTiles);
    }
}