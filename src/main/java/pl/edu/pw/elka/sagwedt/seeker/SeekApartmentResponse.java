package pl.edu.pw.elka.sagwedt.seeker;

import pl.edu.pw.elka.sagwedt.infrastructure.AppResponse;

/**
 * Response to {@link SeekApartmentRequest}.
 */
public class SeekApartmentResponse extends AppResponse<SeekApartmentRequest>
{
    SeekApartmentResponse(final SeekApartmentRequest request)
    {
        super(request);
    }
}