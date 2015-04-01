package akka

import akka.actor.ActorSystem
import akka.pattern.Patterns
import akka.testkit.JavaTestKit
import akka.testkit.TestActorRef
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import spock.lang.Specification

import java.util.concurrent.TimeUnit

class EchoActorTests extends Specification {

    def system = ActorSystem.create("TestSystem")

    def "using ask pattern"() {
        given:
        def subject = TestActorRef.create(system, EchoActor.create(), "echo")

        when:
        def future = Patterns.ask(subject, "Jerks", 5000)

        then:
        "Jerks" == Await.result(future, Duration.create(1, TimeUnit.SECONDS))
    }

    def "using TestProbe -- much better"() {
        given:
        def subject = TestActorRef.create(system, EchoActor.create(), "echo")
        JavaTestKit probe = new JavaTestKit(system)

        when:
        subject.tell("Aloha", probe.ref)

        then:
        probe.expectMsgEquals("Aloha")
    }
}
