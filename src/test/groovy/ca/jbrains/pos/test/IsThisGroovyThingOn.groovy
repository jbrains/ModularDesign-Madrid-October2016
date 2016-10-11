package ca.jbrains.pos.test

import spock.lang.Specification


class IsThisGroovyThingOn extends Specification {
    def "wtf?!"() {
        expect:
        true == false
    }
}