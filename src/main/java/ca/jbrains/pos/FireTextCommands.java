package ca.jbrains.pos;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.stream.Stream;

public class FireTextCommands {
    private final TextCommandListener textCommandListener;

    public FireTextCommands(TextCommandListener textCommandListener) {
        this.textCommandListener = textCommandListener;
    }

    public void consumeText(Reader textSource) {
        // Sigh. filter() and predicates and not().
        // Read http://stackoverflow.com/a/30506585/253921
        sanitizeLines(new BufferedReader(textSource)
                .lines())
                .forEachOrdered(textCommandListener::onCommand);
    }

    // REFACTOR Make this a collaborator of the text consumer?
    private Stream<String> sanitizeLines(Stream<String> lines) {
        return lines
                .map(String::trim)
                .filter((line) -> !line.isEmpty());
    }
}
