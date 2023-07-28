package xyz.tildejustin.stateoutput;

public enum State {
    GENERATING("generating", true),
    PAUSED("inworld", "paused"),
    INGAME("inworld", "unpaused"),
    OPEN_SCREEN("inworld", "gamescreenopen"),
    TITLE("title", false),
    WAITING("waiting", false),
    @SuppressWarnings("unused")
    PREVIEW("previewing", true),
    UNKNOWN("", false);

    public final boolean progress;
    public final String string;
    public final String substring;

    State(String name, boolean progress) {
        this.string = name;
        this.progress = progress;
        this.substring = "";
    }

    State(String name, String substring) {
        this.string = name;
        this.substring = substring;
        this.progress = false;
    }

    @Override
    public String toString() {
        if (progress) return String.format("%s,%d", this.string, StateOutputHelper.loadingProgress);
        if (!substring.isEmpty()) return String.format("%s,%s", this.string, this.substring);
        else return this.string;
    }
}
