package akka

import akka.actor.Props
import akka.actor.UntypedActor

class EchoActor extends UntypedActor {

    static create() {
        Props.create(EchoActor)
    }

    @Override
    void onReceive(Object message) {
        sender.tell(message, self)
    }


}
