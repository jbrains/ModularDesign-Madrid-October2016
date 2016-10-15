package ca.jbrains.pos.test;

import org.hamcrest.Factory;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Scanner;

public class ConsumeTextCommandsWithScannerHasNext {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private TextCommandListener textCommandListener;

    @Test
    public void breaksContractWithEmptyLines() throws Exception {
        context.checking(new Expectations() {{
            // According to the contract, there should be 3 commands,
            // each with empty text.
            exactly(0).of(textCommandListener).onCommand("");
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
            final Scanner scanner = new Scanner(stringReader);
            while (scanner.hasNext())
                textCommandListener.onCommand(scanner.nextLine());
        }
    }
}
