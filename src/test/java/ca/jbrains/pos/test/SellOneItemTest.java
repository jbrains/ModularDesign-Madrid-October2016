package ca.jbrains.pos.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SellOneItemTest {
    @Test
    public void productFound() throws Exception {
        final Display display = new Display();
        final Sale sale = new Sale(new HashMap<String, String>() {{
            put("12345", "EUR 7,95");
            put("23456", "EUR 12,50");
        }}, display);

        sale.onBarcode("12345");

        Assert.assertEquals("EUR 7,95", display.getText());
    }

    @Test
    public void anotherProductFound() throws Exception {
        final Display display = new Display();
        final Sale sale = new Sale(new HashMap<String, String>() {{
            put("12345", "EUR 7,95");
            put("23456", "EUR 12,50");
        }}, display);

        sale.onBarcode("23456");

        Assert.assertEquals("EUR 12,50", display.getText());
    }

    @Test
    public void productNotFound() throws Exception {
        final Display display = new Display();
        final Sale sale = new Sale(Collections.emptyMap(), display);

        sale.onBarcode("99999");

        Assert.assertEquals("Product not found for 99999", display.getText());
    }

    @Test
    public void emptyBarcode() throws Exception {
        final Display display = new Display();
        final Sale sale = new Sale(null, display);

        sale.onBarcode("");

        Assert.assertEquals("Scanning error: empty barcode", display.getText());
    }

    private static class Sale {
        private final Map<String, String> pricesByBarcode;
        private final Display display;

        private Sale(Map<String, String> pricesByBarcode, Display display) {
            this.pricesByBarcode = pricesByBarcode;
            this.display = display;
        }

        public void onBarcode(String barcode) {
            if ("".equals(barcode)) {
                display.setText("Scanning error: empty barcode");
                return;
            }

            final String priceAsText = pricesByBarcode.get(barcode);
            if (priceAsText == null)
                display.setText(
                        String.format("Product not found for %s", barcode));
            else
                display.setText(priceAsText);
        }
    }

    private static class Display {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
