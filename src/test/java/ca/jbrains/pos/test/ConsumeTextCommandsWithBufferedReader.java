package ca.jbrains.pos.test;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsumeTextCommandsWithBufferedReader {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private TextCommandListener textCommandListener;

    @Auto
    private Sequence commandsSequence;

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

        final ConsumeTextInputAsLines consumeTextInputAsLines = new ConsumeTextInputAsLines(textCommandListener);

        consumeTextInputAsLines.consume(new StringReader(
                String.join(
                        System.lineSeparator(),
                        nLinesLike(5, "::valid command %d::")
                )
        ));
    }

    // REFACTOR Move to a reusable library for test data.
    public static List<String> nLinesLike(int n, String formatDescription) {
        return IntStream.range(1, n).mapToObj(
                (each) -> String.format(formatDescription, each)
        ).collect(Collectors.toList());
    }

    public interface TextCommandListener {
        void onCommand(String commandText);
    }

    public static class ConsumeTextInputAsLines {
        private final TextCommandListener textCommandListener;

        public ConsumeTextInputAsLines(TextCommandListener textCommandListener) {
            this.textCommandListener = textCommandListener;
        }

        public void consume(StringReader stringReader) {
            new BufferedReader(stringReader)
                    .lines()
                    .forEach(textCommandListener::onCommand);
        }
    }
}
