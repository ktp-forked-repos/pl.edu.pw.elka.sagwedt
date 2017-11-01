package pl.edu.pw.elka.sagwedt.broker;

import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractApplicationActor;
import scala.util.Random;

/**
 * Actor that encapsulates a set of {@link Broker} actors.
 */
class BrokerContainer extends AbstractApplicationActor
{
    private static final Random RANDOM = new Random();
    private final List<ActorRef> brokerRefList;

    /**
     * Package scoped factory method.
     * @param brokerRefList list of brokers to contain
     */
    static Props props(final List<ActorRef> brokerRefList, final ActorRef printer)
    {
        return Props.create(BrokerContainer.class,
            () -> new BrokerContainer(brokerRefList, printer));
    }

    /**
     * Private constructor to force the use of {@link BrokerContainer#props()};
     */
    private BrokerContainer(final List<ActorRef> brokerRefList, final ActorRef printer)
    {
        super(printer);
        this.brokerRefList = brokerRefList;
    }

    /**
     * Message handling method.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(SelectApartmentRequest.class, this::handle)
            .build();
    }

    /**
     * Handle method for {@link SelectApartmentRequest}.
     * Delegates the task to one of brokers.
     */
    private void handle(final SelectApartmentRequest msg)
    {
        final ActorRef broker = getRandomBroker();
        log("Delegating " + getName(getSender()) + " to " + getName(broker));
        broker.tell(msg, getSender());
    }

    /**
     * Returns a random finder.
     */
    private ActorRef getRandomBroker()
    {
        final int randomInt = RANDOM.nextInt(brokerRefList.size());
        return brokerRefList.get(randomInt);
    }
}