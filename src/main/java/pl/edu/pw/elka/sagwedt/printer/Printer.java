package pl.edu.pw.elka.sagwedt.printer;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Actor responsible for logging information.
 */
public class Printer extends AbstractActor
{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Factory method.
     */
    public static Props props()
    {
        return Props.create(Printer.class, Printer::new);
    }

    /**
     * Private constructor to force the use of {@link Printer#props()} method.
     */
    private Printer()
    {
        // intentionally empty
    }

    /**
     * Message handling method.
     */
    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
            .match(PrintRequest.class, this::handle)
            .build();
    }

    /**
     * Handle method for {@link PrintRequest}.
     */
    private void handle(final PrintRequest printMsg)
    {
        log.info(printMsg.getMsg());
    }
}