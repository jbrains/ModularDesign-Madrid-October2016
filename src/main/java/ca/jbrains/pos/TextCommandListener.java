package ca.jbrains.pos;

public interface TextCommandListener {
    void onCommand(String commandText);

    void onEmptyCommand();
}
