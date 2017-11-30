package pl.edu.pw.elka.sagwedt.broker;

import pl.edu.pw.elka.sagwedt.finder.Apartment;

/**
 * A response to {@link BrokerApartmentRequest}.
 */
public class BrokerApartmentResponse
{
    private final Apartment apartment;

    BrokerApartmentResponse(final Apartment apartment)
    {
        this.apartment = apartment;
    }

    public Apartment getApartment()
    {
        return apartment;
    }

    public boolean isApartmentFound()
    {
        return apartment != null;
    }
}