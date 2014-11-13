package suncertify.db.client;

/**
 * Defines a set of constant values that are common across the client application.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface ClientConstants
{
    /**
     * The configuration property that represents the number of columns in the data schema.
     */
    public final static String PROP_SCHEMA_TOTAL_COLUMNS = "schema.total.columns";

    /**
     * The prefix of the configuration property that represents whether or not a schema column is a unique key.
     */
    public final static String PROP_SCHEMA_KEY_ = "schema.key.";

    /**
     * The prefix of the configuration property that represents the schema column display name.
     */
    public final static String PROP_SCHEMA_DISPLAY_NAME_ = "schema.display.name.";

    /**
     * The prefix of the configuration property that represents the schema column length in bytes.
     */
    public final static String PROP_SCHEMA_LENGTH_ = "schema.length.";

    /**
     * The configuration property that represents the host name to connect to when the application is in networked mode.
     * Defaults to "localhost".
     */
    public final static String PROP_HOST_NAME = "host.name";

    /**
     * The configuration property on the client that represents the host port to connect to when the application is in networked mode.
     * Defaults to "1099".
     */
    public final static String PROP_CLIENT_HOST_PORT = "client.host.port";

    /**
     * The configuration property on the client that represents the JNDI name of the remote DB object to connect to when the application is in networked mode.
     *
     * @see suncertify.db.DB
     */
    public final static String PROP_CLIENT_DB_JNDI_NAME = "client.db.jndi.name";

    /**
     * The configuration property on the client that represents the data file name to use.
     */
    public final static String PROP_CLIENT_DATA_FILE_NAME = "client.data.file.name";

    /**
     * The configuration property that determines whether or not to confirm the action when a user attempts to 'Cut' a data record.
     *
     * @see suncertify.db.client.actions.Cut
     */
    public final static String PROP_CONFIRM_CUT = "confirm.cut";

    /**
     * The configuration property that determines whether or not to confirm the action when a user attempts to 'Delete' a data record.
     *
     * @see suncertify.db.client.actions.DeleteRecord
     */
    public final static String PROP_CONFIRM_DELETE = "confirm.delete";

    /**
     * The configuration property that determines whether or not to confirm the action when a user attempts to 'Exit' the application.
     *
     * @see suncertify.db.client.actions.Exit
     */
    public final static String PROP_CONFIRM_EXIT = "confirm.exit";
}
