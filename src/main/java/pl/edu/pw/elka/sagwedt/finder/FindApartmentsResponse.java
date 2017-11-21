package pl.edu.pw.elka.sagwedt.finder;

import java.util.List;

import pl.edu.pw.elka.sagwedt.infrastructure.AppResponse;

/**
 * Message returned to sender after {@link Finder} received {@link FindApartmentsRequest}.
 *
 */
public class FindApartmentsResponse extends AppResponse<FindApartmentsRequest>
{
    private final List<Apartment> apartmentList;

    /**
     * Package scoped constructor.
     */
    FindApartmentsResponse(final FindApartmentsRequest request, final List<Apartment> apartmentList)
    {
        super(request);
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