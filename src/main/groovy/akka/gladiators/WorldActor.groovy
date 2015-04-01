package akka.gladiators

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor
import akka.actor.UntypedActorFactory
import akka.japi.Creator
import groovy.transform.Immutable

class WorldActor extends UntypedActor {

    Closure listener
    ActorRef map

    public WorldActor(Closure listener) {
        this.listener = listener
        map = context.actorOf(MapActor.create())
    }

    @Override
    void onReceive(Object message) throws Exception {
        process(message)
    }

    def process(MapActor.MapState message) {
        listener.call(message)
    }

    def process(GladiatorState message) {
        listener.call(message)
    }

    def process(SetPosition message) {
        map.tell(message, self)
    }

    def process(MoveGladiator message) {
        map.tell(message, self)
    }

    def process(CreateGladiator message) {
        ActorRef gladiator = context.actorOf(GladiatorActor.create())
        sender.tell(new CreatedGladiator(gladiator: gladiator), self)
    }

    def process(message) {
        unhandled(message)
    }

    public static Props create(Closure listener) {
        Props.create(new WorldCreator(listener))
    }

    static class WorldCreator implements Creator<WorldActor> {

        Closure listener

        public WorldCreator(Closure listener) {
            this.listener = listener
        }

        @Override
        WorldActor create() throws Exception {
            new WorldActor(listener)
        }
    }

}

class CreateGladiator {}

class CreatedGladiator {
    ActorRef gladiator
}

