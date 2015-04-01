package akka.gladiators

import akka.actor.ActorSystem
import akka.testkit.JavaTestKit
import akka.testkit.TestActorRef
import spock.lang.Specification

class GladiatorActorTests extends Specification {

    ActorSystem system
    JavaTestKit parent
    JavaTestKit probe
    TestActorRef<GladiatorActor> subject

    def setup() {
        system = ActorSystem.create("Gladiator")
        parent = new JavaTestKit(system)
        probe = new JavaTestKit(system)
        subject = new TestActorRef(system, GladiatorActor.create(), parent.ref, "GladiatorActor")
    }

    def "has 10 hit points by default"() {
        when:
        subject.tell(new GetGladiatorState(), probe.ref)
        Object[] messages = probe.receiveN(1)

        then:
        10 == messages[0].hitpoints
    }

    def "has 10 armor class by default"() {
        when:
        subject.tell(new GetGladiatorState(), probe.ref)
        Object[] messages = probe.receiveN(1)

        then:
        10 == messages[0].armorClass
    }

    def "removes 1 hit point when attack roll is greater than armor class"() {
        when:
        subject.tell(new Attack(roll: 11), probe.ref)
        Object[] messages = parent.receiveN(1)

        then:
        9 == messages[0].hitpoints
    }

    def "removes 1 hit point when attack roll is even with armor class"() {
        when:
        subject.tell(new Attack(roll: 11), probe.ref)
        Object[] messages = parent.receiveN(1)

        then:
        9 == messages[0].hitpoints
    }

    def "does not remove any hitpoints when attack roll is less than armor class"() {
        when:
        subject.tell(new Attack(roll: 9), probe.ref)
        Object[] messages = parent.receiveN(1)

        then:
        10 == messages[0].hitpoints
    }

}
