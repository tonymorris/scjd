package suncertify.db;

/**
 * A checked exception that is thrown if an attempt is made to have two data records with the same unique key.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class DuplicateKeyException extends Exception
{
    /**
     * Construct a <code>DuplicateKeyException</code> with <code>null</code> as its detailed error message.
     */
    public DuplicateKeyException()
    {
        super();
    }

    /**
     * Construct a <code>DuplicateKeyException</code> with the given detailed error message.
     *
     * @param message The detailed error message of the exception.
     */
    public DuplicateKeyException(String message)
    {
        super(message);
    }
}
