package akka.bowling

import akka.actor.ActorSystem
import akka.testkit.JavaTestKit
import akka.testkit.TestActorRef
import spock.lang.Specification
import spock.lang.Unroll

class BowlingGameActorTests extends Specification {

    def system
    def subject
    def probe

    def setup() {
        system = ActorSystem.create("Bowling")
        probe = new JavaTestKit(system)
        subject = TestActorRef.create(system, BowlingGameActor.create(probe.ref))
    }

    @Unroll
    def "game #game should be #result"() {
        when:
        subject.tell(game, probe.ref)

        then:
        probe.expectMsgEquals(result)

        where:
        result  | game
        1       | "-1"
        15      | "3462"
        60      | "XXX"
        68       | "1/X7/9"
    }

}
