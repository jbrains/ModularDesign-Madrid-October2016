package ca.jbrains.math.test;

import org.junit.Assert;
import org.junit.Test;

public class AddFractionsTest {
    @Test
    public void zeroPlusZero() throws Exception {
        Assert.assertEquals(new Fraction(0), add(new Fraction(0), new Fraction(0)));
    }

    @Test
    public void nonZeroPlusZero() throws Exception {
        Assert.assertEquals(new Fraction(3), add(new Fraction(3), new Fraction(0)));
    }

    @Test
    public void zeroPlusNonZero() throws Exception {
        Assert.assertEquals(new Fraction(7), add(new Fraction(0), new Fraction(7)));
    }

    @Test
    public void nonZeroPlusNonZero() throws Exception {
        Assert.assertEquals(new Fraction(9), add(new Fraction(4), new Fraction(5)));
    }

    @Test
    public void nonIntegersWithSameDenominator() throws Exception {
        Assert.assertEquals(
                new Fraction(3, 5),
                add(new Fraction(1, 5), new Fraction(2, 5)));
    }

    @Test
    public void differentDenominator() throws Exception {
        Assert.assertEquals(
                new Fraction(25, 28),
                add(new Fraction(3, 4), new Fraction(1, 7)));
    }

    private Fraction add(Fraction addend, Fraction augend) {
        return new Fraction(
                addend.getNumerator() * augend.getDenominator()
                        + augend.getNumerator() * addend.getDenominator(),
                addend.getDenominator() * augend.getDenominator());
    }

    private class Fraction {
        private final int numerator;
        private final int denominator;

        public Fraction(int integerValue) {
            this(integerValue, 1);
        }

        public Fraction(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public int getNumerator() {
            return numerator;
        }

        public int getDenominator() {
            return denominator;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Fraction) {
                Fraction that = (Fraction) other;
                return
                        this.getNumerator() * that.getDenominator()
                                == that.getNumerator() * this.getDenominator();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return String.format("%d/%d", numerator, denominator);
        }
    }
}
