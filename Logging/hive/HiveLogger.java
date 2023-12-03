package hive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HiveLogger {
    private static final Logger logger = LogManager.getLogger(HiveLogger.class);

    private HiveLogger(){}

    public static Logger getLogger(){
        return logger;
    }
}
