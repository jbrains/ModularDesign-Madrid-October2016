package ca.jbrains.pos;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Scanner;

public class TextInputConsumerAndCommandInterpreter {
    private final BarcodeScannedListener barcodeScannedListener;

    public TextInputConsumerAndCommandInterpreter(BarcodeScannedListener barcodeScannedListener) {
        this.barcodeScannedListener = barcodeScannedListener;
    }

    public void consume(Reader commandSource) {
        new BufferedReader(commandSource).lines()
                .forEach(barcodeScannedListener::onBarcode);
    }
}
