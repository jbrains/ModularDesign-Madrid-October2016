package ca.jbrains.pos.test;

import ca.jbrains.pos.Catalog;
import ca.jbrains.pos.Display;
import ca.jbrains.pos.Price;
import ca.jbrains.pos.SellOneItemController;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class SellOneItemControllerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void productFound() throws Exception {
        final int irrelevantCentsValue = 1250;

        final Catalog catalog = context.mock(Catalog.class);
        final Display display = context.mock(Display.class);
        final Price price = Price.cents(irrelevantCentsValue);

        final SellOneItemController controller
                = new SellOneItemController(catalog, display);

        context.checking(new Expectations() {{
            allowing(catalog).findPrice(with("::known::"));
            will(returnValue(price));

            oneOf(display).displayPrice(with(price));
        }});

        controller.onBarcode("::known::");
    }

    @Test
    public void productNotFound() throws Exception {
        final Catalog catalog = context.mock(Catalog.class);
        final Display display = context.mock(Display.class);

        final SellOneItemController controller
                = new SellOneItemController(catalog, display);

        context.checking(new Expectations() {{
            allowing(catalog).findPrice(with("::unknown::"));
            will(returnValue(null));

            oneOf(display).displayProductNotFoundMessage(with("::unknown::"));
        }});

        controller.onBarcode("::unknown::");
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

}
