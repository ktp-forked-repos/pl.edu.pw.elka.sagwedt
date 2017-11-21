package pl.edu.pw.elka.sagwedt.infrastructure;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

/**
 * Application configuration values.
 * TODO should be moved to xml file.
 */
public class Configuration
{
    public static final FiniteDuration RESPONSE_TIMOUT_DURATION = Duration.create(30, TimeUnit.SECONDS);
    public static final Integer SEEKERS_TO_CREATE_DURING_SIMULATION_COUNT = 10;
    public static final Integer MAX_SEEKERS_IN_CONTAINER_COUNT = 100;
    public static final Integer OFFERS_TO_FETCH_COUNT = 100;
    public static final Integer BROKERS_TO_CREATE_COUNT = 5;

    private Configuration()
    {
        //private constructor to prevent from instantiating
    }
}
