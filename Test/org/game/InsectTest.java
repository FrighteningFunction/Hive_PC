package org.game;
import org.insects.*;

import org.junit.After;
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
        GameLogic.newGame();
        gameLogic = GameLogic.getInstance();
        whiteQueen=new Queen(gameLogic.getWhitePlayer(), gameLogic);

        origoTile = new GameTile(board, new Coordinate(0,0));
        whiteQueen.place(origoTile);
        starterTile = origoTile.getNeigbour(3);
    }
    @After
    public void cleanUp(){
        board.clear();
    }

    @Test
    public void insectPlaceTest(){
    }

    @Test
    public void grasshopperTest(){
        Insect grasshopper = new Grasshopper(gameLogic.getBlackPlayer(), gameLogic);
        grasshopper.place(starterTile);
        Set<GameTile> expected = new HashSet<>();
        expected.add(origoTile.getNeigbour(0));
        assertEquals("Incorrect availableTiles!", expected, grasshopper.pingAvailableTiles());
    }
}
