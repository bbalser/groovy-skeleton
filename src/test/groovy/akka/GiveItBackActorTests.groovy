package akka

import akka.actor.ActorSystem
import akka.pattern.Patterns
import akka.testkit.JavaTestKit
import akka.testkit.TestActorRef
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import spock.lang.Specification

class GiveItBackActorTests extends Specification {

    def system = ActorSystem.create("TestSystem")

    def "using ask pattern"() {
        given:
        def subject = TestActorRef.create(system, GiveItBackActor.create(), "GiveIt")

        when:
        def future = Patterns.ask(subject, "Jerks", 5000)

        then:
        "Jerks" == Await.result(future, Duration.Zero())
    }

    def "using TestProbe -- much better"() {
        given:
        def subject = TestActorRef.create(system, GiveItBackActor.create(), "GiveIt")
        JavaTestKit probe = new JavaTestKit(system)

        when:
        subject.tell("Aloha", probe.ref)

        then:
        probe.expectMsgEquals("Aloha")
    }
}
