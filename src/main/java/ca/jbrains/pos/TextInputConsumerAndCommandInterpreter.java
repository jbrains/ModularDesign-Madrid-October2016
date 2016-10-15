package ca.jbrains.pos;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Scanner;

public class TextInputConsumerAndCommandInterpreter {
    private final BarcodeScannedListener barcodeScannedListener;
    private final FireTextCommands fireTextCommands;

    public TextInputConsumerAndCommandInterpreter(BarcodeScannedListener barcodeScannedListener) {
        this.barcodeScannedListener = barcodeScannedListener;
        this.fireTextCommands = new FireTextCommands(barcodeScannedListener::onBarcode);
    }

}
