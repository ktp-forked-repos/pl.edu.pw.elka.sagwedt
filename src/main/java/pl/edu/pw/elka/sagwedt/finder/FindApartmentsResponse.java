package pl.edu.pw.elka.sagwedt.finder;

import java.util.List;

/**
 * Message returned to sender after {@link Finder} received {@link FindApartmentsRequest}.
 *
 */
public class FindApartmentsResponse
{
    private final List<Apartment> apartmentList;

    /**
     * Package scoped constructor.
     */
    FindApartmentsResponse(final List<Apartment> apartmentList)
    {
        this.apartmentList = apartmentList;
    }

    /**
     * Returns the apartment that were found due to {@link FindApartmentsRequest}.
     */
    public List<Apartment> getApartmentList()
    {
        return apartmentList;
    }
}