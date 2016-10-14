package ca.jbrains.pos.test;

import ca.jbrains.pos.Catalog;
import ca.jbrains.pos.Price;
import org.junit.Assert;
import org.junit.Test;

public abstract class CatalogContract {
    @Test
    public void productFound() throws Exception {
        final String barcode = "::barcode::";
        final Price price = Price.cents(700);

        final Catalog catalog = createCatalogWith(barcode, price);
        Assert.assertEquals(price, catalog.findPrice(barcode));
    }

    @Test
    public void productNotFound() throws Exception {
        final Catalog catalog = createCatalogWithout("::unknown::");
        Assert.assertEquals(null, catalog.findPrice("::unknown::"));
    }

    protected abstract Catalog createCatalogWith(String barcode, Price price);

    protected abstract Catalog createCatalogWithout(String barcodeToAvoid);
}
