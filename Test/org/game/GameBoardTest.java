package org.game;

import org.insects.Insect;
import org.insects.Queen;
import org.junit.*;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//todo: egy insect hozzáadása a hibák elcsitításához
public class GameBoardTest {
    private GameBoard board;

    private final Insect defaultInsect = new Queen(new Player(HiveColor.WHITE));

    @Before
    public void init(){
        board = GameBoard.getInstance();
    }

    @After
    public void cleanUp(){
        board.clear();
    }

    @Test
    public void newTile(){
        GameTile starter = new GameTile(board, new Coordinate(0,0));

            boolean noNeighbour = true;
            for (int i=0; i<6; i++){
                if(starter.getNeigbour(i)!=null){
                    noNeighbour=false;
                }
            }
            assertTrue("uninitialized starter Tile has a neighbour!", noNeighbour);
            assertTrue("The new Tile was not added to GameBoard or the search was incorrect", board.hasGameTile(starter));
    }

    @Test
    public void linkTilesTest(){
        GameTile origo = new GameTile(board, new Coordinate(0,0));
        GameTile north = new GameTile(board, new Coordinate(0,1));
        assertEquals("Neighbour tiles weren't linked consistently!",origo, north.getNeigbour(3));
    }

    @Test
    public void initialize_NoNullNeighbours(){
        GameTile tile = new GameTile(board, new Coordinate(0,0));
        tile.initialize(defaultInsect);
        int nullneighbours=0;
        for (int i=0; i<6; i++){
            if(tile.getNeigbour(i)==null){
                nullneighbours++;
            }
        }
        assertEquals("initialized tile has" + nullneighbours + " null neighbours!", 0, nullneighbours);
    }

    @Test
    public void initialize_ConsistentLinks(){
        GameTile tile = new GameTile(board, new Coordinate(0,0));
        tile.initialize(defaultInsect);
        int inconsistentLinks=0;
        for (int i=0; i<6; i++){
            if(!tile.equals(tile.getNeigbour(i).getNeigbour(GameBoard.invertDirection(i)))){
                inconsistentLinks++;
            }
        }
        assertEquals(inconsistentLinks+" neighbour links are inconsistent!", 0, inconsistentLinks);
    }

    @Test(expected=DoubleTileException.class)
    public void DoubleTileTest() throws DoubleTileException{
        GameTile tile = new GameTile(board, new Coordinate(0,0));
        board.addGameTile(new GameTile(board, new Coordinate(0,0)));
    }

    @Test
    public void removeGameTileCheck(){
        GameTile tile = new GameTile(board, new Coordinate(0.5, 0.5));
        tile.initialize(defaultInsect);
        GameTile[] neighbours= new GameTile[6];
        for(int i=0; i<6; i++){
            neighbours[i]=tile.getNeigbour(i);
        }
        tile.deleteGameTile();
        boolean referenceDeleted = true;
        int wrongreferences = 0;
        int k=3; //mert az első szomszéd a 3-as irányban rendelkezett a tile-al
        for(int i=0; i<6; i++){
            if(neighbours[i].getNeigbour(k%6)!=null){
                referenceDeleted=false;
                wrongreferences++;
            }
            k++;
        }
        assertTrue("Not all neighbours references were set to null. Non-null refs: "+wrongreferences,referenceDeleted);
    }
}
