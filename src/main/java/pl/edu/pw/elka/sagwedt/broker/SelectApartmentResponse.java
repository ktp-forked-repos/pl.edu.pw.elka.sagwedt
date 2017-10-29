package pl.edu.pw.elka.sagwedt.broker;

import pl.edu.pw.elka.sagwedt.finder.Apartment;

/**
 * A response to {@link SelectApartmentRequest}.
 */
public class SelectApartmentResponse
{
    private final SelectApartmentRequest request;
    private final Apartment apartment;

    SelectApartmentResponse(final SelectApartmentRequest request, final Apartment apartment)
    {
        this.request = request;
        this.apartment = apartment;
    }

    public SelectApartmentRequest getRequest()
    {
        return request;
    }

    public Apartment getApartment()
    {
        return apartment;
    }
}