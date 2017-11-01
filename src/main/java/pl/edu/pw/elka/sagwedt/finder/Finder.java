package pl.edu.pw.elka.sagwedt.finder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractApplicationActor;

/**
 * Actor responsible for finding offers.
 */
class Finder extends AbstractApplicationActor
{
    /**
     * Package scoped factory method.
     */
    static Props props(final ActorRef printer)
    {
        return Props.create(Finder.class,
            () -> new Finder(printer));
    }

    /**
     * Private constructor to force the use of {@link Finder#props()} method.
     */
    private Finder(final ActorRef printer)
    {
        super(printer);
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
        log("Looking for apartments on behalf of " + getName(getSender()));
        final List<Apartment> apartmentList = getMockApartmentList(request);
        final FindApartmentsResponse response = new FindApartmentsResponse(request, apartmentList);
        getSender().tell(response, getSelf());
    }

    /**
     * Gets mock apartment list.
     *
     * @deprecated Should be replaced by a method that performs a real search.
     */
    @Deprecated
    private List<Apartment> getMockApartmentList(final FindApartmentsRequest msg)
    {
        try
        {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (final InterruptedException e)
        {
            //do nothing
        }
        return Lists.newArrayList(
            new Apartment(),
            new Apartment(),
            new Apartment());
    }
}