package org.game;

public enum HiveColor{
    RED("red"),
    BLUE("blue");

    private final String name;

    HiveColor(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}