package pl.edu.pw.elka.sagwedt.seeker;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.Configuration;

/**
 * Container for actors that are searching for apartments.
 */
public class SeekerContainer extends AbstractAppActor
{
    private final ActorRef brokerContainerRef;
    private int seekersCreatedCount = 0;

    /**
     * Package scoped factory method.
     */
    public static Props props(final ActorRef brokerContainerRef, final ActorRef printerRef)
    {
        return Props.create(SeekerContainer.class, () -> new SeekerContainer(brokerContainerRef, printerRef));
    }

    /**
     * Private constructor to force the use of {@link SeekerContainer#props()}.
     */
    private SeekerContainer(final ActorRef brokerContainerRef, final ActorRef printerRef)
    {
        super(printerRef);
        this.brokerContainerRef = brokerContainerRef;
    }

    /**
     * Handler method for messages.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder().match(SeekApartmentRequest.class, this::handle).build();
    }

    /**
     * Handling method for {@link SeekApartmentRequest}.
     */
    private void handle(final SeekApartmentRequest seekApartmentRequest)
    {
        if (seekersCreatedCount >= Configuration.MAX_SEEKERS_IN_CONTAINER_COUNT)
        {
            log("To many seeker requests, I'm stopping.");
            throw new RuntimeException("Seekers limit exceeded");
        }
        final String name = "Seeker" + seekersCreatedCount++;
        final ActorRef seeker = context().actorOf(Seeker.props(brokerContainerRef, printer), name);
        log("Created " + getName(seeker) + " to look for apartment ");
        PatternsCS.ask(seeker, seekApartmentRequest, Configuration.RESPONSE_TIMOUT).handle((response, exception) ->
        {
            if (exception != null)
            {
                log("Killing " + getName(seeker) + ", because it's response timeout has been exceeded.");
                seeker.tell(PoisonPill.getInstance(), getSelf());
                return false;
            }
            log("Killing " + getName(seeker) + ", because it is done working.");
            seeker.tell(PoisonPill.getInstance(), getSelf());
            return true;
        });
    }
}