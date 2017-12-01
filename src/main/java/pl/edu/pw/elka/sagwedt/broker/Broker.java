package pl.edu.pw.elka.sagwedt.broker;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import pl.edu.pw.elka.sagwedt.finder.Apartment;
import pl.edu.pw.elka.sagwedt.finder.FindApartmentsRequest;
import pl.edu.pw.elka.sagwedt.finder.FindApartmentsResponse;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.Configuration;

/**
 * A proxy actor between Finder and Seeker.
 */
class Broker extends AbstractAppActor
{
    private final ActorRef finder;

    /**
     * Package scoped factory method.
     * @param finder to be used for apartment search
     */
    static Props props(final ActorRef finder, final ActorRef printer)
    {
        return Props.create(Broker.class,
            () -> new Broker(finder, printer));
    }

    /**
     * Private constructor to force the use of {@link Broker#props()} method.
     * @param finder to be used for apartment search
     */
    private Broker(final ActorRef finder, final ActorRef printer)
    {
        super(printer);
        this.finder = finder;
    }

    /**
     * Message handling method.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(BrokerApartmentRequest.class, this::handle)
            .build();
    }

    /**
     * Handle method for {@link BrokerApartmentRequest}.
     * Asks {@link #finder} to find apartments.
     */
    private void handle(final BrokerApartmentRequest brokerRequest)
    {
        final ActorRef sender = getSender();
        log("Received request from " + getName(sender) + ", asking " + getName(finder) + " for apartment offers");
        final FindApartmentsRequest findRequest = new FindApartmentsRequest();
        PatternsCS.ask(finder, findRequest, Configuration.RESPONSE_TIMOUT)
            .handle((response, exception) -> {
                if(exception != null) {
                    final BrokerApartmentResponse brokerResponse = new BrokerApartmentResponse(null);
                    log("Timeout exceeded while waiting for " + getName(finder) + "'s response to find apartment for " + getName(sender));
                    sender.tell(brokerResponse, getSelf());
                    return false;
                }
                final FindApartmentsResponse findResponse = (FindApartmentsResponse) response;
                final Apartment apartment = getBestApartment(findResponse, brokerRequest);
                final BrokerApartmentResponse brokerResponse = new BrokerApartmentResponse(apartment);
                log("Received response from " + getName(sender) + ", returning best apartment offer to " + getName(sender));
                sender.tell(brokerResponse, getSelf());
                return true;
            });
    }

    /**
     * Selects the best apartment from apartment list.
     */
    private Apartment getBestApartment(final FindApartmentsResponse response, final BrokerApartmentRequest brokerApartmentRequest)
    {
    	List<Apartment> apartmentList = response.getApartmentList();
    	apartmentList = getApartmentsThatMeetExpectations(apartmentList, brokerApartmentRequest);
        if(apartmentList.isEmpty())
        {
            throw new RuntimeException("Apartments not found!");
        }
        return apartmentList.get(0);
    }

    /**
     * Selects all apartments that meet expectations
     */
    private List<Apartment> getApartmentsThatMeetExpectations(final List<Apartment> input_list, final BrokerApartmentRequest request){
    	final List<Apartment> newlist = new ArrayList<>();
    	for(final Apartment a : input_list) {
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

    	return request.getMaxPrice() > apartment.getPrice() &&
			request.getMinPrice() <= apartment.getPrice() &&
			request.getMaxArea() > apartment.getArea() &&
			request.getMinArea() <= apartment.getArea();
    }
}