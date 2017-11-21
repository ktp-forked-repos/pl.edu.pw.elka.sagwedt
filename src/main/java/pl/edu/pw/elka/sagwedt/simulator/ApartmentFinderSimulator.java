package pl.edu.pw.elka.sagwedt.simulator;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import pl.edu.pw.elka.sagwedt.broker.BrokerContainer;
import pl.edu.pw.elka.sagwedt.crawler.AbstractCrawler;
import pl.edu.pw.elka.sagwedt.crawler.GumtreeCrawler;
import pl.edu.pw.elka.sagwedt.crawler.OlxCrawler;
import pl.edu.pw.elka.sagwedt.crawler.OtodomCrawler;
import pl.edu.pw.elka.sagwedt.finder.FinderContainer;
import pl.edu.pw.elka.sagwedt.infrastructure.Configuration;
import pl.edu.pw.elka.sagwedt.printer.Printer;
import pl.edu.pw.elka.sagwedt.seeker.SeekApartmentRequest;
import pl.edu.pw.elka.sagwedt.seeker.SeekerContainer;

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
        final ActorRef printer = system.actorOf(Printer.props(), "Printer");
        final ArrayList<AbstractCrawler> cralwerList = Lists.newArrayList(new GumtreeCrawler(), new OlxCrawler(), new OtodomCrawler());
        final ActorRef finderContainerRef = system.actorOf(FinderContainer.props(cralwerList, printer), "FinderContainer");
        final ActorRef brokerContainerRef = system.actorOf(BrokerContainer.props(finderContainerRef, printer), "BrokerContainer");
        final ActorRef seekerContainerRef = system.actorOf(SeekerContainer.props(brokerContainerRef, printer), "SeekerContainer");
        for(int i = 0; i < Configuration.SEEKERS_TO_CREATE_DURING_SIMULATION_COUNT; ++i)
        {
            seekerContainerRef.tell(new SeekApartmentRequest(), ActorRef.noSender());
        }
    }
}
