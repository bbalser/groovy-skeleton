package akka.gladiators

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.UntypedActor

class MapActor extends UntypedActor {

    static enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1,0), RIGHT(1,0)

        int xAdjustment
        int yAdjustment

        Direction(x, y) {
            xAdjustment = x
            yAdjustment = y
        }

        def adjust(x, y) {
            [x: x + xAdjustment, y: y + yAdjustment]
        }
    }

    ActorRef[][] grid = new ActorRef[10][10]


    @Override
    void onReceive(Object message) throws Exception {
        switch (message) {
            case SetPosition:
                set(message.x, message.y, message.gladiator)
                break
            case MoveGladiator:
                move(message)
                break
            case GetCoordinate:
                sender.tell(new MapCoordinate(x: message.x, y: message.y, gladiator: find(message.x, message.y)), self)
                break
        }

        sendState()
    }

    private move(MoveGladiator message) {
        def pos = find(message.gladiator)
        if (pos) {
            def newPos = message.direction.adjust(pos.x, pos.y)
            clear(pos.x, pos.y)
            set(newPos.x, newPos.y, message.gladiator)
        }
    }

    private clear(x, y) {
        grid[x][y] = null
    }

    private set(x, y, ActorRef ref) {
        grid[x][y] = ref
    }

    private find(ActorRef gladiator) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (gladiator.is(grid[i][j])) {
                    return [x: i, y: j]
                }
            }
        }
        return null
    }

    private find(x, y) {
        grid[x][y]
    }

    private sendState() {
        sender.tell(MapState.create(grid), self)
    }

    static Props create() {
        Props.create(MapActor)
    }

    static class MapState {
        ActorRef[][] grid

        ActorRef pos(x, y) {
            grid[x][y]
        }

        static create(ActorRef[][] grid) {
            ActorRef[][] copy = new ActorRef[10][10]
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    copy[i][j] = grid[i][j]
                }
            }
            new MapState(grid: copy)
        }
    }

}

class MoveGladiator {
    MapActor.Direction direction
    ActorRef gladiator
}

class GetCoordinate {
    int x
    int y
}

class MapCoordinate {
    int x
    int y
    ActorRef gladiator
}

class SetPosition {
    int x
    int y
    ActorRef gladiator
}
