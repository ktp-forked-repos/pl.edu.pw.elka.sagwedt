package pl.edu.pw.elka.sagwedt.seeker;

import java.util.Map;

import com.google.common.collect.Maps;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.Configuration;
import pl.edu.pw.elka.sagwedt.infrastructure.TimeoutExceededResponse;

/**
 * Container for actors that are searching for apartments.
 */
public class SeekerContainer extends AbstractAppActor
{
    private final ActorRef brokerContainerRef;
    private final Map<SeekApartmentRequest, ActorRef> seekerMap;
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
        this.seekerMap = Maps.newHashMap();
    }

    /**
     * Handler method for messages.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(SeekApartmentRequest.class, this::handle)
            .match(SeekApartmentResponse.class, this::stopWaitingForResponse, this::handle)
            .match(TimeoutExceededResponse.class, this::stopWaitingForResponse, this::handle)
            .build();
    }

    /**
     * Handling method for {@link SeekApartmentRequest}.
     */
    private void handle(final SeekApartmentRequest seekApartmentRequest)
    {
        if(seekerMap.size() >= Configuration.MAX_SEEKERS_IN_CONTAINER_COUNT)
        {
            log("To many seeker requests, I'm stopping.");
            //TODO handle rejection
            throw new RuntimeException("Seekers limit exceeded");
        }
        final String name = "Seeker" + seekersCreatedCount++;
        final ActorRef seeker = context().actorOf(Seeker.props(brokerContainerRef, printer), name);
        log("Created " + getName(seeker) + " to look for apartment ");
        waitForResponse(seekApartmentRequest);
        seeker.tell(seekApartmentRequest, getSelf());
        seekerMap.put(seekApartmentRequest, seeker);
    }

    /**
     * Handling method for {@link SeekApartmentResponse}.
     * Kills a seeker that found an apartment.
     */
    private void handle(final SeekApartmentResponse seekApartmentResponse)
    {
        final ActorRef seeker = seekerMap.remove(seekApartmentResponse.getRequest());
        log("Killing " + getName(seeker) + ", because it is done working.");
        seeker.tell(PoisonPill.getInstance(), getSelf());
    }

    /**
     * Handling of timeout for waiting for response exceeded.
     */
    private void handle(final TimeoutExceededResponse<SeekApartmentRequest> timeoutMessage)
    {
        final ActorRef seeker = seekerMap.remove(timeoutMessage.getRequest());
        log("Killing " + getName(seeker) + ", because it's response timeout has been exceeded.");
        seeker.tell(PoisonPill.getInstance(), getSelf());
    }
}