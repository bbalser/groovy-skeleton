package actors

import groovyx.gpars.actor.DefaultActor

class EchoActor extends DefaultActor {

    void act() {
        loop {
            react { String message ->
                reply message
            }
        }
    }

}
