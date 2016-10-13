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

    @Test
    public void differentDenominator() throws Exception {
        Fraction sum = add(new Fraction(3, 4), new Fraction(1, 7));

        Assert.assertEquals(25, sum.getNumerator());
        Assert.assertEquals(28, sum.getDenominator());
    }

    private Fraction add(Fraction addend, Fraction augend) {
        if (addend.getDenominator() == augend.getDenominator())
            return new Fraction(
                    addend.getNumerator() + augend.getNumerator(),
                    addend.getDenominator());
        else
            return new Fraction(
                    addend.getNumerator() * augend.getDenominator()
                            + augend.getNumerator() * addend.getDenominator(),
                    addend.getDenominator() * augend.getDenominator());
    }

    private class Fraction {
        private final int numerator;
        private final int denominator;
        private int integerValue;

        public Fraction(int integerValue) {
            this(integerValue, 1);
        }

        public Fraction(int numerator, int denominator) {
            this.integerValue = numerator;
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public int intValue() {
            return integerValue;
        }

        public int getNumerator() {
            return numerator;
        }

        public int getDenominator() {
            return denominator;
        }
    }
}
