package suncertify.db.datafile;

/**
 * Represents a description (schema) of a database field (the most granular representation of data).
 * A database field description is composed of a name property and the length of the data that is will hold (in bytes).
 *
 * @see FieldSchemaImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface FieldSchema
{
    /**
     * Returns the name property of the field schema.
     *
     * @return The name property of the field schema.
     */
    public String getName();

    /**
     * Sets the name property of the field schema.
     *
     * @param name The name property of the field schema.
     */
    public void setName(String name);

    /**
     * Returns the length property of the field schema.
     *
     * @return The length property of the field schema.
     */
    public short getLength();

    /**
     * Sets the length property of the field schema.
     *
     * @param length The length property of the field schema.
     */
    public void setLength(short length);
}
