package suncertify.db.client;

/**
 * Represents a schema of a column of the data that the application is configured to use.
 * Each column may (or may not) be a unique key, contains a display name and has a length in bytes.
 * These attributes are represented as a <code>boolean</code>, a <code>String</code>, and a <code>short</code> respectively.
 *
 * @see SchemaColumnImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface SchemaColumn
{
    /**
     * Determines if this column schema is a unique key.
     *
     * @return <code>true</code> if this column schema is a unique key, <code>false</code> otherwise.
     */
    public boolean isKey();

    /**
     * Sets whether or not this column schema is a unique key.
     *
     * @param key Set to <code>true</code> if this column schema is a unique key, <code>false</code> otherwise.
     */
    public void setKey(boolean key);

    /**
     * Returns the display name of this column schema.
     *
     * @return The display name of this column schema.
     */
    public String getDisplayName();

    /**
     * Sets the display name of this column schema.
     *
     * @param displayName The new display name of this column schema.
     */
    public void setDisplayName(String displayName);

    /**
     * Returns the length, in bytes, of this column schema.
     *
     * @return The length, in bytes, of this column schema.
     */
    public short getLength();

    /**
     * Sets the length, in bytes, of this column schema.
     *
     * @param length The new value of the length, in bytes, of this column schema.
     */
    public void setLength(short length);
}
