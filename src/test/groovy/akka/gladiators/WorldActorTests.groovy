package akka.gladiators

import akka.actor.ActorSystem
import akka.testkit.JavaTestKit
import akka.testkit.TestActorRef

class WorldActorTests {

    ActorSystem system
    JavaTestKit probe
    TestActorRef<WorldActor> subject

    def setup() {
        system = ActorSystem.create('World')
        probe = new JavaTestKit(system)
        subject = TestActorRef.create(system, WorldActor.create())
    }

//    def "stuff"() {
//        when:
//
//    }


}
