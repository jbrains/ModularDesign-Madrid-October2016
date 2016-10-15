package ca.jbrains.pos;

import java.io.InputStreamReader;
import java.util.HashMap;

public class PointOfSaleTerminal {
    public static void main(String[] args) {
        final SellOneItemController sellOneItemController = new SellOneItemController(
                new InMemoryCatalog(new HashMap<String, Price>() {{
                    put("8410055050011", Price.cents(179));
                }}),
                new Display() {
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
                }
        );

        // So far, there's only one command!
        final TextCommandListener commandInterpreter = sellOneItemController::onBarcode;
        
        new FireTextCommands(commandInterpreter).consumeText(new InputStreamReader(System.in));
    }
}
