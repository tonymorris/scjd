package suncertify.db;

/**
 * A checked exception that is thrown if an attempt is made to access a record (either read or write)
 * that does not exist.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class RecordNotFoundException extends Exception
{
    /**
     * Construct a <code>RecordNotFoundException</code> with <code>null</code> as its detailed error message.
     */
    public RecordNotFoundException()
    {
        super();
    }

    /**
     * Construct a <code>RecordNotFoundException</code> with the given detailed error message.
     *
     * @param message The detailed error message of the exception.
     */
    public RecordNotFoundException(String message)
    {
        super(message);
    }
}
