package org.game;

public enum HiveColor{
    BLACK("black"),
    WHITE("white");

    private final String name;

    HiveColor(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}