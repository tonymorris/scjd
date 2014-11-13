package suncertify.db.client;

/**
 * Determines one ore more {@link suncertify.db.DataRecord DataRecord}s contains data that is a duplicate key as indicated in the
 * application configuration. This method of configuring the data schema provides a loose coupling between the data representation
 * and the client/server application itself without exceeding the requirements. A typical solution may determine
 * the data record keys from a remote API call to the server, however, this exceeds the given requirements of the application.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface DuplicateKeyDetector
{
    /**
     * Determines if the given record data contains a duplicate key according to the application configuration
     * and all records in the data model.
     *
     * @param data The data that may contain a duplicate key.
     * @return <code>true</code> if the record data contains a duplicate key according to the application configuration
     * and all records in the data model, <code>false</code> otherwise.
     */
    public boolean isDuplicateKey(String[] data);

    /**
     * Determines if the given record data contains a duplicate key according to the application configuration
     * and the given record.
     *
     * @param data The data that may contain a duplicate key.
     * @param row The data record to check to have a duplicate key against.
     * @return <code>true</code> if the record data contains a duplicate key according to the application configuration
     * and the given record, <code>false</code> otherwise.
     */
    public boolean isDuplicateKey(String[] data, String[] row);
}
