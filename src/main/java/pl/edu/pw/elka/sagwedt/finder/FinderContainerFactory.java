package pl.edu.pw.elka.sagwedt.finder;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.IntStream;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * Factory class for finder components.
 */
public class FinderContainerFactory
{
    /**
     * Factory method for {@link FinderContainer}.
     * @param printer
     *
     * @param howManyFinders how many finders should be created
     */
    public ActorRef getFinderContainer(final ActorSystem context, final ActorRef printer, final int howManyFinders)
    {
        if (howManyFinders < 1)
        {
            throw new RuntimeException("FinderContainer should have at least one Finder");
        }
        final List<ActorRef> finderRefList = IntStream.range(0, howManyFinders).boxed()
                .map(id -> getFinder(context, printer, id)).collect(toList());
        return context.actorOf(FinderContainer.props(finderRefList, printer), "FinderContainer");
    }

    private ActorRef getFinder(final ActorSystem context, final ActorRef printer, final Integer id)
    {
        final String name = "Finder" + id;
        return context.actorOf(Finder.props(printer), name);
    }
}