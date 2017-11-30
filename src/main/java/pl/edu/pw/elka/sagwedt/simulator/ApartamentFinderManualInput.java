package pl.edu.pw.elka.sagwedt.simulator;

import java.util.ArrayList;
import java.util.Scanner;

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

public class ApartamentFinderManualInput {

	public static void main(String[] args) {

	    
        final ActorSystem system = ActorSystem.create();
        final ActorRef printer = system.actorOf(Printer.props(), "Printer");
        final ArrayList<AbstractCrawler> cralwerList = Lists.newArrayList(new GumtreeCrawler(), new OlxCrawler(), new OtodomCrawler());
        final ActorRef finderContainerRef = system.actorOf(FinderContainer.props(cralwerList, printer), "FinderContainer");
        final ActorRef brokerContainerRef = system.actorOf(BrokerContainer.props(finderContainerRef, printer), "BrokerContainer");
        final ActorRef seekerContainerRef = system.actorOf(SeekerContainer.props(brokerContainerRef, printer), "SeekerContainer");
        
        
        Scanner sc = new Scanner(System.in);

		System.out.println("Choose min area:");
	    int minMeters = sc.nextInt();
	    
	    System.out.println("Choose max area:");
	    int maxMeters = sc.nextInt();
	    
		System.out.println("Choose min price:");
	    int minPrice = sc.nextInt();
	    
	    System.out.println("Choose max price:");
	    int maxPrice = sc.nextInt();
	    
        seekerContainerRef.tell(new SeekApartmentRequest(minMeters, maxMeters, minPrice, maxPrice), ActorRef.noSender());

        sc.close();

	}

}
