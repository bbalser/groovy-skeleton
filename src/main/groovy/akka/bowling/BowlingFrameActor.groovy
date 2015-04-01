package akka.bowling

import akka.actor.Props
import akka.actor.UntypedActor

class BowlingFrameActor extends UntypedActor {

    static rollConversions = ["X": 10, "-": 0].withDefault { x -> x.toInteger() }

    @Override
    void onReceive(Object message) {
        def sum = message.inject([sum: 0, previous: 0]) { acc, val ->
            def convertedValue = val == '/' ? 10 - acc.previous : rollConversions[val]
            [sum: acc.sum + convertedValue, previous: convertedValue]
        }.sum

        sender.tell(sum, self)
    }

    static create() {
        Props.create(BowlingFrameActor)
    }
}
