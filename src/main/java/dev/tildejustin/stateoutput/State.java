package dev.tildejustin.stateoutput;

public enum State {
    GENERATING("generating", true),
    PAUSED("inworld,paused"),
    INGAME("inworld,unpaused"),
    OPEN_SCREEN("inworld,gamescreenopen"),
    TITLE("title"),
    WAITING("waiting"),
    @SuppressWarnings("unused")
    PREVIEW("previewing", true),
    UNKNOWN("");

    private static int progress = 0;
    private static boolean progressChanged;
    private final boolean usesProgress;
    private final String string;

    State(String string, boolean usesProgress) {
        this.string = string;
        this.usesProgress = usesProgress;
    }

    State(String string) {
        this(string, false);
    }

    @SuppressWarnings("unused")
    public static int getProgress() {
        return State.progress;
    }

    public State withProgress(int progress) {
        assert this.usesProgress;

        if (progress != State.progress) {
            State.progressChanged = true;
        }
        State.progress = progress;
        return this;
    }

    public static boolean hasProgressChanged() {
        return State.progressChanged;
    }

    public void markLatest() {
        State.progressChanged = false;
    }

    @Override
    public String toString() {
        if (usesProgress) {
            return String.format("%s,%d", this.string, State.progress);
        } else {
            return this.string;
        }
    }
}
