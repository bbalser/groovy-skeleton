package akka.gladiators

import akka.actor.ActorRef
import akka.actor.ActorSystem
import org.codehaus.groovy.runtime.StackTraceUtils

class Battle {

    public static void main(String[] args) {

        def listener = { message ->
            switch (message) {
                case MapActor.MapState:
                    println "  1   2   3   4   5   6   7   8   9   10"
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            def actor = message.pos(i, j)
                            println "| " + (actor != null ? "A" : " ") + " "
                        }
                    }
                    break
                case GladiatorState:
                    println "Hitpoints: " + message.hitpoints
                    break
            }
        }

        ActorSystem system = ActorSystem.create("Battle")
        ActorRef world = system.actorOf(WorldActor.create(listener))
//        ActorRef bob = system.actorOf(GladiatorActor.create())
//        ActorRef mary = system.actorOf(GladiatorActor.create())


//        world.tell(new SetPosition(x: 3, y: 3, gladiator: bob), null)
//        world.tell(new SetPosition(x: 6, y: 6, gladiator: mary), null)

//        System.in.withReader {
//            it.readLine()
//        }

        sleep(3000)

        system.shutdown()
        system.awaitTermination()


    }

}
