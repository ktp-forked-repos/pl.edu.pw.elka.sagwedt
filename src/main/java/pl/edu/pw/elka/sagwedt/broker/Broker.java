package pl.edu.pw.elka.sagwedt.broker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.finder.Apartment;
import pl.edu.pw.elka.sagwedt.finder.FindApartmentsRequest;
import pl.edu.pw.elka.sagwedt.finder.FindApartmentsResponse;
import pl.edu.pw.elka.sagwedt.finder.FinderContainer;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.TimeoutExceededResponse;

/**
 * A proxy actor between Finder and Seeker.
 */
class Broker extends AbstractAppActor
{
    private final ActorRef finderContainerRef;
    private final Map<FindApartmentsRequest, ActorRef> actorOfRequestMap;
    private final Map<FindApartmentsRequest, BrokerApartmentRequest> requestMap;

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
            .match(BrokerApartmentRequest.class, this::handle)
            .match(FindApartmentsResponse.class, this::stopWaitingForResponse, this::handle)
            .match(TimeoutExceededResponse.class, this::stopWaitingForResponse, this::handle)
            .build();
    }

    /**
     * Handle method for {@link BrokerApartmentRequest}.
     * Asks {@link FinderContainer} to find apartments.
     */
    private void handle(final BrokerApartmentRequest selectApartmentRequest)
    {
        log("Received request from " + getName(getSender()) + ", asking " + getName(finderContainerRef) + " for apartment offers");
        final FindApartmentsRequest findApartmentsRequest = new FindApartmentsRequest();
        actorOfRequestMap.put(findApartmentsRequest, getSender());
        requestMap.put(findApartmentsRequest, selectApartmentRequest);
        finderContainerRef.tell(findApartmentsRequest, getSelf());
        waitForResponse(findApartmentsRequest);
    }

    /**
     * Handling of apartments found message.
     * Selects the best apartment and returns it to seeker.
     */
    private void handle(final FindApartmentsResponse findApartmentResponse)
    {
        final Apartment apartment = getBestApartment(findApartmentResponse);
        final ActorRef requestSender = actorOfRequestMap.remove(findApartmentResponse.getRequest());
        final BrokerApartmentRequest request = requestMap.remove(findApartmentResponse.getRequest());
        final BrokerApartmentResponse response = new BrokerApartmentResponse(request, apartment);
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
    
    /**
     * Selects the best apartment from apartment list.
     */    
    private Apartment getBestApartment(final FindApartmentsResponse response)
    {
    	FindApartmentsRequest findApartmentRequest = response.getRequest();
    	BrokerApartmentRequest brokerApartmentRequest = requestMap.get(findApartmentRequest);
    	List<Apartment> apartmentList = response.getApartmentList();
    	apartmentList = getApartmentsThatMeetExpectations(apartmentList, brokerApartmentRequest);
        if(apartmentList.isEmpty())
        {
            //TODO implement apartments not found handling
            throw new RuntimeException("Apartments not found!");
        }
        return apartmentList.get(0);
    }
    
    /**
     * Selects all apartments that meet expectations
     */  
    private List<Apartment> getApartmentsThatMeetExpectations(final List<Apartment> input_list, final BrokerApartmentRequest request){
    	List<Apartment> newlist = new ArrayList<>();
    	for(Apartment a : input_list) {
    		if(apartementMeetsExpectations(a, request))
    			newlist.add(a);
    	}
    	return newlist;
    }
    
    /**
     * Returns true if the Apartment meets expectations
     */  
    private boolean apartementMeetsExpectations(final Apartment apartment, final BrokerApartmentRequest request) {
    	if(apartment.getPrice() == null || apartment.getArea() == null) return false;
    	
    	if(request.getMaxPrice() > apartment.getPrice() && 
    			request.getMinPrice() <= apartment.getPrice() &&
    			request.getMaxArea() > apartment.getArea() &&
    			request.getMinArea() <= apartment.getArea())  
    				return true;
    	return false;
    }

    /**
     * Handle message telling this actor to stop waiting for response.
     */
    private void handle(final TimeoutExceededResponse<FindApartmentsRequest> message)
    {
        final ActorRef requestSender = actorOfRequestMap.remove(message.getRequest());
        final BrokerApartmentRequest request = requestMap.remove(message.getRequest());
        final BrokerApartmentResponse response = new BrokerApartmentResponse(request, null);
        log("Timeout exceeded while waiting for response to find apartment for " + getName(requestSender));
        requestSender.tell(response, getSelf());
    }
}