package hive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A játék logikájával, modell működésével kapcsolatos eseményeket loggoló osztály.
 */
public class HiveLogger {
    private static final Logger logger = LogManager.getLogger(HiveLogger.class);

    private HiveLogger(){}

    public static Logger getLogger(){
        return logger;
    }
}
