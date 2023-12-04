package hive.game;

public enum HiveColor{
    ORANGE("orange"),
    BLUE("blue");

    private final String name;

    HiveColor(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}