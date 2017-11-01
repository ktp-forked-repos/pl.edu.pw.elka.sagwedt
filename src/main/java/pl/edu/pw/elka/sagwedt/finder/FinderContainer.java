package pl.edu.pw.elka.sagwedt.finder;

import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;
import pl.edu.pw.elka.sagwedt.infrastructure.AbstractApplicationActor;
import scala.util.Random;

/**
 * Actor that encapsulates a set of {@link Finder} actors.
 */
class FinderContainer extends AbstractApplicationActor
{
    private static final Random RANDOM = new Random();

    /** Set of {@link Finder} actor references in this {@link FinderContainer}*/
    private final List<ActorRef> finderRefList;

    /**
     * Package scoped factory method.
     */
    static Props props(final List<ActorRef> finderRefList, final ActorRef printer)
    {
        return Props.create(FinderContainer.class,
            () -> new FinderContainer(finderRefList, printer));
    }

    /**
     * Private constructor to force the use of {@link FinderContainer#props(int)} method.
     */
    private FinderContainer(final List<ActorRef> finderRefList, final ActorRef printer)
    {
        super(printer);
        this.finderRefList = finderRefList;
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