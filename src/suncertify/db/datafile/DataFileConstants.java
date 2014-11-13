package suncertify.db.datafile;

/**
 * Defines a set of constant values that are related to the data file.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface DataFileConstants
{
    /**
     * The 2 byte flag value of a record that indicates that it is valid (not deleted).
     */
    public static final char VALID_RECORD = 0x0000;

    /**
     * The 2 byte flag value of a record that indicates that it has been deleted.
     */
    public static final char DELETED_RECORD = 0x8000;

    /**
     * The <code>byte</code> value to pad the remainder of data record data with.
     */
    public static final byte RECORD_PADDING = 0x0020;
}
