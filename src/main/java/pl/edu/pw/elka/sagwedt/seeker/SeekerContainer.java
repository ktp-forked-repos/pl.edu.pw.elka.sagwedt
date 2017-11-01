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
class SeekerContainer extends AbstractApplicationActor
{
    private final SeekerFactory seekerFactory;
    private final Set<ActorRef> seekerSet;
    private final int seekersLimit;

    /**
     * Package scoped factory method.
     */
    static Props props(final SeekerFactory seekerFactory,
        final int seekersLimit, final ActorRef printer)
    {
        return Props.create(SeekerContainer.class,
            () -> new SeekerContainer(seekerFactory, seekersLimit, printer));
    }

    /**
     * Private constructor to force the use of {@link SeekerContainer#props()}.
     * @param seekerFactory factory for creating {@link Seeker}.
     * @param seekersLimit how many seekers can be handled simultanously
     */
    private SeekerContainer(final SeekerFactory seekerFactory,
            final int seekersLimit, final ActorRef printer)
    {
        super(printer);
        this.seekerFactory = seekerFactory;
        this.seekersLimit = seekersLimit;
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
        if(seekerSet.size() >= seekersLimit)
        {
            log("To many seeker requests, I'm stopping.");
            //TODO handle rejection
            throw new RuntimeException("Seekers limit exceeded");
        }
        final ActorRef seeker = seekerFactory.getSeeker(getContext());
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
    }
}