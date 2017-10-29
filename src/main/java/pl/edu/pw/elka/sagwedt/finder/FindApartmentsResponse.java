package pl.edu.pw.elka.sagwedt.finder;

import java.util.List;

/**
 * Message returned to sender after {@link Finder} received {@link FindApartmentsRequest}.
 *
 */
public class FindApartmentsResponse
{
    private final FindApartmentsRequest request;
    private final List<Apartment> apartmentList;

    /**
     * Package scoped constructor.
     */
    FindApartmentsResponse(final FindApartmentsRequest request, final List<Apartment> apartmentList)
    {
        this.request = request;
        this.apartmentList = apartmentList;
    }

    /**
     * Request for which this response was created.
     */
    public FindApartmentsRequest getRequest()
    {
        return request;
    }

    /**
     * Returns the apartment that were found due to {@link FindApartmentsRequest}.
     */
    public List<Apartment> getApartmentList()
    {
        return apartmentList;
    }
}