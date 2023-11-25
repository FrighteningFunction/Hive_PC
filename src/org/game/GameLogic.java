package org.game;

import java.util.HashSet;
import java.util.Set;

public class GameLogic {

    private GameLogic() {
    }

    /**
     * DFS algoritmussal bejárjuk a játéktáblát a megadott tile-tól kezdve.
     * A végén a paraméterként megadott visited az összes elérhető tile-t kiadja.
     * @param visited a meglátogatott tile-ok halmaza.
     * @param root a gyökér, ahonnan éppen indulunk
     */
    private static void pathFinder(Set<GameTile> visited, GameTile root) {
        visited.add(root);
        for(int i=0; i<6; i++){
            GameTile to = root.getNeigbour(i);
            if(to.isInitialized() && !visited.contains(to)){
                pathFinder(visited, to);
            }
        }
    }

    /**
     * DFS-t segítségül hívva megállapítja, hogy a játéktábla összefüggő lenne-e,
     * ha az adott rovart elmozdítanánk (ak. a tile-ját uninicializálnánk)
     * @param moveStart a tile, ahonnan a rovar mozogna.
     * @return true, ha összefüggő maradna vagy a tábla a tábla legfeljebb egy inicializált tile-t tartalmaz,
     * hamis, ha legalább 2 részre szétszakadna.
     */
    public static boolean wouldHiveBeConnected(GameTile moveStart) {
        GameBoard board = GameBoard.getInstance();

        Set<GameTile> newState = board.getInitializedTileSet();
        newState.remove(moveStart);
        GameTile checkOrigo;

        while(true){
            GameTile assign = board.getInitializedTile();
            if(assign==null){
                HiveLogger.getLogger().error("a wouldHiveBeConnected() was called for an either empty or 1-insect board.");
                return true;
            }
            if(!assign.equals(moveStart)){
                checkOrigo=assign;
                break;
            }
        }

        Set<GameTile> reachables = new HashSet<>();
        pathFinder(reachables, checkOrigo);
        return newState.equals(reachables);
    }

    public static Set<GameTile> pingAvailableTilesForPlacing(Player p){
        HiveColor playerColor = p.color;

        GameBoard board = GameBoard.getInstance();
        Set<GameTile> unInitializedTiles = board.getUnInitializedTileSet();
        Set<GameTile> availableTiles = new HashSet<>();

        for(GameTile tile : unInitializedTiles){
            boolean noOpponentNeighbour = true;
            for(int i=0; i<6; i++){
                GameTile neighbour = tile.getNeigbour(i);
                if(neighbour.isInitialized() && !neighbour.getInsect().color.equals(playerColor)){
                    noOpponentNeighbour = false;
                    break;
                }
            }
            if(noOpponentNeighbour){
                availableTiles.add(tile);
            }
        }

        return availableTiles;

    }
}
