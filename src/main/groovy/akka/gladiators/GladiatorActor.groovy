package akka.gladiators

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor

class GladiatorActor extends UntypedActor {

    int hitpoints = 10
    int armorClass = 10

    @Override
    void onReceive(Object message) {
        process(message)
    }

    def process(GetGladiatorState state) {
        sender.tell(createState(), self)
    }

    def process(Attack message) {
        if (message.roll >= armorClass) {
            hitpoints--
        }
        context.parent().tell(createState(), self)
    }

    def process(message) {
        unhandled(message)
    }

    GladiatorState createState() {
        new GladiatorState(hitpoints: hitpoints, armorClass: armorClass, gladiator: self)
    }

    static Props create() {
        Props.create(GladiatorActor)
    }

}

class GetGladiatorState {}

class GladiatorState {
    int hitpoints
    int armorClass
    ActorRef gladiator
}

class Attack {
    int roll
}

