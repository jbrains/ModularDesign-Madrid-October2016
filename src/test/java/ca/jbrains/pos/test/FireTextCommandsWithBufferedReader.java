package ca.jbrains.pos.test;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FireTextCommandsWithBufferedReader {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private TextCommandListener textCommandListener;

    @Auto
    private Sequence commandsSequence;
    private FireTextCommands fireTextCommands;

    // REFACTOR Move to a reusable library for test data.
    public static List<String> nLinesLike(int n, String formatDescription) {
        return IntStream.range(1, n).mapToObj(
                (each) -> String.format(formatDescription, each)
        ).collect(Collectors.toList());
    }

    // REFACTOR Move to generic text library
    public static String unlines(List<String> lines) {
        return String.join(System.lineSeparator(), lines);
    }

    @Before
    public void setUp() throws Exception {
        fireTextCommands = new FireTextCommands(textCommandListener);
    }

    @Test
    public void onlyValidCommands() throws Exception {
        // Commands are "valid" if they have neither leading
        // nor trailing whitespace. By implication, this means
        // that they are not empty, either.
        context.checking(new Expectations() {{
            nLinesLike(5, "::valid command %d::").stream().forEach(
                    (eachCommand) -> {
                        oneOf(textCommandListener).onCommand(eachCommand);
                        inSequence(commandsSequence);
                    }
            );
        }});

        fireTextCommands.consumeText(new StringReader(
                unlines(nLinesLike(5, "::valid command %d::"))
        ));
    }

    @Test
    public void ignoreEmptyCommands() throws Exception {
        context.checking(new Expectations() {{
            never(textCommandListener);
        }});

        fireTextCommands.consumeText(new StringReader(
                unlines(Arrays.asList(
                        "",
                        "\t",
                        "\r",
                        "\f",
                        "\u000B",
                        " ",
                        " \t  \f  \u000B   "
                ))
        ));
    }

    @Test
    public void trimWhitespaceFromCommands() throws Exception {
        context.checking(new Expectations() {{
            oneOf(textCommandListener).onCommand("::command\twith\finterior\u000Bspaces::");
        }});

        fireTextCommands.consumeText(new StringReader(
                "  \t  \f  \u000B  \t ::command\twith\finterior\u000Bspaces::\t\t \f  \t   \u000B  \f \t  "
        ));

    }

    public interface TextCommandListener {
        void onCommand(String commandText);
    }

    public static class FireTextCommands {
        private final TextCommandListener textCommandListener;

        public FireTextCommands(TextCommandListener textCommandListener) {
            this.textCommandListener = textCommandListener;
        }

        public void consumeText(Reader textSource) {
            // Sigh. filter() and predicates and not().
            // Read http://stackoverflow.com/a/30506585/253921
            sanitizeLines(new BufferedReader(textSource)
                    .lines())
                    .forEach(textCommandListener::onCommand);
        }

        // REFACTOR Make this a collaborator of the text consumer?
        private Stream<String> sanitizeLines(Stream<String> lines) {
            return lines
                    .map(String::trim)
                    .filter((line) -> !line.isEmpty());
        }
    }
}
