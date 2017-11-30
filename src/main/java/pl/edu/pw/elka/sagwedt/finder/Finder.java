package pl.edu.pw.elka.sagwedt.finder;

import static java.util.stream.Collectors.toList;

import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.analytic.Analytic;
import pl.edu.pw.elka.sagwedt.crawler.AbstractCrawler;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.Configuration;

/**
 * Actor responsible for finding offers.
 */
class Finder extends AbstractAppActor
{
    private final AbstractCrawler crawler;
    private List<Apartment> apartmentCache;

    /**
     * Package scoped factory method.
     */
    static Props props(final ActorRef printerRef, final AbstractCrawler crawler)
    {
        return Props.create(Finder.class,
            () -> new Finder(printerRef, crawler));
    }

    /**
     * Private constructor to force the use of {@link Finder#props()} method.
     */
    private Finder(final ActorRef printerRef, final AbstractCrawler crawler)
    {
        super(printerRef);
        this.crawler = crawler;
    }

    /**
     * Message handling method.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(FindApartmentsRequest.class, this::handle)
            .build();
    }

    /**
     * Handle method for {@link FindApartmentsRequest}.
     */
    private void handle(final FindApartmentsRequest request)
    {
        final List<Apartment> apartmentList = getApartmentList(request);
        final FindApartmentsResponse response = new FindApartmentsResponse(request, apartmentList);
        getSender().tell(response, getSelf());
        log("Returning found apartments to " + getName(getSender()));
    }

    /**
     * Gets apartment list.
     */
    private List<Apartment> getApartmentList(final FindApartmentsRequest msg)
    {
        if(apartmentCache == null)
        {
            log("Fetching apartments from web");
            apartmentCache = crawler.getOfferList(Configuration.OFFERS_TO_FETCH_COUNT).stream()
               .map(Analytic::getApartment)
               .collect(toList());
        }
        return apartmentCache;
    }
}