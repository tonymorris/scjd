package suncertify.db.server.ui;

import java.util.Properties;

/**
 * Provides an interface for manipulating configuration properties of the data server.
 *
 * @see ServerConfigurationImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface ServerConfiguration
{
    /**
     * Returns the dataFilename configuration property.
     *
     * @return The dataFilename configuration property.
     */
    public String getDataFilename();

    /**
     * Sets the dataFilename configuration property.
     *
     * @param dataFilename The new value of the dataFilename configuration property.
     */
    public void setDataFilename(String dataFilename);

    /**
     * Returns the dbJndiName configuration property.
     *
     * @return The dbJndiName configuration property.
     */
    public String getDbJndiName();

    /**
     * Sets the dbJndiName configuration property.
     *
     * @param dbJndiName The new value of the dbJndiName configuration property.
     */
    public void setDbJndiName(String dbJndiName);

    /**
     * Returns the port configuration property.
     *
     * @return The port configuration property.
     */
    public int getPort();

    /**
     * Sets the port configuration property.
     *
     * @param port The new value of the port configuration property.
     */
    public void setPort(int port);

    /**
     * Returns the confirmServerStop configuration property.
     *
     * @return The confirmServerStop configuration property.
     */
    public boolean isConfirmServerStop();

    /**
     * Sets the confirmServerStop configuration property.
     *
     * @param confirmServerStop The new value of the confirmServerStop configuration property.
     */
    public void setConfirmServerStop(boolean confirmServerStop);

    /**
     * Converts this set of configuration properties into a <code>Properties</code> object
     * that is suitable for writing to file.
     *
     * @return A <code>Properties</code> object representing this set of configuration properties
     * that is suitable for writing to file.
     */
    public Properties toProperties();
}
