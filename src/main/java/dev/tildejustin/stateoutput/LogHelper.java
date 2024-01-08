package dev.tildejustin.stateoutput;


import org.apache.logging.log4j.*;

// hack class to not have pre 1.7 try to load log4j classes (which don't exist in those versions)
public class LogHelper {
    static Logger LOGGER = LogManager.getLogger("state-output");

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }
}
