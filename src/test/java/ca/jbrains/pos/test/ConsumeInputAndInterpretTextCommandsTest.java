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

    @Test
    public void noBarcodes() throws Exception {
        context.checking(new Expectations() {{
            never(barcodeScannedListener);
        }});

        new TextInputConsumerAndCommandInterpreter(barcodeScannedListener)
                .consume(new StringReader(""));
    }

    @Test
    public void severalBarcodes() throws Exception {
        context.checking(new Expectations() {{
            oneOf(barcodeScannedListener).onBarcode(with("::barcode 1::"));
            oneOf(barcodeScannedListener).onBarcode(with("::barcode 2::"));
            oneOf(barcodeScannedListener).onBarcode(with("::barcode 3::"));
        }});

        new TextInputConsumerAndCommandInterpreter(barcodeScannedListener)
                .consume(new StringReader("::barcode 1::\n::barcode 2::\n::barcode 3::\n"));
    }

    @Test
    public void emptyCommand() throws Exception {
        context.checking(new Expectations() {{
            oneOf(barcodeScannedListener).onBarcode(with("::barcode 1::"));
            oneOf(barcodeScannedListener).onBarcode(with("::barcode 2::"));
            oneOf(barcodeScannedListener).onBarcode(with("::barcode 3::"));
        }});

        new TextInputConsumerAndCommandInterpreter(barcodeScannedListener)
                .consume(new StringReader("::barcode 1::\n" +
                        " \t \n" +
                        "\r" +
                        " \n" +
                        "::barcode 2::\n" +
                        "\n" +
                        "::barcode 3::\n"));
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
            final Scanner commandScanner = new Scanner(commandSource);
            while (commandScanner.hasNext())
                barcodeScannedListener.onBarcode(commandScanner.nextLine());
        }
    }
}
