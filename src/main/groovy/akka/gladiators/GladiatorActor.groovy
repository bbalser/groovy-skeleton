package akka.gladiators

import akka.actor.Props
import akka.actor.UntypedActor

class GladiatorActor extends UntypedActor {

    int hitpoints = 10
    int armorClass = 10

    @Override
    void onReceive(Object message) {
        switch (message) {
            case GetGladiatorState:
                sender.tell(createState(), self)
                break
            case Attack:
                hitpoints--
                context.parent().tell(createState(), self)
                break
        }

    }

    GladiatorState createState() {
        new GladiatorState(hitpoints: hitpoints, armorClass: armorClass)
    }

    static Props create() {
        Props.create(GladiatorActor)
    }

}

class GetGladiatorState {}

class GladiatorState {
    int hitpoints
    int armorClass
}

class Attack {
    int roll
}

