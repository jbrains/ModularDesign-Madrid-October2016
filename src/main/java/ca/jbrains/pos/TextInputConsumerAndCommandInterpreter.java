package ca.jbrains.pos;

import java.io.Reader;
import java.util.Scanner;

public class TextInputConsumerAndCommandInterpreter {
    private final BarcodeScannedListener barcodeScannedListener;

    public TextInputConsumerAndCommandInterpreter(BarcodeScannedListener barcodeScannedListener) {
        this.barcodeScannedListener = barcodeScannedListener;
    }

    public void consume(Reader commandSource) {
        final Scanner scanner = new Scanner(commandSource);
        while (scanner.hasNext())
            barcodeScannedListener.onBarcode(scanner.nextLine());
    }
}
