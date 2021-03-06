package akka.gladiators

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Inbox
import scala.concurrent.duration.Duration

import java.util.concurrent.TimeUnit
import java.util.regex.Matcher

class Battle {

    static DIRECTIONS = [right: MapActor.Direction.RIGHT, left: MapActor.Direction.LEFT, up: MapActor.Direction.UP, down: MapActor.Direction.DOWN]

    ActorSystem system
    ActorRef world
    ActorRef bob
    ActorRef mary
    def gladiators = [bob: { -> bob }, mary: { -> mary }]

    def run() {
        system = ActorSystem.create("Battle")
        Inbox inbox = Inbox.create(system)

        world = system.actorOf(WorldActor.create(this.&printoutState))
        inbox.send(world, new CreateGladiator())
        bob = inbox.receive(Duration.create(5, TimeUnit.SECONDS)).gladiator
        inbox.send(world, new CreateGladiator())
        mary = inbox.receive(Duration.create(5, TimeUnit.SECONDS)).gladiator

        world.tell(new SetPosition(x: 3, y: 4, gladiator: bob), ActorRef.noSender())
        world.tell(new SetPosition(x: 6, y: 8, gladiator: mary), ActorRef.noSender())

        commandLoop()

        system.shutdown()
        system.awaitTermination()
    }

    def printoutState(MapActor.MapState message) {
        println "    0   1   2   3   4   5   6   7   8   9"
        for (int i = 0; i < 10; i++) {
            print i + " "
            for (int j = 0; j < 10; j++) {
                def actor = message.pos(j, i)
                def name = gladiators.find {k, v -> v().is(actor)}?.key
                print "| " + (name ? name[0].toUpperCase() : " ") + " "
            }
            print "|\n"
        }
    }

    def printoutState(GladiatorState message) {
        def name = gladiators.find { k, v -> v().is(message.gladiator)}.key
        println "${name} hitpoints: " + message.hitpoints
    }

    def commandLoop() {
        System.in.withReader {
            def keepGoing = true

            while (keepGoing) {
                sleep(500)
                print ":> "
                def command = it.readLine()

                switch (command) {
                    case ~/^quit$/:
                        keepGoing = false
                        break
                    case ~/^move (bob|mary) (right|left|up|down)$/:
                        def name = Matcher.lastMatcher[0][1]
                        def direction = Matcher.lastMatcher[0][2]
                        world.tell(new MoveGladiator(gladiator: gladiators[name](), direction: DIRECTIONS[direction]), ActorRef.noSender())
                        break
                    case ~/^(bob|mary) attacks (bob|mary) roll (\d{1,2})$/:
                        def attacker = Matcher.lastMatcher[0][1]
                        def defender = Matcher.lastMatcher[0][2]
                        def roll = Matcher.lastMatcher[0][3].toInteger()
                        gladiators[defender]().tell(new Attack(roll: roll), gladiators[attacker]())
                        break
                }

            }
        }
    }

    public static void main(String[] args) {
        new Battle().run()
    }

}
