package ca.jbrains.math.test;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class AddFractionsTest {
    @Test
    public void zeroPlusZero() throws Exception {
        Fraction sum = add(new Fraction(0), new Fraction(0));
        Assert.assertEquals(0, sum.intValue());
    }

    @Test
    public void nonZeroPlusZero() throws Exception {
        Fraction sum = add(new Fraction(3), new Fraction(0));
        Assert.assertEquals(3, sum.intValue());
    }

    private Fraction add(Fraction addend, Fraction augend) {
        return addend;
    }

    private class Fraction {
        private final int integerValue;

        public Fraction(int integerValue) {
            this.integerValue = integerValue;
        }

        public int intValue() {
            return integerValue;
        }
    }
}
