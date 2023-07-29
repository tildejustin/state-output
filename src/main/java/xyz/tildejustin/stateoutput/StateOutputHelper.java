package xyz.tildejustin.stateoutput;

import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A helper class for outputting and logging the current state of resetting to help with macros and verification without interrupting the regular flow of the game.
 *
 * @author DuncanRuns
 * @author jojoe77777
 */
public final class StateOutputHelper {
    private static final Path OUT_PATH = Paths.get("wpstateout.txt");
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    public static int loadingProgress = 0;
    public static boolean titleHasEverLoaded = false;
    private static State lastOutput = State.UNKNOWN;

    private static RandomAccessFile stateFile;

    public static void outputState(State state) {
        // Prevent "generating,0" from appearing on game start
        if (!titleHasEverLoaded) {
            if (state.equals(State.TITLE)) titleHasEverLoaded = true;
            else return;
        }

        // Check for changes
        if (!state.progress && lastOutput.equals(state)) {
            return;
        }
        lastOutput = state;

        // Queue up the file writes as to not interrupt mc itself
        EXECUTOR.execute(() -> outputStateInternal(state.toString()));
    }

    public static void outputLastState() {
        EXECUTOR.execute(() -> outputStateInternal(lastOutput.toString()));
    }

    private static void outputStateInternal(String string) {
        try {
            if (stateFile == null) { // opening file only once is better for performance
                stateFile = new RandomAccessFile(OUT_PATH.toString(), "rw");
            }
            stateFile.setLength(0); // clear existing file contents
            stateFile.seek(0); // move pointer back to start of file
            stateFile.write(string.getBytes(StandardCharsets.UTF_8));
            String message = "StateOutput State: " + string;
            if (StateOutput.legacyLog) StateOutput.log(message);
            else LogHelper.log(Level.INFO, message);
        } catch (IOException ignored) {
            String error = "Failed to write state output!";
            if (StateOutput.legacyLog) StateOutput.log(error);
            else LogHelper.log(Level.ERROR, error);
        }
    }
}