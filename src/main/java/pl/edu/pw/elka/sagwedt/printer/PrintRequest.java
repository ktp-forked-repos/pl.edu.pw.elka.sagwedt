package pl.edu.pw.elka.sagwedt.printer;

/**
 * Message telling {@link Printer} to print something to console.
 */
public class PrintRequest
{
    private final String msg;

    public PrintRequest(final String msg)
    {
        this.msg = msg;
    }

    String getMsg()
    {
        return msg;
    }
}