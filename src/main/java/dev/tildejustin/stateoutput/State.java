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

    private final boolean usesProgress;
    private int progress = 0;
    private boolean progressChanged;
    private final String string;

    State(String string, boolean usesProgress) {
        this.string = string;
        this.usesProgress = usesProgress;
    }

    State(String string) {
        this(string, false);
    }

    public State withProgress(int progress) {
        assert this.usesProgress;

        if (progress != this.progress) {
            progressChanged = true;
        }
        this.progress = progress;
        return this;
    }

    public boolean hasProgressChanged() {
        return progressChanged;
    }

    public void markLatest() {
        this.progressChanged = false;
    }

    @Override
    public String toString() {
        if (usesProgress) {
            return String.format("%s,%d", this.string, this.progress);
        } else {
            return this.string;
        }
    }
}
