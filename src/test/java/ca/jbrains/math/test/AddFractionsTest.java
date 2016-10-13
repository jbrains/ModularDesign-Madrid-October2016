package ca.jbrains.math.test;

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

        public Fraction plus(Fraction that) {
            return new Fraction(
                    this.getNumerator() * that.getDenominator()
                            + that.getNumerator() * this.getDenominator(),
                    this.getDenominator() * that.getDenominator());
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
