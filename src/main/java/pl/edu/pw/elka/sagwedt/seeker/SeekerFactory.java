package pl.edu.pw.elka.sagwedt.seeker;

import akka.actor.AbstractActor.ActorContext;
import akka.actor.ActorRef;

/**
 * Factory for {@link Seeker}.
 */
class SeekerFactory
{
    private static Integer seekersCreatedCount = 0;
    private final ActorRef brokerContainerRef;
    private final ActorRef printer;

    SeekerFactory(final ActorRef brokerContainerRef, final ActorRef printer)
    {
        this.brokerContainerRef = brokerContainerRef;
        this.printer = printer;
    }

    /**
     * Returns new {@link Seeker}.
     */
    ActorRef getSeeker(final ActorContext actorContext)
    {
        final String name = "Seeker" + seekersCreatedCount++;
        return actorContext.actorOf(Seeker.props(brokerContainerRef, printer), name);
    }
}
