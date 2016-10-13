package ca.jbrains.math.test

import ca.jbrains.math.Fraction
import spock.lang.Ignore
import spock.lang.Specification


class AddFractionsTest extends Specification {
    @Ignore("Parsing doesn't work yet")
    def "special cases"() {
        expect:
        sum == addend.plus(augend)

        where:
        addendAsText | augendAsText || sumAsText
        "0" | "0" || "1"

        addend = Fraction.parse(addendAsText)
        augend = Fraction.parse(augendAsText)
        sum = Fraction.parse(sumAsText)

    }
}