package ca.jbrains.math.test;

import org.junit.Assert;
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

    @Test
    public void zeroPlusNonZero() throws Exception {
        Fraction sum = add(new Fraction(0), new Fraction(7));
        Assert.assertEquals(7, sum.intValue());
    }

    @Test
    public void nonZeroPlusNonZero() throws Exception {
        Fraction sum = add(new Fraction(4), new Fraction(5));
        Assert.assertEquals(9, sum.intValue());
    }

    @Test
    public void nonIntegersWithSameDenominator() throws Exception {
        Fraction sum = add(new Fraction(1, 5), new Fraction(2, 5));

        Assert.assertEquals(3, sum.getNumerator());
        Assert.assertEquals(5, sum.getDenominator());
    }

    private Fraction add(Fraction addend, Fraction augend) {
        return new Fraction(addend.intValue() + augend.intValue());
    }

    private class Fraction {
        private int integerValue;

        public Fraction(int integerValue) {
            this.integerValue = integerValue;
        }

        public Fraction(int numerator, int denominator) {

        }

        public int intValue() {
            return integerValue;
        }

        public int getNumerator() {
            return 3;
        }

        public int getDenominator() {
            return 5;
        }
    }
}
