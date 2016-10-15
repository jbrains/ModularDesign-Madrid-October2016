package ca.jbrains.pos;

import java.io.InputStreamReader;
import java.util.HashMap;

public class PointOfSaleTerminal {
    public static void main(String[] args) {
        final Display consoleDisplay = new Display() {
            @Override
            public void displayPrice(Price price) {
                System.out.println(String.format("EUR %.2f", price.euro()));
            }

            @Override
            public void displayProductNotFoundMessage(String barcodeNotFound) {
                System.out.println(String.format("Product not found for %s", barcodeNotFound));
            }

            @Override
            public void displayScannedEmptyBarcodeMessage() {
                System.out.println(String.format("Scanning error: empty barcode"));
            }
        };

        final SellOneItemController sellOneItemController = new SellOneItemController(
                new InMemoryCatalog(new HashMap<String, Price>() {{
                    put("8410055050011", Price.cents(179));
                }}),
                consoleDisplay
        );

        new FireTextCommands(new TextCommandListener() {
            @Override
            public void onCommand(String commandText) {
                // So far, there's only one command!
                sellOneItemController.onBarcode(commandText);
            }

            @Override
            public void onEmptyCommand() {
                consoleDisplay.displayScannedEmptyBarcodeMessage();
            }
        }).consumeText(new InputStreamReader(System.in));
    }
}
