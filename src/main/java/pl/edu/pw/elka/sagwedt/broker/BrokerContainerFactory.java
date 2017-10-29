package pl.edu.pw.elka.sagwedt.broker;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.IntStream;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * Factory class for creating {@link Broker} components.
 */
public class BrokerContainerFactory
{
    /**
     * Factory method for {@link BrokerContainer}.
     * @param printer
     * @param howManyBrokers how many brokers should be created.
     */
    public ActorRef getBrokerContainer(final ActorSystem context,
        final ActorRef printer, final int howManyBrokers, final ActorRef finderContainerRef)
    {
        if(howManyBrokers < 1)
        {
            throw new RuntimeException("FinderContainer should have at least one Finder");
        }
        final List<ActorRef> brokerRefList = IntStream.range(0, howManyBrokers).boxed()
            .map(id -> getBroker(context, printer, finderContainerRef, id))
            .collect(toList());
        return context.actorOf(BrokerContainer.props(brokerRefList, printer), "BrokerContainer");
    }

    private ActorRef getBroker(final ActorSystem context,
        final ActorRef printer, final ActorRef finderContainerRef, final Integer id)
    {
        final String name = "Broker" + id;
        return context.actorOf(Broker.props(finderContainerRef, printer), name);
    }
}
