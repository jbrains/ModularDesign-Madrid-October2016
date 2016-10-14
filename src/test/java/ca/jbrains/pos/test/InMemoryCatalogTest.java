package ca.jbrains.pos.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InMemoryCatalogTest {
    @Test
    public void productFound() throws Exception {
        final String barcode = "::barcode::";
        final Price price = Price.cents(700);

        final InMemoryCatalog catalog = createCatalogWith(barcode, price);
        Assert.assertEquals(price, catalog.findPrice(barcode));
    }

    @Test
    public void productNotFound() throws Exception {
        final InMemoryCatalog catalog = createCatalogWithout("::unknown::");
        Assert.assertEquals(null, catalog.findPrice("::unknown::"));
    }

    private InMemoryCatalog createCatalogWith(String barcode, Price price) {
        return new InMemoryCatalog(Collections.<String, Price>singletonMap(barcode, price));
    }

    private InMemoryCatalog createCatalogWithout(String barcodeToAvoid) {
        return new InMemoryCatalog(new HashMap<String, Price>() {{
            put("Not " + barcodeToAvoid, Price.cents(1));
            put("Definitely not " + barcodeToAvoid, Price.cents(2));
            put("Not " + barcodeToAvoid + " even a little bit", Price.cents(3));
        }});
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
