package akka.bowling

import akka.actor.ActorSystem
import akka.bowling.BowlingFrameActor
import akka.testkit.JavaTestKit
import akka.testkit.TestActorRef
import spock.lang.Specification
import spock.lang.Unroll

class BowlingFrameActorsTests extends Specification {

    def system
    def subject
    JavaTestKit probe


    def setup() {
        system = ActorSystem.create("Bowling")
        subject = TestActorRef.create(system, BowlingFrameActor.create())
        probe = new JavaTestKit(system)
    }

    @Unroll
    def "rolls of #firstRoll and #secondRoll should be #result"() {
        when:
        subject.tell([firstRoll, secondRoll], probe.ref)

        then:
        probe.expectMsgEquals(result)

        where:
        firstRoll   | secondRoll    | result
        "1"         | "1"           | 2
        "2"         | "1"           | 3
        "-"         | "5"           | 5
        "X"         | "-"           | 10
        "7"         | "/"           | 10
    }

    @Unroll
    def "rolls of #first, #second and #third should be #result"() {
        when:
        subject.tell([first, second, third], probe.ref)

        then:
        probe.expectMsgEquals(result)

        where:
        first   | second    | third | result
        "1"     | "2"       | "3"   | 6
        "X"     | "X"       | "X"   | 30
        "X"     | "3"       | "/"   | 20
    }


}
