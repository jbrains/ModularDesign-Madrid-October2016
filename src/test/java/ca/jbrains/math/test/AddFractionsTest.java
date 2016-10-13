package ca.jbrains.math.test;

import org.junit.Assert;
import org.junit.Test;

public class AddFractionsTest {
    @Test
    public void zeroPlusZero() throws Exception {
        Fraction sum = add(new Fraction(0), new Fraction(0));
        Assert.assertEquals(0, sum.intValue());
    }

    private Fraction add(Fraction addend, Fraction augend) {
        return new Fraction(-23746);
    }

    private class Fraction {
        public Fraction(int integerValue) {
        }

        public int intValue() {
            return 0;
        }
    }
}
