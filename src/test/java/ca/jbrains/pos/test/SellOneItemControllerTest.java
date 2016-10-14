package ca.jbrains.pos.test;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class SellOneItemControllerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void productFound() throws Exception {
        final Catalog catalog = context.mock(Catalog.class);
        final Display display = context.mock(Display.class);
        final Price price = Price.cents(1250);

        final SellOneItemController controller
                = new SellOneItemController(catalog, display);

        context.checking(new Expectations() {{
            allowing(catalog).findPrice(with("12345"));
            will(returnValue(price));

            oneOf(display).displayPrice(with(price));
        }});

        controller.onBarcode("12345");
    }

    @Test
    public void productNotFound() throws Exception {
        final Catalog catalog = context.mock(Catalog.class);
        final Display display = context.mock(Display.class);

        final SellOneItemController controller
                = new SellOneItemController(catalog, display);

        context.checking(new Expectations() {{
            allowing(catalog).findPrice(with("99999"));
            will(returnValue(null));

            oneOf(display).displayProductNotFoundMessage(with("99999"));
        }});

        controller.onBarcode("99999");
    }

    @Test
    public void emptyBarcode() throws Exception {
        final Display display = context.mock(Display.class);

        final SellOneItemController controller
                = new SellOneItemController(null, display);

        context.checking(new Expectations() {{
            oneOf(display).displayScannedEmptyBarcodeMessage();
        }});
        
        controller.onBarcode("");
    }

    public interface Catalog {
        Price findPrice(String barcode);
    }

    public interface Display {
        void displayPrice(Price price);

        void displayProductNotFoundMessage(String barcodeNotFound);

        void displayScannedEmptyBarcodeMessage();
    }

    public static class Price {
        public static Price cents(int centsValue) {
            return new Price();
        }

        @Override
        public String toString() {
            return "a Price";
        }
    }

    private class SellOneItemController {
        private final Catalog catalog;
        private final Display display;

        public SellOneItemController(Catalog catalog, Display display) {
            this.catalog = catalog;
            this.display = display;
        }

        public void onBarcode(String barcode) {
            if ("".equals(barcode)) {
                display.displayScannedEmptyBarcodeMessage();
                return;
            }
            
            final Price price = catalog.findPrice(barcode);
            if (price == null)
                display.displayProductNotFoundMessage(barcode);
            else
                display.displayPrice(price);
        }
    }
}
