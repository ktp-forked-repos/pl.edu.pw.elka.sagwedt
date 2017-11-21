package pl.edu.pw.elka.sagwedt.infrastructure;

/**
 * Generic reponse to request.
 */
public abstract class AppResponse <T extends AppRequest>
{
    private final T request;

    /**
     * @param request request for this response.
     */
    protected AppResponse(final T request)
    {
        this.request = request;
    }

    public T getRequest()
    {
        return request;
    }
}
