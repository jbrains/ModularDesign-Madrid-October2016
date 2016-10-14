package ca.jbrains.pos.test;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

public class ConsumeInputAndInterpretTextCommandsTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private BarcodeScannedListener barcodeScannedListener;

    @Test
    public void oneBarcode() throws Exception {
        context.checking(new Expectations() {{
            oneOf(barcodeScannedListener).onBarcode(with("::barcode::"));
        }});

        new TextInputConsumerAndCommandInterpreter(barcodeScannedListener)
                .consume(new StringReader("::barcode::\n"));
    }

    public interface BarcodeScannedListener {
        void onBarcode(String barcode);
    }

    private static class TextInputConsumerAndCommandInterpreter {
        private final BarcodeScannedListener barcodeScannedListener;

        public TextInputConsumerAndCommandInterpreter(BarcodeScannedListener barcodeScannedListener) {
            this.barcodeScannedListener = barcodeScannedListener;
        }

        public void consume(Reader commandSource) {
            barcodeScannedListener.onBarcode(new Scanner(commandSource).nextLine());
        }
    }
}
