package akka.gladiators

import akka.actor.ActorSystem
import akka.testkit.JavaTestKit
import akka.testkit.TestActorRef
import spock.lang.Specification
import akka.actor.ActorRef

class MapActorTests extends Specification {

    ActorSystem system
    JavaTestKit probe
    TestActorRef subject
    ActorRef gladiatorActor

    def setup() {
        system = ActorSystem.create("Map")
        probe = new JavaTestKit(system)
        subject = TestActorRef.create(system, MapActor.create())
        gladiatorActor = system.actorOf(GladiatorActor.create())
    }

    def "it should record position of gladiator actor when set"() {
        when:
        subject.tell(new SetPosition(x: 3, y: 3, gladiator: gladiatorActor), probe.ref)
        def messages = probe.receiveN(1)

        then:
        messages[0].pos(3,3) is gladiatorActor
    }

    def "it should move actor right when given right"() {
        when:
        subject.tell(new SetPosition(x: 5, y: 5, gladiator: gladiatorActor), probe.ref)
        subject.tell(new MoveGladiator(gladiator: gladiatorActor, direction: MapActor.Direction.RIGHT), probe.ref)
        def messages = probe.receiveN(2)

        then:
        messages.size() == 2
        messages[0].pos(5,5) is gladiatorActor
        messages[1].pos(6,5) is gladiatorActor
    }

    def "it should move actor left when given left"() {
        when:
        subject.tell(new SetPosition(x: 5, y: 5, gladiator: gladiatorActor), probe.ref)
        subject.tell(new MoveGladiator(gladiator: gladiatorActor, direction: MapActor.Direction.LEFT), probe.ref)
        def messages = probe.receiveN(2)

        then:
        messages.size() == 2
        messages[0].pos(5,5) is gladiatorActor
        messages[1].pos(4,5) is gladiatorActor
    }

    def "it should move actor up when given up"() {
        when:
        subject.tell(new SetPosition(x: 5, y: 5, gladiator: gladiatorActor), probe.ref)
        subject.tell(new MoveGladiator(gladiator: gladiatorActor, direction: MapActor.Direction.UP), probe.ref)
        def messages = probe.receiveN(2)

        then:
        messages.size() == 2
        messages[0].pos(5,5) is gladiatorActor
        messages[1].pos(5,4) is gladiatorActor
    }

    def "it should move actor down when given down"() {
        when:
        subject.tell(new SetPosition(x: 5, y: 5, gladiator: gladiatorActor), probe.ref)
        subject.tell(new MoveGladiator(gladiator: gladiatorActor, direction: MapActor.Direction.DOWN), probe.ref)
        def messages = probe.receiveN(2)

        then:
        messages.size() == 2
        messages[0].pos(5,5) is gladiatorActor
        messages[1].pos(5,6) is gladiatorActor
    }

    def "it should return gladiator at given coordinate"() {
        when:
        subject.tell(new SetPosition(x:4, y:9, gladiator: gladiatorActor), probe.ref)
        subject.tell(new GetCoordinate(x: 4, y: 9), probe.ref)
        def messages = probe.receiveN(2)

        then:
        messages[1].gladiator == gladiatorActor
    }

}
