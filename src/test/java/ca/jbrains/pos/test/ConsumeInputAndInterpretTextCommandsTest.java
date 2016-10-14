package ca.jbrains.pos.test;

import ca.jbrains.pos.BarcodeScannedListener;
import ca.jbrains.pos.TextInputConsumerAndCommandInterpreter;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.io.StringReader;

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

}
