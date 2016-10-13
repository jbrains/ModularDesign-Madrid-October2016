package ca.jbrains.math.test

import ca.jbrains.math.Fraction
import spock.lang.Specification


class ParseFractionsTest extends Specification {
    def "special cases"() {
        expect:
        fraction == Fraction.parse(text)

        where:
        text || fraction
        "0" || new Fraction(0)
        "5" || new Fraction(5)
        "-4" || new Fraction(-4)
    }

}