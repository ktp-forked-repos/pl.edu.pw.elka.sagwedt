package pl.edu.pw.elka.sagwedt.broker;

import pl.edu.pw.elka.sagwedt.finder.Apartment;
import pl.edu.pw.elka.sagwedt.infrastructure.AppResponse;

/**
 * A response to {@link BrokerApartmentRequest}.
 */
public class BrokerApartmentResponse extends AppResponse<BrokerApartmentRequest>
{
    private final Apartment apartment;

    BrokerApartmentResponse(final BrokerApartmentRequest request, final Apartment apartment)
    {
        super(request);
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