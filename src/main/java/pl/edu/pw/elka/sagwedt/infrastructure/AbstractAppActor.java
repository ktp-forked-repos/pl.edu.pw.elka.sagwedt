package pl.edu.pw.elka.sagwedt.infrastructure;

import java.util.Set;

import com.google.common.collect.Sets;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import pl.edu.pw.elka.sagwedt.printer.PrintRequest;

/**
 * Abstract actor of this application.
 */
public abstract class AbstractAppActor extends AbstractActor
{
    protected final ActorRef printer;
    private final Set<AppRequest> requestsWaitingForResponse;

    protected AbstractAppActor(final ActorRef printer)
    {
        this.printer = printer;
        this.requestsWaitingForResponse = Sets.newHashSet();
    }

    /**
     * Tells this actor to wait for response to specified request.
     * If response isn't received in some time period then this
     * actor is notified by {@link TimeoutExceededResponse}.
     */
    protected <T extends AppRequest> void waitForResponse(final T request)
    {
        this.requestsWaitingForResponse.add(request);
        context().system().scheduler().scheduleOnce(
                Configuration.RESPONSE_TIMOUT_DURATION,
                getSelf(),
                new TimeoutExceededResponse<T>(request),
                context().system().dispatcher(),
                getSelf());
    }

    /**
     * Stops awaiting for specified response if this actor was
     * awaiting it.
     *
     * @param response response to request for which actor might
     *      have been waiting.
     * @return true if actor was waiting for this response,
     *      false otherwise.
     */
    protected <T extends AppResponse<?>> boolean stopWaitingForResponse(final T response)
    {
        return this.requestsWaitingForResponse.remove(response.getRequest());
    }

    protected void log(final String msg)
    {
        printer.tell(new PrintRequest("# " + getName(getSelf()) + " says: " + msg), getSelf());
    }

    protected static String getName(final ActorRef actorRef)
    {
        return actorRef.path().name();
    }
}
