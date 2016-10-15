package ca.jbrains.pos.test;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

public class ConsumeTextCommandsWithBufferedReader {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private TextCommandListener textCommandListener;

    @Test
    public void onlyEmptyLines() throws Exception {
        context.checking(new Expectations() {{
            exactly(3).of(textCommandListener).onCommand("");
        }});

        new ConsumeTextInputAsLines(textCommandListener)
                .consume(new StringReader("\n\n\n"));
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
