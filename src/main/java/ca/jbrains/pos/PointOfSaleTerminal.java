package ca.jbrains.pos;

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
                        System.out.printf("EUR %.2f", price.euro());
                    }

                    @Override
                    public void displayProductNotFoundMessage(String barcodeNotFound) {
                        System.out.printf("Product not found for %s", barcodeNotFound);
                    }

                    @Override
                    public void displayScannedEmptyBarcodeMessage() {
                        System.out.printf("Scanning error: empty barcode");
                    }
                }
        );
        
        sellOneItemController.onBarcode("8410055050011");
    }
}
