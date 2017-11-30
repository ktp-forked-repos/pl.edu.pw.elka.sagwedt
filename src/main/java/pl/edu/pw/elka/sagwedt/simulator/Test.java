package pl.edu.pw.elka.sagwedt.simulator;

import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import scala.concurrent.duration.FiniteDuration;

/**
 * Entry class for simulation.
 */
public class Test {

    /**
     * Entry point for simulation.
     */
    public static void main(final String[] args)
    {
        final ActorSystem system = ActorSystem.create();
        final ActorRef a = system.actorOf(A.props(null), "A");
        final ActorRef b = system.actorOf(A.props(a), "B");
        final ActorRef c = system.actorOf(A.props(b), "C");
        c.tell("ping", ActorRef.noSender());
    }

    private static class A extends AbstractActor
    {
        private final ActorRef child;

        public static Props props(final ActorRef child) { return Props.create(A.class, () -> new A(child)); }

        private A(final ActorRef child) { this.child = child;}

        @Override
        public Receive createReceive()
        {
            return receiveBuilder().match(String.class, this::handle).build();
        }

        private void handle(final String msg) throws InterruptedException
        {
            final ActorRef sender = getSender();
            final ActorRef self = getSelf();
            log("received: " + msg);
            if(child != null)
            {
                log("sending: " + msg + " to " + child.path().name());
                PatternsCS.ask(child, msg, Timeout.durationToTimeout(FiniteDuration.create(1, TimeUnit.SECONDS)))
                    .handle((result, exception) -> {
                        if(exception != null) {
                            log("exception " + exception.toString());
                            return false;
                        }
                        if(sender != null) {
                            log("responding " + result + " to " + sender.path().name());
                            sender.tell(result, self);
                        }
                        return true;
                    });
            }
            else
            {
                TimeUnit.SECONDS.sleep(2);
                log("calculating pong to " + sender.path().name());
                sender.tell("pong", self);
            }
        }

        private void log(final String msg)
        {
            System.out.println(getSelf().path().name() + ": " + msg);
        }
    }
}
