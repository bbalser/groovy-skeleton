package akka

import akka.actor.Props
import akka.actor.UntypedActor

class GiveItBackActor extends UntypedActor {

    static create() {
        Props.create(GiveItBackActor)
    }

    @Override
    void onReceive(Object message) {
        sender.tell(message, self)
    }


}
