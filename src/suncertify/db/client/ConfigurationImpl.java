package suncertify.db.client;

import suncertify.db.client.Configuration;
import suncertify.db.client.ClientConstants;
import suncertify.db.client.SchemaColumn;
import suncertify.db.ConfigurationException;
import suncertify.db.client.SchemaColumnImpl;

import java.util.Properties;
import java.rmi.registry.Registry;

/**
 * Represents a configuration implementation of the client application.
 * Provides public accessor methods to each configuration attribute.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class ConfigurationImpl implements Configuration, ClientConstants
{
    private SchemaColumn[] metaSchema = new SchemaColumn[]
    {
        new SchemaColumnImpl(true, "Name", (short)32),
        new SchemaColumnImpl(true, "Location", (short)64),
        new SchemaColumnImpl(false, "Specialties", (short)64),
        new SchemaColumnImpl(false, "Number of Staff", (short)6),
        new SchemaColumnImpl(false, "Rate", (short)8),
        new SchemaColumnImpl(false, "Owner", (short)8),
    };

    private String host = "localhost";
    private int clientPort = Registry.REGISTRY_PORT;
    private String clientDbJndiName = "Data";
    private String clientDataFilename;
    private boolean confirmCut = true;
    private boolean confirmDelete = true;
    private boolean confirmExit = true;

    /**
     * Construct a <code>ConfigurationImpl</code> with default values.
     */
    public ConfigurationImpl()
    {

    }

    /**
     * Construct a <code>ConfigurationImpl</code> with values derived from the given properties.
     * Property values are defined in the {@link suncertify.db.client.ClientConstants ClientConstants} interface.
     *
     * @see suncertify.db.client.ClientConstants
     * @param props The properties to construct the <code>ConfigurationImpl</code> from.
     * @throws suncertify.db.ConfigurationException If the given properties contain invalid values.
     */
    public ConfigurationImpl(Properties props) throws ConfigurationException
    {
        try
        {
            if(props.getProperty(PROP_SCHEMA_TOTAL_COLUMNS) != null)
            {
                int columns = Integer.parseInt(props.getProperty(PROP_SCHEMA_TOTAL_COLUMNS));

                metaSchema = new SchemaColumn[columns];

                for(int i = 0; i < columns; i++)
                {
                    StringBuffer key = new StringBuffer();
                    key.append(PROP_SCHEMA_KEY_);
                    key.append(i);

                    StringBuffer displayName = new StringBuffer();
                    displayName.append(PROP_SCHEMA_DISPLAY_NAME_);
                    displayName.append(i);

                    StringBuffer length = new StringBuffer();
                    length.append(PROP_SCHEMA_LENGTH_);
                    length.append(i);

                    metaSchema[i] = new SchemaColumnImpl(new Boolean(props.getProperty(key.toString())).booleanValue(), props.getProperty(displayName.toString()), Short.parseShort(props.getProperty(length.toString())));
                }
            }

            if(props.getProperty(PROP_HOST_NAME) != null)
            {
                host = props.getProperty(PROP_HOST_NAME);
            }

            if(props.getProperty(PROP_CLIENT_HOST_PORT) != null)
            {
                clientPort = Integer.parseInt(props.getProperty(PROP_CLIENT_HOST_PORT));
            }

            if(props.getProperty(PROP_CLIENT_DB_JNDI_NAME) != null)
            {
                clientDbJndiName = props.getProperty(PROP_CLIENT_DB_JNDI_NAME);
            }

            if(props.getProperty(PROP_CLIENT_DATA_FILE_NAME) != null)
            {
                clientDataFilename = props.getProperty(PROP_CLIENT_DATA_FILE_NAME);
            }

            if(props.getProperty(PROP_CONFIRM_CUT) != null)
            {
                confirmCut = new Boolean(props.getProperty(PROP_CONFIRM_CUT)).booleanValue();
            }

            if(props.getProperty(PROP_CONFIRM_DELETE) != null)
            {
                confirmDelete = new Boolean(props.getProperty(PROP_CONFIRM_DELETE)).booleanValue();
            }

            if(props.getProperty(PROP_CONFIRM_EXIT) != null)
            {
                confirmExit = new Boolean(props.getProperty(PROP_CONFIRM_EXIT)).booleanValue();
            }
        }
        catch(NumberFormatException nfe)
        {
            throw new ConfigurationException(nfe.toString());
        }
    }

    /**
     * Returns the metaSchema configuration property.
     *
     * @return The metaSchema configuration property.
     */
    public SchemaColumn[] getMetaSchema()
    {
        return metaSchema;
    }

    /**
     * Sets the metaSchema configuration property.
     *
     * @param metaSchema The new value of the metaSchema configuration property.
     */
    public void setMetaSchema(SchemaColumn[] metaSchema)
    {
        this.metaSchema = metaSchema;
    }

    /**
     * Returns the host configuration property.
     *
     * @return The host configuration property.
     */
    public String getHost()
    {
        return host;
    }

    /**
     * Sets the host configuration property.
     *
     * @param host The new value of the host configuration property.
     */
    public void setHost(String host)
    {
        this.host = host;
    }

    /**
     * Returns the clientPort configuration property.
     *
     * @return The clientPort configuration property.
     */
    public int getClientPort()
    {
        return clientPort;
    }

    /**
     * Sets the clientPort configuration property.
     *
     * @param clientPort The new value of the clientPort configuration property.
     */
    public void setClientPort(int clientPort)
    {
        this.clientPort = clientPort;
    }

    /**
     * Returns the clientDbJndiName configuration property.
     *
     * @return The clientDbJndiName configuration property.
     */
    public String getClientDbJndiName()
    {
        return clientDbJndiName;
    }

    /**
     * Sets the clientDbJndiName configuration property.
     *
     * @param clientDbJndiName The new value of the clientDbJndiName configuration property.
     */
    public void setClientDbJndiName(String clientDbJndiName)
    {
        this.clientDbJndiName = clientDbJndiName;
    }

    /**
     * Returns the clientDataFilename configuration property.
     *
     * @return The clientDataFilename configuration property.
     */
    public String getClientDataFilename()
    {
        return clientDataFilename;
    }

    /**
     * Sets the clientDataFilename configuration property.
     *
     * @param clientDataFilename The new value of the clientDataFilename configuration property.
     */
    public void setClientDataFilename(String clientDataFilename)
    {
        this.clientDataFilename = clientDataFilename;
    }

    /**
     * Returns the confirmCut configuration property.
     *
     * @return The confirmCut configuration property.
     */
    public boolean isConfirmCut()
    {
        return confirmCut;
    }

    /**
     * Sets the confirmCut configuration property.
     *
     * @param confirmCut The new value of the confirmCut configuration property.
     */
    public void setConfirmCut(boolean confirmCut)
    {
        this.confirmCut = confirmCut;
    }

    /**
     * Returns the confirmDelete configuration property.
     *
     * @return The confirmDelete configuration property.
     */
    public boolean isConfirmDelete()
    {
        return confirmDelete;
    }

    /**
     * Sets the confirmDelete configuration property.
     *
     * @param confirmDelete The new value of the confirmDelete configuration property.
     */
    public void setConfirmDelete(boolean confirmDelete)
    {
        this.confirmDelete = confirmDelete;
    }

    /**
     * Returns the confirmExit configuration property.
     *
     * @return The confirmExit configuration property.
     */
    public boolean isConfirmExit()
    {
        return confirmExit;
    }

    /**
     * Sets the confirmExit configuration property.
     *
     * @param confirmExit The new value of the confirmExit configuration property.
     */
    public void setConfirmExit(boolean confirmExit)
    {
        this.confirmExit = confirmExit;
    }

    /**
     * Converts this <code>ConfigurationImpl</code> to a set of <code>Properties</code> suitable for
     * writing to file so that configuration can be persisted each time the application starts.
     *
     * @return A set of <code>Properties</code> suitable for
     * writing to file so that configuration can be persisted each time the application starts.
     */
    public Properties toProperties()
    {
        Properties props = new Properties();

        if(metaSchema != null)
        {
            props.setProperty(PROP_SCHEMA_TOTAL_COLUMNS, String.valueOf(metaSchema.length));

            for(int i = 0; i < metaSchema.length; i++)
            {
                StringBuffer key = new StringBuffer();
                key.append(PROP_SCHEMA_KEY_);
                key.append(i);

                StringBuffer displayName = new StringBuffer();
                displayName.append(PROP_SCHEMA_DISPLAY_NAME_);
                displayName.append(i);

                StringBuffer length = new StringBuffer();
                length.append(PROP_SCHEMA_LENGTH_);
                length.append(i);

                props.setProperty(key.toString(), String.valueOf(metaSchema[i].isKey()));
                props.setProperty(displayName.toString(), metaSchema[i].getDisplayName());
                props.setProperty(length.toString(), String.valueOf(metaSchema[i].getLength()));
            }
        }

        if(host != null && host.length() > 0)
        {
            props.setProperty(PROP_HOST_NAME, host);
        }

        props.setProperty(PROP_CLIENT_HOST_PORT, String.valueOf(clientPort));

        if(clientDbJndiName != null && clientDbJndiName.length() > 0)
        {
            props.setProperty(PROP_CLIENT_DB_JNDI_NAME, clientDbJndiName);
        }

        if(clientDataFilename != null && clientDataFilename.length() > 0)
        {
            props.setProperty(PROP_CLIENT_DATA_FILE_NAME, clientDataFilename);
        }

        props.setProperty(PROP_CONFIRM_CUT, String.valueOf(confirmCut));
        props.setProperty(PROP_CONFIRM_DELETE, String.valueOf(confirmDelete));
        props.setProperty(PROP_CONFIRM_EXIT, String.valueOf(confirmExit));

        return props;
    }
}
