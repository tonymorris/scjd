package suncertify.db;

/**
 * A checked exception that is thrown if the configuration file contains invalid data.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class ConfigurationException extends Exception
{
    /**
     * Construct a <code>ConfigurationException</code> with <code>null</code> as its detailed error message.
     */
    public ConfigurationException()
    {
        super();
    }

    /**
     * Construct a <code>ConfigurationException</code> with the given detailed error message.
     *
     * @param message The detailed error message of the exception.
     */
    public ConfigurationException(String message)
    {
        super(message);
    }
}
