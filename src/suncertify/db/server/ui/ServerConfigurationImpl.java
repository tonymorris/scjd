package suncertify.db.server.ui;

import suncertify.db.ConfigurationException;

import java.util.Properties;
import java.rmi.registry.Registry;

/**
 * Represents a configuration implementation for the data server.
 * Provides public accessor methods to each configuration attribute.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class ServerConfigurationImpl implements ServerConfiguration, ServerConfigurationConstants
{
    private String dataFilename;
    private String dbJndiName = "Data";
    private String hostname = "localhost";
    private int port = Registry.REGISTRY_PORT;
    private boolean confirmServerStop = true;

    /**
     * Construct a </code>ServerConfigurationImpl</code> with a default set of properties.
     */
    public ServerConfigurationImpl()
    {

    }

    /**
     * Construct a <code>ServerConfigurationImpl</code> with values derived from the given properties.
     * Property values are defined in the {@link ServerConfigurationConstants ServerConfigurationConstants} interface.
     *
     * @see ServerConfigurationConstants
     * @param props The properties to construct the <code>ServerConfigurationImpl</code> from.
     * @throws suncertify.db.ConfigurationException If the given properties contain invalid values.
     */
    public ServerConfigurationImpl(Properties props) throws ConfigurationException
    {
        try
        {
            if(props.getProperty(PROP_SERVER_DATA_FILE_NAME) != null)
            {
                dataFilename = props.getProperty(PROP_SERVER_DATA_FILE_NAME);
            }

            if(props.getProperty(PROP_SERVER_DB_JNDI_NAME) != null)
            {
                dbJndiName = props.getProperty(PROP_SERVER_DB_JNDI_NAME);
            }

            if(props.getProperty(PROP_SERVER_HOST_PORT) != null)
            {
                port = Integer.parseInt(props.getProperty(PROP_SERVER_HOST_PORT));
            }

            if(props.getProperty(PROP_SERVER_CONFIRM_SERVER_STOP) != null)
            {
                confirmServerStop = new Boolean(props.getProperty(PROP_SERVER_CONFIRM_SERVER_STOP)).booleanValue();
            }
        }
        catch(NumberFormatException nfe)
        {
            throw new ConfigurationException(nfe.toString());
        }
    }

    /**
     * Returns the dataFilename configuration property.
     *
     * @return The dataFilename configuration property.
     */
    public String getDataFilename()
    {
        return dataFilename;
    }

    /**
     * Sets the dataFilename configuration property.
     *
     * @param dataFilename The new value of the dataFilename configuration property.
     */
    public void setDataFilename(String dataFilename)
    {
        this.dataFilename = dataFilename;
    }

    /**
     * Returns the dbJndiName configuration property.
     *
     * @return The dbJndiName configuration property.
     */
    public String getDbJndiName()
    {
        return dbJndiName;
    }

    /**
     * Sets the dbJndiName configuration property.
     *
     * @param dbJndiName The new value of the dbJndiName configuration property.
     */
    public void setDbJndiName(String dbJndiName)
    {
        this.dbJndiName = dbJndiName;
    }

    /**
     * Returns the hostname configuration property.
     *
     * @return The hostname configuration property.
     */
    public String getHostname()
    {
        return hostname;
    }

    /**
     * Sets the hostname configuration property.
     *
     * @param hostname The new value of the hostname configuration property.
     */
    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    /**
     * Returns the port configuration property.
     *
     * @return The port configuration property.
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Sets the port configuration property.
     *
     * @param port The new value of the port configuration property.
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Returns the confirmServerStop configuration property.
     *
     * @return The confirmServerStop configuration property.
     */
    public boolean isConfirmServerStop()
    {
        return confirmServerStop;
    }

    /**
     * Sets the confirmServerStop configuration property.
     *
     * @param confirmServerStop The new value of the confirmServerStop configuration property.
     */
    public void setConfirmServerStop(boolean confirmServerStop)
    {
        this.confirmServerStop = confirmServerStop;
    }

    /**
     * Converts this set of configuration properties into a <code>Properties</code> object
     * that is suitable for writing to file.
     *
     * @return A <code>Properties</code> object representing this set of configuration properties
     * that is suitable for writing to file.
     */
    public Properties toProperties()
    {
        Properties props = new Properties();

        if(dataFilename != null && dataFilename.length() > 0)
        {
            props.setProperty(PROP_SERVER_DATA_FILE_NAME, dataFilename);
        }

        if(dbJndiName != null && dbJndiName.length() > 0)
        {
            props.setProperty(PROP_SERVER_DB_JNDI_NAME, dbJndiName);
        }

        props.setProperty(PROP_SERVER_HOST_PORT, String.valueOf(port));
        props.setProperty(PROP_SERVER_CONFIRM_SERVER_STOP, String.valueOf(confirmServerStop));

        return props;
    }
}
