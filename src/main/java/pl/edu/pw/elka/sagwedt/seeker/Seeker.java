package pl.edu.pw.elka.sagwedt.seeker;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import pl.edu.pw.elka.sagwedt.broker.BrokerApartmentRequest;
import pl.edu.pw.elka.sagwedt.broker.BrokerApartmentResponse;
import pl.edu.pw.elka.sagwedt.finder.Apartment;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.Configuration;

/**
 * Actor that is searching for an apartment.
 */
class Seeker extends AbstractAppActor
{
    private final ActorRef broker;

    /**
     * Package scoped factory method.
     *
     * @param broker reference to broker
     */
    static Props props(final ActorRef broker, final ActorRef printer)
    {
        return Props.create(Seeker.class, () -> new Seeker(broker, printer));
    }

    /**
     * Private constructor to force the use of {@link Seeker#props()}.
     *
     * @param broker
     *            reference to broker container
     */
    private Seeker(final ActorRef broker, final ActorRef printer)
    {
        super(printer);
        this.broker = broker;
    }

    /**
     * Handling method for messages.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
                .match(SeekApartmentRequest.class, this::handle)
                .build();
    }

    /**
     * Handling of {@link SeekApartmentRequest}.
     */
    private void handle(final SeekApartmentRequest seekRequest)
    {
        final ActorRef parent = getContext().getParent();
        log("Want to seek for apartment, asking " + getName(broker) + " for help");
        final BrokerApartmentRequest brokerRequest = new BrokerApartmentRequest(seekRequest);
        PatternsCS.ask(broker, brokerRequest, Configuration.RESPONSE_TIMOUT).handle((response, exception) ->
        {
            if (exception != null)
            {
                log("Timeout exceeded while waiting for response to select apartment, telling " + getName(parent) + " that I'm done.");
                final SeekApartmentResponse seekResponse = new SeekApartmentResponse();
                parent.tell(seekResponse, getSelf());
                return false;
            }
            final BrokerApartmentResponse brokerResponse = (BrokerApartmentResponse) response;
            if (brokerResponse.isApartmentFound())
            {
                log("Received apartment offer from " + getName(broker) + ", telling " + getName(parent) + " that I'm done.");
                logFoundApartament(brokerResponse.getApartment());
            }
            else
            {
                log("Didn't receive apartment offer from " + getName(broker) + ", telling " + getName(parent) + " that I'm done.");
            }
            final SeekApartmentResponse seekResponse = new SeekApartmentResponse();
            parent.tell(seekResponse, getSelf());
            return true;
        });
    }

    private void logFoundApartament(final Apartment apartament)
    {
        if (apartament.getType() != null)
            log("Type: " + apartament.getType());
        if (apartament.getPrice() != null)
            log("Price: " + apartament.getPrice());
        if (apartament.getArea() != null)
            log("Area: " + apartament.getArea());
        if (apartament.getDistrict() != null)
            log("District: " + apartament.getDistrict());
        if (apartament.getNumberOfRooms() != null)
            log("NumberOfRooms: " + apartament.getNumberOfRooms());
        if (apartament.getBuildYear() != null)
            log("BuildYear: " + apartament.getBuildYear());
        if (apartament.getEmail() != null)
            log("Email: " + apartament.getEmail());
        if (apartament.getTelephone() != null)
            log("Telephone: " + apartament.getTelephone());
        if (apartament.getURL() != null)
            log("URL: " + apartament.getURL());
    }
}