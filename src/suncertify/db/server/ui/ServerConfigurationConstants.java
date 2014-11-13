package suncertify.db.server.ui;

/**
 * Defines a set of constants (property keys) for configuration attributes of the data server.
 *
 * @see ServerConfiguration
 * @see ServerConfigurationImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface ServerConfigurationConstants
{
    /**
     * The configuration property on the server that represents the data file name to use.
     */
    public final static String PROP_SERVER_DATA_FILE_NAME = "server.data.file.name";

    /**
     * The configuration property on the server that represents the JNDI name of the remote DB object to export.
     *
     * @see suncertify.db.DB
     */
    public final static String PROP_SERVER_DB_JNDI_NAME = "server.db.jndi.name";

    /**
     * The configuration property on the server that represents the host port to export the remote DB object on.
     */
    public final static String PROP_SERVER_HOST_PORT = "server.host.port";

    /**
     * The configuration property that determines whether or not to confirm the stopping of the server.
     */
    public final static String PROP_SERVER_CONFIRM_SERVER_STOP = "server.confirm.stop";
}
