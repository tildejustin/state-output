package dev.tildejustin.stateoutput;

import org.apache.logging.log4j.Level;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.function.UnaryOperator;

/**
 * A helper class for outputting and logging the current state of resetting to help with macros and verification without interrupting the regular flow of the game.
 *
 * @author DuncanRuns
 * @author jojoe77777
 */
public final class StateOutputHelper {
    private static final Path OUT_PATH = Paths.get("wpstateout.txt");
    private static final RandomAccessFile stateFile;
    public static boolean titleHasEverLoaded = false;
    private static State lastOutput = State.UNKNOWN;

    static {
        try {
            stateFile = new RandomAccessFile(OUT_PATH.toString(), "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void outputState(State state) {
        // Prevent "generating,0" from appearing on game start
        if (!titleHasEverLoaded) {
            if (!state.equals(State.TITLE)) {
                return;
            }
            titleHasEverLoaded = true;
        }

        // Check for changes
        if (lastOutput == state && !State.hasProgressChanged()) {
            return;
        }
        state.markLatest();
        lastOutput = state;

        // Queue up the file writes as to not interrupt mc itself
        outputStateInternal(state.toString());
    }

    public static void updateLastState(UnaryOperator<State> stateUpdater) {
        outputState(stateUpdater.apply(lastOutput));
    }

    private static void outputStateInternal(String string) {
        try {
            stateFile.seek(0); // move pointer back to start of file
            stateFile.write(string.getBytes(StandardCharsets.UTF_8));
            stateFile.setLength(string.length()); // clear old file contents
            String message = "StateOutput State: " + string;
            if (StateOutput.legacyLog) {
                StateOutput.log(message);
            } else {
                LogHelper.log(Level.INFO, message);
            }
        } catch (IOException ignored) {
            String error = "Failed to write state output!";
            if (StateOutput.legacyLog) {
                StateOutput.log(error);
            } else {
                LogHelper.log(Level.ERROR, error);
            }
        }
    }
}
