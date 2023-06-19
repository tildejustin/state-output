package xyz.tildejustin.stateoutput;

public class StateOutput {
    public static boolean legacyLog = false;
    static java.util.logging.Logger LEGACY_LOGGER = java.util.logging.Logger.getLogger("stater");

    public static void log(String message) {
        LEGACY_LOGGER.info(message);
    }

}
