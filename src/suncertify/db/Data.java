package suncertify.db;

import suncertify.db.server.RecordLockManager;
import suncertify.db.server.RecordMatcherFactory;
import suncertify.db.server.RecordLockManagerImpl;
import suncertify.db.server.RecordMatcherFactoryImpl;
import suncertify.db.server.RecordMatcher;
import suncertify.db.datafile.DataFileFactory;
import suncertify.db.datafile.DataFileFactoryImpl;
import suncertify.db.datafile.DataFile;
import suncertify.db.datafile.DataFileHeader;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;
import java.util.LinkedList;

/**
 * This class is used as the exposed remote API to clients.
 * The public methods of this class allow operations on the back-end data by remotely (RMI) connected clients.
 * Such operations include adding, editing, deleting and searching data records.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Data implements DB
{
    private static final int[] KEY_INDICIES = new int[]{0, 1};

    private RecordLockManager lockManager;
    private DataFileFactory datafileFactory;
    private RecordMatcherFactory recordMatcherFactory;

    /**
     * Construct a <code>Data</code> object that allows manipulation of the back-end data
     * in the given data file name. It is assumed that the given data file already exists and contains
     * data header information at the time this constructor is called.
     *
     * @param dataFilename The name of the file containing the data to provide the interface to.
     * @throws FileNotFoundException If the data file does not exist, is a directory, cannot be read or cannot be written to.
     */
    public Data(String dataFilename) throws FileNotFoundException
    {
        verifyDataFilename(dataFilename);

        lockManager = new RecordLockManagerImpl();
        datafileFactory = new DataFileFactoryImpl(dataFilename);
        recordMatcherFactory = new RecordMatcherFactoryImpl();
    }

    /**
     * Reads the given record number and returns the data as a <code>String</code> array representing the data in each field.
     * Records are indexed from 0 (zero).
     *
     * @param recNo The data record number to read.
     * @return The record that was read, or <code>null</code> if the record has been deleted.
     * @throws RecordNotFoundException If the given record number does not exist.
     * @throws IllegalStateException If the data file cannot be opened or closed for read.
     */
    public String[] read(int recNo) throws RecordNotFoundException, IllegalStateException
    {
        verifyValidRecord(recNo);

        DataFile file = null;

        try
        {
            file = datafileFactory.createDataFile("r");

            file.seekRecord(recNo);

            DataRecord rec = file.nextRecord();

            if(rec.isDeleted())
            {
                return null;
            }

            return rec.getData();
        }
        catch(IOException ioe)
        {
            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for read: ");
            message.append(ioe);

            throw new IllegalStateException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new IllegalStateException(message.toString());
                }
            }
        }
    }

    /**
     * Updates the given record number with the given <code>String[]</code> data.
     * The requesting client must hold the exclusive write lock on the record to successfully execute this method.
     * The data indicies that are the unique key are not updated with the new value. It is not possible to update a record with new key value(s).
     * Records are indexed from 0 (zero).
     *
     * @param recNo The data record number to update with the given <code>String[]</code> data.
     * @param data The data to update the given record with.
     * @param lockCookie The cookie value to authenticate the requesting client as holding the exclusive write lock on the data record.
     * @throws RecordNotFoundException If the given record number does not exist.
     * @throws SecurityException If the given cookie value does not authenticate the requesting client as holding the exclusive write lock for the data record.
     * @throws IllegalStateException If the data file cannot be opened or closed for read/write or if the given data
     *      is not consistent with the data source schema.
     */
    public void update(int recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException, IllegalStateException
    {
        verifyValidRecord(recNo);
        verifyNotDeletedRecord(recNo);
        verifyValidCookie(recNo, lockCookie);
        verifyValidData(data);

        DataFile file = null;

        try
        {
            file = datafileFactory.createDataFile("rw");

            file.seekRecord(recNo);

            DataRecord rec = file.nextRecord();

            for(int i = 0; i < KEY_INDICIES.length; i++)
            {
               data[KEY_INDICIES[i]] = rec.getData()[KEY_INDICIES[i]];
            }

            rec.setData(data);

            file.seekRecord(recNo);

            file.writeRecord(rec);
        }
        catch(IOException ioe)
        {
            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for write: ");
            message.append(ioe);

            throw new IllegalStateException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new IllegalStateException(message.toString());
                }
            }
        }
    }

    /**
     * Deletes the given record number.
     * The requesting client must hold the exclusive write lock on the record to successfully execute this method.
     * Records are indexed from 0 (zero).
     *
     * @param recNo The data record number to delete.
     * @param lockCookie The cookie value to authenticate the requesting client as holding the exclusive write lock on the data record.
     * @throws RecordNotFoundException If the given record number does not exist.
     * @throws SecurityException If the given cookie value does not authenticate the requesting client as holding the exclusive write lock for the data record.
     * @throws IllegalStateException If the data file cannot be opened or closed for read/write.
     */
    public void delete(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException, IllegalStateException
    {
        verifyValidRecord(recNo);
        verifyNotDeletedRecord(recNo);
        verifyValidCookie(recNo, lockCookie);

        DataFile file = null;

        try
        {
            file = datafileFactory.createDataFile("rw");

            file.seekRecord(recNo);

            DataRecord rec = file.nextRecord();

            rec.setDeleted(true);

            file.seekRecord(recNo);

            file.writeRecord(rec);
        }
        catch(IOException ioe)
        {
            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for read: ");
            message.append(ioe);

            throw new IllegalStateException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new IllegalStateException(message.toString());
                }
            }
        }
    }

    /**
     * Search the data records with the given criteria. The indicies of matching records are returned to the
     * requesting client. A matching data record is determined by the
     * {@link suncertify.db.server.RecordMatcherImpl RecordMatcherImpl} implementation.
     * Records are indexed from 0 (zero).
     *
     * @see suncertify.db.server.RecordMatcherImpl
     * @param criteria The search criteria to match data records with.
     * @return An array of indicies of records that match the search criteria or <code>null</code> if the back-end data file cannot be opened or closed for read.
     * @throws IllegalStateException If the data file cannot be opened or closed for read.
     */
    public int[] find(String[] criteria) throws IllegalStateException
    {
        DataFile file = null;

        try
        {
            file = datafileFactory.createDataFile("r");

            List matches = new LinkedList();

            int index = 0;
            RecordMatcher rm = recordMatcherFactory.createRecordMatcher();

            while(file.hasMoreRecords())
            {
                DataRecord rec = file.nextRecord();

                if(!rec.isDeleted() && rm.matches(rec, criteria))
                {
                    matches.add(new Integer(index));
                }

                index++;
            }

            // There is no real nice way of doing this.
            // This is the quickest method of creating a int[] from a List of Integer types according to my own benchmarks.
            // JDK 1.5 should solve this problem with generic types and autoboxing/unboxing.
            Integer[] asArray = (Integer[])matches.toArray(new Integer[0]);

            int[] retval = new int[asArray.length];

            for(int i = 0; i < retval.length; i++)
            {
                retval[i] = asArray[i].intValue();
            }

            return retval;
        }
        catch(IOException ioe)
        {
            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for read: ");
            message.append(ioe);

            throw new IllegalStateException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new IllegalStateException(message.toString());
                }
            }
        }
    }

    /**
     * Create a new data record. The new data record will be placed at the end of the data.
     * Records are indexed from 0 (zero).
     *
     * @param data The new data record.
     * @return The index of the new data record or a value less than 0 (zero) if an error occurred while creating the new data record.
     * @throws DuplicateKeyException If the new data record contains a unique key value that already exists.
     * @throws IllegalStateException If the data file cannot be opened or closed for read/write or if the given data
     *      is not consistent with the data source schema.
     */
    public int create(String[] data) throws DuplicateKeyException, IllegalStateException
    {
        verifyValidKey(data);
        verifyValidData(data);

        DataFile file = null;

        try
        {
            file = datafileFactory.createDataFile("rw");

            int length = (int)file.lengthInRecords();

            file.seekRecord(length);

            DataRecord record = new DataRecordImpl(data);

            file.writeRecord(record);

            return length;
        }
        catch(IOException ioe)
        {
            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for write: ");
            message.append(ioe);

            throw new IllegalStateException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new IllegalStateException(message.toString());
                }
            }
        }
    }

    /**
     * Attempt to acquire the exclusive write lock on the given data record.
     * Requesting clients may be put on "wait state" if the lock is in use.
     *
     * @see RecordLockManagerImpl#lock(int)
     * @param recNo The data record number to attempt to acquire the exclusive write lock on.
     * @return A cookie value to be used for calls to methods that write, and so require authentication of the owner of the write lock.
     * @throws RecordNotFoundException If the given record number does not exist, or if the back-end data file cannot be opened or closed for read.
     */
    public long lock(int recNo) throws RecordNotFoundException
    {
        verifyValidRecord(recNo);
        verifyNotDeletedRecord(recNo);

        return lockManager.lock(recNo);
    }

    /**
     * Release the exclusive write lock for the given record and notify a waiting client if there is one.
     *
     * @see RecordLockManagerImpl#unlock(int)
     * @param recNo The data record number to release the exclusive write lock for.
     * @param cookie The cookie value to authenticate the requesting client as holding the exclusive write lock on the data record.
     * @throws RecordNotFoundException If the given record number does not exist, or if the back-end data file cannot be opened or closed for read.
     * @throws SecurityException If the given cookie value does not authenticate the requesting client as holding the exclusive write lock for the data record.
     */
    public void unlock(int recNo, long cookie) throws RecordNotFoundException, SecurityException
    {
        verifyValidRecord(recNo);
        verifyValidCookie(recNo, cookie);

        lockManager.unlock(recNo);
    }

    // throws a FileNotFoundException if the data file does not exist, is a directory, cannot be read or cannot be written.
    private void verifyDataFilename(String dataFilename) throws FileNotFoundException
    {
        if(dataFilename == null || dataFilename.trim().length() == 0)
        {
            throw new FileNotFoundException("Data source file name not specified");
        }

        File f = new File(dataFilename);

        if(!f.exists())
        {
            StringBuffer message = new StringBuffer();
            message.append("Specified data file name does not exist: ");
            message.append(dataFilename);

            throw new FileNotFoundException(message.toString());
        }

        if(f.isDirectory())
        {
            StringBuffer message = new StringBuffer();
            message.append("Specified data file name is a directory: ");
            message.append(dataFilename);

            throw new FileNotFoundException(message.toString());
        }

        if(!f.canRead())
        {
            StringBuffer message = new StringBuffer();
            message.append("Specified data file name cannot be read: ");
            message.append(dataFilename);

            throw new FileNotFoundException(message.toString());
        }

        if(!f.canWrite())
        {
            StringBuffer message = new StringBuffer();
            message.append("Specified data file name cannot be written to: ");
            message.append(dataFilename);

            throw new FileNotFoundException(message.toString());
        }
    }

    // throws a RecordNotFoundException if the given record number does not exist or if an error occur determining it.
    private void verifyValidRecord(int recNo) throws RecordNotFoundException
    {
        DataFile file = null;

        try
        {
            file = datafileFactory.createDataFile("r");

            long length = file.lengthInRecords();

            if(recNo < 0)
            {
                StringBuffer message = new StringBuffer();
                message.append("Invalid DataRecord Number : [");
                message.append(recNo);
                message.append(" < 0]");

                throw new RecordNotFoundException(message.toString());
            }

            if(recNo >= length)
            {
                StringBuffer message = new StringBuffer();
                message.append("DataRecord Number Out of Range: [");
                message.append(recNo);
                message.append(" >= ");
                message.append(length);
                message.append("]");

                throw new RecordNotFoundException(message.toString());
            }
        }
        catch(IOException ioe)
        {
            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for read: ");
            message.append(ioe);

            throw new RecordNotFoundException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new RecordNotFoundException(message.toString());
                }
            }
        }
    }

    // throws a RecordNotFoundException if the given data record has been deleted or if an error occurred determining it.
    private void verifyNotDeletedRecord(int recNo) throws RecordNotFoundException
    {
        DataFile file = null;

        try
        {
            file = datafileFactory.createDataFile("r");

            file.seekRecord(recNo);

            DataRecord rec = file.nextRecord();

            if(rec.isDeleted())
            {
                StringBuffer message = new StringBuffer();
                message.append("DataRecord has been deleted: ");
                message.append(recNo);

                throw new RecordNotFoundException(message.toString());
            }
        }
        catch(IOException ioe)
        {
            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for read: ");
            message.append(ioe);

            throw new RecordNotFoundException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new RecordNotFoundException(message.toString());
                }
            }
        }
    }

    // throws a SecurityException if the given data record number cookie value does not authenticate with the cookie value.
    private void verifyValidCookie(int recNo, long cookie) throws SecurityException
    {
        if(!lockManager.isValidCookie(recNo, cookie))
        {
            StringBuffer message = new StringBuffer();
            message.append("Failed authentication to unlock record: ");
            message.append(recNo);
            message.append(" with cookie lock: ");
            message.append(cookie);

            throw new SecurityException(message.toString());
        }
    }

    // throws a DuplicateKeyException if the given data contains a unique key value that already exists or if an error occurs determining it.
    private void verifyValidKey(String[] data) throws DuplicateKeyException
    {
        DataFile file = null;

        for(int i = 0; i < KEY_INDICIES.length; i++)
        {
            if(data[KEY_INDICIES[i]] == null)
            {
                throw new DuplicateKeyException("Cannot create record with null key");
            }
        }

        try
        {
            file = datafileFactory.createDataFile("r");

            while(file.hasMoreRecords())
            {
                DataRecord rec = file.nextRecord();

                if(!rec.isDeleted())
                {
                    boolean duplicate = true;

                    for(int i = 0; i < KEY_INDICIES.length; i++)
                    {
                        if(!data[KEY_INDICIES[i]].trim().equals(rec.getData()[KEY_INDICIES[i]].trim()))
                        {
                            duplicate = false;
                            break;
                        }
                    }

                    if(duplicate)
                    {
                        throw new DuplicateKeyException("Cannot create record with duplicate key - must be unique");
                    }
                }
            }
        }
        catch(IOException ioe)
        {
            System.err.println(ioe);

            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for read: ");
            message.append(ioe);

            throw new DuplicateKeyException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    System.err.println(ioe);

                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new DuplicateKeyException(message.toString());
                }
            }
        }
    }

    // throws an IllegalStateException if the given data does not match up against the schema in the data source
    private void verifyValidData(String[] data)
    {
        DataFile file = null;

        try
        {
            file = datafileFactory.createDataFile("r");

            DataFileHeader header = file.getHeader();

            if(header.getSchema().length != data.length)
            {
                StringBuffer message = new StringBuffer();
                message.append("Data source schema is not consistent with data [ ");
                message.append(header.getSchema().length);
                message.append(" != ");
                message.append(data.length);
                message.append(" ]");

                throw new IllegalStateException(message.toString());
            }
        }
        catch(IOException ioe)
        {
            System.err.println(ioe);

            StringBuffer message = new StringBuffer();
            message.append("Failed to open data file for read: ");
            message.append(ioe);

            throw new IllegalStateException(message.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    System.err.println(ioe);

                    StringBuffer message = new StringBuffer();
                    message.append("Failed to close data file: ");
                    message.append(ioe);

                    throw new IllegalStateException(message.toString());
                }
            }
        }
    }
}
