package pl.edu.pw.elka.sagwedt.simulator;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import pl.edu.pw.elka.sagwedt.broker.BrokerContainerFactory;
import pl.edu.pw.elka.sagwedt.finder.FinderContainerFactory;
import pl.edu.pw.elka.sagwedt.printer.Printer;
import pl.edu.pw.elka.sagwedt.seeker.SeekApartmentRequest;
import pl.edu.pw.elka.sagwedt.seeker.SeekerContainerFactory;

/**
 * Entry class for simulation.
 */
public class ApartmentFinderSimulator {

    /**
     * Entry point for simulation.
     */
    public static void main(final String[] args)
    {
        final ActorSystem system = ActorSystem.create();
        final ActorRef printer = system.actorOf(Printer.props());
        final ActorRef finderContainerRef = new FinderContainerFactory().getFinderContainer(system, printer, 10);
        final ActorRef brokerContainerRef = new BrokerContainerFactory().getBrokerContainer(system, printer, 5, finderContainerRef);
        final ActorRef seekerContainerRef = new SeekerContainerFactory().getSeekerContainer(system, printer, 100, brokerContainerRef);
        for(int i = 0; i < 10; ++i)
        {
            seekerContainerRef.tell(new SeekApartmentRequest(), ActorRef.noSender());
        }
    }
}
