package pl.edu.pw.elka.sagwedt.finder;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.crawler.AbstractCrawler;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractApplicationActor;
import scala.util.Random;

/**
 * Actor that encapsulates a set of {@link Finder} actors.
 */
public class FinderContainer extends AbstractApplicationActor
{
    private static final Random RANDOM = new Random();
    private final List<ActorRef> finderRefList;

    /**
     * Package scoped factory method.
     */
    public static Props props(final List<AbstractCrawler> crawlerList, final ActorRef printer)
    {
        return Props.create(FinderContainer.class,
            () -> new FinderContainer(crawlerList, printer));
    }

    /**
     * Private constructor to force the use of {@link FinderContainer#props(int)} method.
     */
    private FinderContainer(final List<AbstractCrawler> crawlerList, final ActorRef printer)
    {
        super(printer);
        this.finderRefList = createFinderList(crawlerList, printer);
    }

    /**
     * Create list of finder actors.
     */
    private ArrayList<ActorRef> createFinderList(final List<AbstractCrawler> crawlerList, final ActorRef printer)
    {
        if (crawlerList.size() < 1)
        {
            throw new RuntimeException("At least one crawler required");
        }
        final ArrayList<ActorRef> result = Lists.newArrayList();
        int finderCount = 0;
        for(final AbstractCrawler crawler : crawlerList)
        {
            final String finderName = "Finder" + finderCount++;
            final ActorRef finderRef = context().actorOf(Finder.props(printer, crawler), finderName);
            result.add(finderRef);
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
            .match(FindApartmentsRequest.class, this::handle)
            .build();
    }

    /**
     * Handle method for {@link FindApartmentsRequest}.
     * Delegates the task to one of finders.
     */
    private void handle(final FindApartmentsRequest msg)
    {
        final ActorRef finder = getRandomFinder();
        finder.tell(msg, getSender());
        log("Delegating " + getName(getSender()) + " to " + getName(finder));
    }

    /**
     * Returns a random finder.
     */
    private ActorRef getRandomFinder()
    {
        final int randomInt = RANDOM.nextInt(finderRefList.size());
        return finderRefList.get(randomInt);
    }
}