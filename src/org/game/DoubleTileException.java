package org.game;

public class DoubleTileException extends Exception{
    DoubleTileException(){
        super("Duplikált koordinátájú játékmező! Fatális hiba");
        HiveLogger.getLogger().error("Duplikált játékmező");
    }
}
