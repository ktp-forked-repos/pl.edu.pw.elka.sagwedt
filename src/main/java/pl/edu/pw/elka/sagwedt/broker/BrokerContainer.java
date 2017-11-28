package pl.edu.pw.elka.sagwedt.broker;

import java.util.List;

import org.assertj.core.util.Lists;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractAppActor;
import pl.edu.pw.elka.sagwedt.infrastructure.Configuration;
import scala.util.Random;

/**
 * Actor that encapsulates a set of {@link Broker} actors.
 */
public class BrokerContainer extends AbstractAppActor
{
    private static final Random RANDOM = new Random();
    private final List<ActorRef> brokerRefList;

    /**
     * Package scoped factory method.
     * @param brokerRefList list of brokers to contain
     */
    public static Props props(final ActorRef finderContainerRef, final ActorRef printer)
    {
        return Props.create(BrokerContainer.class,
            () -> new BrokerContainer(finderContainerRef, printer));
    }

    /**
     * Private constructor to force the use of {@link BrokerContainer#props()};
     */
    private BrokerContainer(final ActorRef finderContainerRef, final ActorRef printer)
    {
        super(printer);
        this.brokerRefList = getBrokerRefList(Configuration.BROKERS_TO_CREATE_COUNT, finderContainerRef);
    }

    private List<ActorRef> getBrokerRefList(final int howManyBrokers, final ActorRef finderContainerRef)
    {
        if(howManyBrokers < 1)
        {
            throw new RuntimeException("BrokerContainer should have at least one Broker");
        }
        final List<ActorRef> result = Lists.newArrayList();
        for(int i = 0; i < howManyBrokers; ++i)
        {
            final String name = "Broker" + i;
            final ActorRef broker = context().actorOf(Broker.props(finderContainerRef, printer), name);
            result.add(broker);
        }
        return result;
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
     * Delegates the task to one of brokers.
     */
    private void handle(final BrokerApartmentRequest msg)
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