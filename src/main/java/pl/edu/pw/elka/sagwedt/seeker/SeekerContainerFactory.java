package pl.edu.pw.elka.sagwedt.seeker;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * Factory for {@link SeekerContainer} components.
 */
public class SeekerContainerFactory
{
    /**
     * Returns new {@link SeekerContainer} actor reference.
     * @param printer
     */
    public ActorRef getSeekerContainer(final ActorSystem actorContext, final ActorRef printer, final int seekersLimit,
        final ActorRef brokerContainerRef)
    {
        return actorContext.actorOf(SeekerContainer.props(new SeekerFactory(brokerContainerRef, printer), seekersLimit, printer), "SeekerContainer");
    }
}