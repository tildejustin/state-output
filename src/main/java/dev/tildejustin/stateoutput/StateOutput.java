package dev.tildejustin.stateoutput;

import java.util.logging.Logger;

public class StateOutput {
    public static boolean legacyLog = false;
    static Logger LEGACY_LOGGER = Logger.getLogger("stater");

    public static void log(String message) {
        LEGACY_LOGGER.info(message);
    }
}
