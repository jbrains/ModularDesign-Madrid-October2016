package ca.jbrains.pos.test;

import ca.jbrains.pos.Catalog;
import ca.jbrains.pos.InMemoryCatalog;
import ca.jbrains.pos.Price;

import java.util.Collections;
import java.util.HashMap;

public class InMemoryCatalogTest extends CatalogContract {

    @Override
    protected Catalog createCatalogWith(String barcode, Price price) {
        return new InMemoryCatalog(Collections.<String, Price>singletonMap(barcode, price));
    }

    @Override
    protected Catalog createCatalogWithout(String barcodeToAvoid) {
        return new InMemoryCatalog(new HashMap<String, Price>() {{
            put("Not " + barcodeToAvoid, Price.cents(1));
            put("Definitely not " + barcodeToAvoid, Price.cents(2));
            put("Not " + barcodeToAvoid + " even a little bit", Price.cents(3));
        }});
    }

}
