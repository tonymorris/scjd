package suncertify.db.client;

import suncertify.db.client.SchemaColumn;

import java.util.Properties;

/**
 * Provides a programming interface to an object that represents the configuration of the client application.
 *
 * @see suncertify.db.client.ConfigurationImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface Configuration
{
    /**
     * Returns the metaSchema configuration property.
     *
     * @return The metaSchema configuration property.
     */
    public SchemaColumn[] getMetaSchema();

    /**
     * Sets the metaSchema configuration property.
     *
     * @param metaSchema The new value of the metaSchema configuration property.
     */
    public void setMetaSchema(SchemaColumn[] metaSchema);

    /**
     * Returns the host configuration property.
     *
     * @return The host configuration property.
     */
    public String getHost();

    /**
     * Sets the host configuration property.
     *
     * @param host The new value of the host configuration property.
     */
    public void setHost(String host);

    /**
     * Returns the clientPort configuration property.
     *
     * @return The clientPort configuration property.
     */
    public int getClientPort();

    /**
     * Sets the clientPort configuration property.
     *
     * @param clientPort The new value of the clientPort configuration property.
     */
    public void setClientPort(int clientPort);

    /**
     * Returns the clientDbJndiName configuration property.
     *
     * @return The clientDbJndiName configuration property.
     */
    public String getClientDbJndiName();

    /**
     * Sets the clientDbJndiName configuration property.
     *
     * @param clientDbJndiName The new value of the clientDbJndiName configuration property.
     */
    public void setClientDbJndiName(String clientDbJndiName);

    /**
     * Returns the clientDataFilename configuration property.
     *
     * @return The clientDataFilename configuration property.
     */
    public String getClientDataFilename();

    /**
     * Sets the clientDataFilename configuration property.
     *
     * @param clientDataFilename The new value of the clientDataFilename configuration property.
     */
    public void setClientDataFilename(String clientDataFilename);

    /**
     * Returns the confirmCut configuration property.
     *
     * @return The confirmCut configuration property.
     */
    public boolean isConfirmCut();

    /**
     * Sets the confirmCut configuration property.
     *
     * @param confirmCut The new value of the confirmCut configuration property.
     */
    public void setConfirmCut(boolean confirmCut);

    /**
     * Returns the confirmDelete configuration property.
     *
     * @return The confirmDelete configuration property.
     */
    public boolean isConfirmDelete();

    /**
     * Sets the confirmDelete configuration property.
     *
     * @param confirmDelete The new value of the confirmDelete configuration property.
     */
    public void setConfirmDelete(boolean confirmDelete);

    /**
     * Returns the confirmExit configuration property.
     *
     * @return The confirmExit configuration property.
     */
    public boolean isConfirmExit();

    /**
     * Sets the confirmExit configuration property.
     *
     * @param confirmExit The new value of the confirmExit configuration property.
     */
    public void setConfirmExit(boolean confirmExit);

    /**
     * Converts this <code>Configuration</code> to a set of <code>Properties</code> suitable for
     * writing to file so that configuration can be persisted each time the application starts.
     *
     * @return A set of <code>Properties</code> suitable for
     * writing to file so that configuration can be persisted each time the application starts.
     */
    public Properties toProperties();
}
