package ca.jbrains.math;

import ca.jbrains.math.test.AddFractionsTest;

public class Fraction {
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
