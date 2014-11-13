package suncertify.db;

/**
 * Defines a set of constant values that are common across the entire application.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface Constants
{
    /**
     * The name of the configuration file for the client application.
     */
    public final static String PROPERTIES_FILE = "suncertify.properties";

    /**
     * The name of the configuration file for the client application.
     */
    public final static String PROPERTIES_FILE_HEADER = "Sun Certified Developer for the Java 2 Platform - Configuration File";

    /**
     * The command line argument to pass to start the data server.
     */
    public final static String CLA_SERVER = "server";

    /**
     * The command line argument to pass to start the client interface in "Local Mode"
     * (as opposed to networked mode).
     */
    public final static String CLA_ALONE = "alone";
}
