package pl.edu.pw.elka.sagwedt.broker;

import java.util.Map;

import com.google.common.collect.Maps;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.finder.Apartment;
import pl.edu.pw.elka.sagwedt.finder.FindApartmentsRequest;
import pl.edu.pw.elka.sagwedt.finder.FindApartmentsResponse;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractApplicationActor;

/**
 * A proxy actor between Finder and Seeker.
 */
class Broker extends AbstractApplicationActor
{
    private final ActorRef finderContainerRef;
    private final Map<FindApartmentsRequest, ActorRef> actorOfRequestMap;
    private final Map<FindApartmentsRequest, SelectApartmentRequest> requestMap;

    /**
     * Package scoped factory method.
     * @param finderContainerRef {@link FinderContainer} to be used for apartment search
     */
    static Props props(final ActorRef finderContainerRef, final ActorRef printer)
    {
        return Props.create(Broker.class,
            () -> new Broker(finderContainerRef, printer));
    }

    /**
     * Private constructor to force the use of {@link Broker#props()} method.
     * @param finderContainerRef {@link FinderContainer} to be used for apartment search
     */
    private Broker(final ActorRef finderContainerRef, final ActorRef printer)
    {
        super(printer);
        this.finderContainerRef = finderContainerRef;
        this.actorOfRequestMap = Maps.newHashMap();
        this.requestMap = Maps.newHashMap();
    }

    /**
     * Message handling method.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(SelectApartmentRequest.class, this::handle)
            .match(FindApartmentsResponse.class, this::handle)
            .build();
    }

    /**
     * Handle method for {@link SelectApartmentRequest}.
     * Asks {@link FinderContainer} to find apartments.
     */
    private void handle(final SelectApartmentRequest selectApartmentRequest)
    {
        log("Received request from " + getName(getSender()) + ", asking " + getName(finderContainerRef) + " for apartment offers");
        final FindApartmentsRequest findApartmentsRequest = new FindApartmentsRequest();
        actorOfRequestMap.put(findApartmentsRequest, getSender());
        requestMap.put(findApartmentsRequest, selectApartmentRequest);
        finderContainerRef.tell(findApartmentsRequest, getSelf());
    }

    /**
     * Handling of apartments found message.
     * Selects the best apartment and returns it to seeker.
     */
    private void handle(final FindApartmentsResponse findApartmentResponse)
    {
        final Apartment apartment = getMockBestApartment(findApartmentResponse);
        final ActorRef requestSender = actorOfRequestMap.remove(findApartmentResponse.getRequest());
        final SelectApartmentRequest request = requestMap.remove(findApartmentResponse.getRequest());
        final SelectApartmentResponse response = new SelectApartmentResponse(request, apartment);
        log("Received response from " + getName(getSender()) + ", returning best apartment offer to " + getName(requestSender));
        requestSender.tell(response, getSelf());
    }

    /**
     * Selects the best apartment from apartment list.
     * @deprecated should be replaced with a method that performs real selection.
     */
    @Deprecated
    private Apartment getMockBestApartment(final FindApartmentsResponse response)
    {
        if(response.getApartmentList().isEmpty())
        {
            //TODO implement apartments not found handling
            throw new RuntimeException("Apartments not found!");
        }
        return response.getApartmentList().get(0);
    }
}