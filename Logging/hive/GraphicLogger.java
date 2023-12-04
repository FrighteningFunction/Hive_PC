package hive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Az app grafikus eseményeit loggoló osztály.
 */
public class GraphicLogger {
    private static final Logger logger = LogManager.getLogger(GraphicLogger.class);

    private GraphicLogger(){}

    public static Logger getLogger(){
        return logger;
    }
}
