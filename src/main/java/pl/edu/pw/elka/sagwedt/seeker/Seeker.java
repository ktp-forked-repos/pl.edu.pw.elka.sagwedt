package pl.edu.pw.elka.sagwedt.seeker;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.broker.SelectApartmentRequest;
import pl.edu.pw.elka.sagwedt.broker.SelectApartmentResponse;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractApplicationActor;

/**
 * Actor that is searching for an apartment.
 */
class Seeker extends AbstractApplicationActor
{
    private final ActorRef brokerContainerRef;

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
    }

    /**
     * Handling method for messages.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(SeekApartmentRequest.class, this::handle)
            .match(SelectApartmentResponse.class, this::handle)
            .build();
    }

    /**
     * Handling of {@link SeekApartmentRequest}.
     */
    private void handle(final SeekApartmentRequest request)
    {
        log("Want to seek for apartment, asking " + getName(brokerContainerRef) + " for help");
        final SelectApartmentRequest selectApartmentRequest = new SelectApartmentRequest();
        brokerContainerRef.tell(selectApartmentRequest, getSelf());
    }

    /**
     * Handling of {@link SelectApartmentResponse}.
     */
    private void handle(final SelectApartmentResponse selectApartmentResponse)
    {
        log("Received apartment offer from " + getName(getSender()) + ", telling " + getName(getContext().getParent()) + " that I'm done.");
        final SeekApartmentResponse seekApartmentResponse = new SeekApartmentResponse();
        getContext().getParent().tell(seekApartmentResponse, getSelf());
    }
}