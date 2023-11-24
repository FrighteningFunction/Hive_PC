package org.game;

import static java.lang.System.exit;

public class GameTile {
    private boolean initialized;

    private GameBoard board = null;

    private Insect insect = null;

    private Coordinate coordinate;
    private GameTile[] neighbours = new GameTile[6];

    /**
     * Ez a konstruktor intézi az új GameTile felvételt.
     * Mind a 6 oldalon csekkolja, hogy össze kell-e kötni más Tile-okkal, és
     * ha kell, össze is köti velük
     * Ekkor a GameTile nem inicializált!
     * @param b a játéktábla.
     * @param c a létrehozandó GameTile koordinátája.
     */
    public GameTile(GameBoard b, Coordinate c){
        initialized=false;
        this.board = b;
        this.coordinate = c;
        for(int i=0; i<6; i++){
            board.linkTile(this, getCoordinateOfNeighbourAt(i), i);
        }
        try {
            board.addGameTile(this);
        }catch(DoubleTileException e){
            HiveLogger.getLogger().error("A new tile was created at an existing coordinate.");
        }
    }

    public void deleteGameTile(){
        board.removeGameTile(this);
        for(int i=0; i<6; i++){
            neighbours[i].removeNeighbour(GameBoard.invertDirection(i));
        }
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }

    public GameTile getNeigbour(int direction) {
        return neighbours[direction];
    }

    public void removeNeighbour(int direction){
        neighbours[direction] = null;
    }

    public void setNeighbour(GameTile neighbour, int direction){
        if(neighbour==null){HiveLogger.getLogger().warn("null GameTile-t kapott a setNeighbour");}
        neighbours[direction] = neighbour;
    }

    boolean isInitialized() {
        return initialized;
    }

    protected void setInitialized(boolean val){
        initialized=val;
        HiveLogger.getLogger().warn("GameTile setInitialized metódusa meghívva!");
    }

    public Coordinate getCoordinateOfNeighbourAt(int direction){
        double x1 = coordinate.getX();
        double y1 = coordinate.getY();
        double x2 = 0;
        double y2 = 0;

        switch(direction){
            case 0:
                x2=x1;
                y2=y1+1;
                break;
            case 1:
                x2=x1+0.5;
                y2=y1+0.5;
                break;
            case 2:
                x2=x1+0.5;
                y2=y1-0.5;
                break;
            case 3:
                x2=x1;
                y2=y1-1;
                break;
            case 4:
                x2=x1-0.5;
                y2=y1-0.5;
                break;
            case 5:
                x2=x1-0.5;
                y2=y1+0.5;
                break;
            default:
                HiveLogger.getLogger().fatal("Fatal: invalid direction got at coordinate calculation");
                exit(1);
        }
        return new Coordinate(x2, y2);
    }

    /**
     * Inicializálja a GameTile-t, vagyis ezen a ponton minden szomszédja létezni fog,
     * benne magában pedig bogárnak kell lennie.
     */
    void initialize() {
        if (initialized) {
            HiveLogger.getLogger().fatal("GameTile already initialized.");
            exit(1);
        } else {
            initialized=true;
            for(int i=0; i<6; i++){
                if(neighbours[i]==null){
                    neighbours[i]=new GameTile(board, getCoordinateOfNeighbourAt(i));
                    board.linkTile(this, neighbours[i].getCoordinate(), i);
                }else{
                    HiveLogger.getLogger().info("At a direction there already was a Tile.");
                }
            }
        }
    }
}
