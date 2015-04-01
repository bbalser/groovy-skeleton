package akka.bowling

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor
import akka.routing.RoundRobinPool
import akka.routing.RoundRobinRouter

class BowlingGameActor extends UntypedActor {

//    def frameActor = context.actorOf(new RoundRobinPool(10).props(BowlingFrameActor.create()))
    def frameActor = context.actorOf(BowlingFrameActor.create())
    def listener
    def framesWaitingToBeScored = 0
    def score = 0

    BowlingGameActor(ActorRef listener) {
        this.listener = listener
    }

    @Override
    void onReceive(Object message) {
        switch (message) {
            case String:
                def frames = createFrames(message)
                framesWaitingToBeScored = frames.size()
                score = 0
                frames.each { frameActor.tell(it, self)}
                break;
            case Integer:
                score += message
                framesWaitingToBeScored -= 1
                if (framesWaitingToBeScored == 0) {
                    listener.tell(score, self)
                }
                break;
        }

    }

    def createFrames(message) {
        def calculateFrames
        calculateFrames = { List rolls ->
            if (!rolls) {
                []
            } else if (rolls[0] == 'X') {
                [rolls.take(3)] + calculateFrames(rolls.drop(1))
            } else if (rolls[1] == '/') {
                [rolls.take(3)] + calculateFrames(rolls.drop(2))
            } else {
                [rolls.take(2)] + calculateFrames(rolls.drop(2))
            }
        }

        calculateFrames(message.toList())
    }



    static create(ActorRef listener) {
        Props.create(BowlingGameActor, listener)
    }

}
