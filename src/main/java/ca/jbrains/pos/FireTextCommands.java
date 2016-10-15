package ca.jbrains.pos;

import java.io.BufferedReader;
import java.io.Reader;

public class FireTextCommands {
    private final TextCommandListener textCommandListener;

    public FireTextCommands(TextCommandListener textCommandListener) {
        this.textCommandListener = textCommandListener;
    }

    public void consumeText(Reader textSource) {
        new BufferedReader(textSource)
                .lines().map(String::trim)
                .forEachOrdered(textCommandListener::onCommand);
    }
}
