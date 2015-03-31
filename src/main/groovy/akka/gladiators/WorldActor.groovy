package akka.gladiators

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor
import akka.japi.Creator

class WorldActor extends UntypedActor {

    Closure listener
    ActorRef map
    ActorRef bob
    ActorRef mary

    public WorldActor(Closure listener) {
        this.listener = listener
//        map = context.actorOf(MapActor.create())
    }

    @Override
    void onReceive(Object message) throws Exception {
        switch (message) {
            case MapActor.MapState:
                listener.call(message)
                break
            case GladiatorState:
                listener.call(message)
                break
            case SetPosition:
                map.tell(message, self)
                break
        }

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

class Start {}