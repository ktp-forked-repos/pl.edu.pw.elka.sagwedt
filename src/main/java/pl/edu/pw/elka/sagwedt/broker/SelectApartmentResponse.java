package pl.edu.pw.elka.sagwedt.broker;

import pl.edu.pw.elka.sagwedt.finder.Apartment;
import pl.edu.pw.elka.sagwedt.infrastructure.AppResponse;

/**
 * A response to {@link SelectApartmentRequest}.
 */
public class SelectApartmentResponse extends AppResponse<SelectApartmentRequest>
{
    private final Apartment apartment;

    SelectApartmentResponse(final SelectApartmentRequest request, final Apartment apartment)
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