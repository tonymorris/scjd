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
public class DuplicateKeyDetectorImpl implements DuplicateKeyDetector
{
    private DataFrame owner;

    /**
     * Constructs a <code>DuplicateKeyDetectorImpl</code> with the given application frame owner.
     * The owner may be queries for configuration information or its data model.
     *
     * @param owner The owner that constructed this <code>DuplicateKeyDetectorImpl</code>
     */
    public DuplicateKeyDetectorImpl(DataFrame owner)
    {
        this.owner = owner;
    }

    /**
     * Determines if the given record data contains a duplicate key according to the application configuration
     * and all records in the data model. Iterates each data record in the owner data model, and calls
     * {@link #isDuplicateKey(String[], String[]) isDuplicateKey{String[], String[]} with each data model record.
     *
     * @param data The data that may contain a duplicate key.
     * @return <code>true</code> if the record data contains a duplicate key according to the application configuration
     * and all records in the data model, <code>false</code> otherwise.
     */
    public boolean isDuplicateKey(String[] data)
    {
        for(int i = 0; i < owner.getDataTableModel().getRowCount(); i++)
        {
            String[] row = owner.getDataTableModel().getDataRecord(i).getData();

            if(isDuplicateKey(data, row))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given record data contains a duplicate key according to the application configuration
     * and the given record.
     *
     * @param data The data that may contain a duplicate key.
     * @param row The data record to check to have a duplicate key against.
     * @return <code>true</code> if the record data contains a duplicate key according to the application configuration
     * and the given record, <code>false</code> otherwise.
     */
    public boolean isDuplicateKey(String[] data, String[] row)
    {
        if(data.length != row.length)
        {
            return false;
        }

        boolean hasKey = false;

        for(int i = 0; i < owner.getConfiguration().getMetaSchema().length; i++)
        {
            if(owner.getConfiguration().getMetaSchema()[i].isKey())
            {
                hasKey = true;
            }
        }

        if(!hasKey)
        {
            return false;
        }

        for(int i = 0; i < owner.getConfiguration().getMetaSchema().length; i++)
        {
            SchemaColumn sc = owner.getConfiguration().getMetaSchema()[i];

            if(sc.isKey() && !data[i].equals(row[i]))
            {
                return false;
            }
        }

        return true;
    }
}
