package pl.edu.pw.elka.sagwedt.seeker;

import java.util.Set;

import com.google.common.collect.Sets;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractApplicationActor;

/**
 * Container for actors that are searching for apartments.
 */
public class SeekerContainer extends AbstractApplicationActor
{
    private final ActorRef brokerContainerRef;
    private final Set<ActorRef> seekerSet;
    private int seekersCreatedCount = 0;

    /**
     * Package scoped factory method.
     */
    public static Props props(final ActorRef brokerContainerRef, final ActorRef printerRef)
    {
        return Props.create(SeekerContainer.class,
            () -> new SeekerContainer(brokerContainerRef, printerRef));
    }

    /**
     * Private constructor to force the use of {@link SeekerContainer#props()}.
     */
    private SeekerContainer(final ActorRef brokerContainerRef, final ActorRef printerRef)
    {
        super(printerRef);
        this.brokerContainerRef = brokerContainerRef;
        this.seekerSet = Sets.newHashSet();
    }

    /**
     * Handler method for messages.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(SeekApartmentRequest.class, this::handle)
            .match(SeekApartmentResponse.class, this::handle)
            .build();
    }

    /**
     * Handling method for {@link SeekApartmentRequest}.
     */
    private void handle(final SeekApartmentRequest seekApartmentRequest)
    {
        if(seekerSet.size() >= 1000)
        {
            log("To many seeker requests, I'm stopping.");
            //TODO handle rejection
            throw new RuntimeException("Seekers limit exceeded");
        }
        final String name = "Seeker" + seekersCreatedCount++;
        final ActorRef seeker = context().actorOf(Seeker.props(brokerContainerRef, printer), name);
        seekerSet.add(seeker);
        log("Created " + getName(seeker) + " to look for apartment ");
        seeker.tell(seekApartmentRequest, getSelf());
    }

    /**
     * Handling method for {@link SeekApartmentResponse}.
     * Kills a seeker that found an apartment.
     */
    private void handle(final SeekApartmentResponse seekApartmentResponse)
    {
        log("Killing " + getName(getSender()) + ", because apartment found.");
        getSender().tell(PoisonPill.getInstance(), getSelf());
        seekerSet.remove(getSender());
    }
}