package pl.edu.pw.elka.sagwedt.seeker;

import java.util.Map;

import com.google.common.collect.Maps;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.broker.SelectApartmentRequest;
import pl.edu.pw.elka.sagwedt.broker.SelectApartmentResponse;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.TimeoutExceededResponse;

/**
 * Actor that is searching for an apartment.
 */
class Seeker extends AbstractAppActor
{
    private final ActorRef brokerContainerRef;
    private final Map<SelectApartmentRequest, SeekApartmentRequest> requestMap;

    /**
     * Package scoped factory method.
     * @param brokerContainerRef reference to broker container
     */
    static Props props(final ActorRef brokerContainerRef, final ActorRef printer)
    {
        return Props.create(Seeker.class,
            () -> new Seeker(brokerContainerRef, printer));
    }

    /**
     * Private constructor to force the use of {@link Seeker#props()}.
     * @param brokerContainerRef reference to broker container
     */
    private Seeker(final ActorRef brokerContainerRef, final ActorRef printer)
    {
        super(printer);
        this.brokerContainerRef = brokerContainerRef;
        this.requestMap = Maps.newHashMap();
    }

    /**
     * Handling method for messages.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(SeekApartmentRequest.class, this::handle)
            .match(SelectApartmentResponse.class, this::stopWaitingForResponse, this::handle)
            .match(TimeoutExceededResponse.class, this::stopWaitingForResponse, this::handle)
            .build();
    }

    /**
     * Handling of {@link SeekApartmentRequest}.
     */
    private void handle(final SeekApartmentRequest request)
    {
        log("Want to seek for apartment, asking " + getName(brokerContainerRef) + " for help");
        final SelectApartmentRequest selectApartmentRequest = new SelectApartmentRequest();
        requestMap.put(selectApartmentRequest, request);
        brokerContainerRef.tell(selectApartmentRequest, getSelf());
        waitForResponse(selectApartmentRequest);
    }

    /**
     * Handling of {@link SelectApartmentResponse}.
     */
    private void handle(final SelectApartmentResponse selectApartmentResponse)
    {
        if(selectApartmentResponse.isApartmentFound())
        {
            log("Received apartment offer from " + getName(getSender()) + ", telling " + getName(getContext().getParent()) + " that I'm done.");
        }
        else
        {
            log("Didn't receive apartment offer from " + getName(getSender()) + ", telling " + getName(getContext().getParent()) + " that I'm done.");
        }
        final SeekApartmentRequest seekApartmentRequest = requestMap.remove(selectApartmentResponse.getRequest());
        final SeekApartmentResponse seekApartmentResponse = new SeekApartmentResponse(seekApartmentRequest);
        getContext().getParent().tell(seekApartmentResponse, getSelf());
    }

    /**
     * Handling of await timeout for response exceeded message.
     */
    private void handle(final TimeoutExceededResponse<SelectApartmentRequest> response)
    {
        log("Timeout exceeded while waiting for response to select apartment, telling " + getName(getContext().getParent()) + " that I'm done.");
        final SeekApartmentRequest seekApartmentRequest = requestMap.remove(response.getRequest());
        final SeekApartmentResponse seekApartmentResponse = new SeekApartmentResponse(seekApartmentRequest);
        getContext().getParent().tell(seekApartmentResponse, getSelf());
    }
}