package ca.jbrains.math.test;

import ca.jbrains.math.Fraction;
import org.junit.Assert;
import org.junit.Test;

public class AddFractionsTest {
    @Test
    public void zeroPlusZero() throws Exception {
        Assert.assertEquals(new Fraction(0), new Fraction(0).plus(new Fraction(0)));
    }

    @Test
    public void nonZeroPlusZero() throws Exception {
        Assert.assertEquals(new Fraction(3), new Fraction(3).plus(new Fraction(0)));
    }

    @Test
    public void zeroPlusNonZero() throws Exception {
        Assert.assertEquals(new Fraction(7), new Fraction(0).plus(new Fraction(7)));
    }

    @Test
    public void nonZeroPlusNonZero() throws Exception {
        Assert.assertEquals(new Fraction(9), new Fraction(4).plus(new Fraction(5)));
    }

    @Test
    public void nonIntegersWithSameDenominator() throws Exception {
        Assert.assertEquals(
                new Fraction(3, 5),
                new Fraction(1, 5).plus(new Fraction(2, 5)));
    }

    @Test
    public void differentDenominator() throws Exception {
        Assert.assertEquals(
                new Fraction(25, 28),
                new Fraction(3, 4).plus(new Fraction(1, 7)));
    }

}
