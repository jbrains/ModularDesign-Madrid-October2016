package ca.jbrains.pos.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class InMemoryCatalogTest {
    @Test
    public void productFound() throws Exception {
        final String barcode = "::barcode::";
        final Price price = Price.cents(700);

        final InMemoryCatalog catalog
                = new InMemoryCatalog(Collections.<String, Price> singletonMap(barcode, price));
        Assert.assertEquals(price, catalog.findPrice(barcode));
    }

    public static class InMemoryCatalog {
        private final Map<String, Price> pricesByBarcode;

        public InMemoryCatalog(Map<String, Price> pricesByBarcode) {
            this.pricesByBarcode = pricesByBarcode;
        }

        public Price findPrice(String barcode) {
            return pricesByBarcode.get(barcode);
        }
    }
}
