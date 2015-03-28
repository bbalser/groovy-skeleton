import actors.EchoActor
import groovyx.gpars.actor.Actors
import spock.lang.Specification

class EchoActorTests extends Specification {

    def "stuff"() {
        given:
        def test = Actors.actor {

        }

        when:
        def result = actor.sendAndWait "Jerks"

        then:
        result == "Jerks"
    }

}
