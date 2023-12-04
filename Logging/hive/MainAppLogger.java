package hive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A fő applikáció eseményeit loggoló osztály.
 */
public class MainAppLogger {
    private static final Logger logger = LogManager.getLogger(MainAppLogger.class);

    private MainAppLogger(){}

    public static Logger getLogger(){
        return logger;
    }
}
