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

        // CONTRACT Assume that command text is trimmed, but possibly empty.
        final TextCommandListener commandInterpreter = (commandText) -> {
            if (commandText.isEmpty())
                consoleDisplay.displayScannedEmptyBarcodeMessage();
            else
                // So far, there's only one command!
                // CONTRACT The command is never empty
                sellOneItemController.onBarcode(commandText);
        };

        new FireTextCommands(commandInterpreter).consumeText(new InputStreamReader(System.in));
    }
}
