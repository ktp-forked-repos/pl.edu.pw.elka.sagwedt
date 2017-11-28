package pl.edu.pw.elka.sagwedt.seeker;

import java.util.Map;

import com.google.common.collect.Maps;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.broker.BrokerApartmentRequest;
import pl.edu.pw.elka.sagwedt.broker.BrokerApartmentResponse;
import pl.edu.pw.elka.sagwedt.finder.Apartment;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.TimeoutExceededResponse;

/**
 * Actor that is searching for an apartment.
 */
class Seeker extends AbstractAppActor
{
    private final ActorRef brokerContainerRef;
    private final Map<BrokerApartmentRequest, SeekApartmentRequest> requestMap;

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
            .match(BrokerApartmentResponse.class, this::stopWaitingForResponse, this::handle)
            .match(TimeoutExceededResponse.class, this::stopWaitingForResponse, this::handle)
            .build();
    }

    /**
     * Handling of {@link SeekApartmentRequest}.
     */
    private void handle(final SeekApartmentRequest request)
    {
        log("Want to seek for apartment, asking " + getName(brokerContainerRef) + " for help");
        final BrokerApartmentRequest selectApartmentRequest = new BrokerApartmentRequest(request);
        requestMap.put(selectApartmentRequest, request);
        brokerContainerRef.tell(selectApartmentRequest, getSelf());
        waitForResponse(selectApartmentRequest);
    }

    /**
     * Handling of {@link BrokerApartmentResponse}.
     */
    private void handle(final BrokerApartmentResponse selectApartmentResponse)
    {
        if(selectApartmentResponse.isApartmentFound())
        {
            log("Received apartment offer from " + getName(getSender()) + ", telling " + getName(getContext().getParent()) + " that I'm done.");
            logFoundApartament(selectApartmentResponse.getApartment());
        }
        else
        {
            log("Didn't receive apartment offer from " + getName(getSender()) + ", telling " + getName(getContext().getParent()) + " that I'm done.");
        }
        final SeekApartmentRequest seekApartmentRequest = requestMap.remove(selectApartmentResponse.getRequest());
        final SeekApartmentResponse seekApartmentResponse = new SeekApartmentResponse(seekApartmentRequest);
        getContext().getParent().tell(seekApartmentResponse, getSelf());
    }
    
    private void logFoundApartament(final Apartment apartament) {
    	if(apartament.getType() != null) log("Type: " + apartament.getType());
    	if(apartament.getPrice() != null) log("Price: " + apartament.getPrice());
    	if(apartament.getArea() != null) log("Area: " + apartament.getArea());
    	if(apartament.getDistrict() != null) log("District: " + apartament.getDistrict());
    	if(apartament.getNumberOfRooms() != null) log("NumberOfRooms: " + apartament.getDistrict());
    	if(apartament.getBuildYear() != null) log("BuildYear: " + apartament.getBuildYear());
    	if(apartament.getEmail() != null) log("Email: " + apartament.getEmail());
    	if(apartament.getTelephone() != null) log("Telephone: " + apartament.getTelephone());
    	if(apartament.getURL() != null) log("URL: " + apartament.getURL());
    }

    /**
     * Handling of await timeout for response exceeded message.
     */
    private void handle(final TimeoutExceededResponse<BrokerApartmentRequest> response)
    {
        log("Timeout exceeded while waiting for response to select apartment, telling " + getName(getContext().getParent()) + " that I'm done.");
        final SeekApartmentRequest seekApartmentRequest = requestMap.remove(response.getRequest());
        final SeekApartmentResponse seekApartmentResponse = new SeekApartmentResponse(seekApartmentRequest);
        getContext().getParent().tell(seekApartmentResponse, getSelf());
    }
}