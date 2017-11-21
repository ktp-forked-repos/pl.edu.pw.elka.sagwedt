package pl.edu.pw.elka.sagwedt.infrastructure;

/**
 * Message that tells actor that waiting time for response
 * to other message sent by this actor has been exceeded.
 *
 * This message should be ignored if response to message
 * specified in it has already been received, or if not
 * it should force the actor to acknowledge that there
 * will be no reponse.
 *
 */
public class TimeoutExceededResponse <T extends AppRequest> extends AppResponse<T>
{
    /**
     * Package scoped constructor.
     *
     * @param originalRequest request for which response
     *      shouldn't be awaited for anymore.
     */
    TimeoutExceededResponse(final T request)
    {
        super(request);
    }
}
