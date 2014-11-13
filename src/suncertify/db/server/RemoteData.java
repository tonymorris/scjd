package suncertify.db.server;

import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;
import suncertify.db.DuplicateKeyException;
import suncertify.db.DB;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.FileNotFoundException;

/**
 * Provides a wrapper around a {@link Data Data} instance and proxies any method calls to the
 * underlying instance. This is an example of the Adapter Design Pattern. This class provides
 * the same public API as the {@link Data Data} class, however, with the ability to be exported
 * as an RMI object.
 *
 * @see DB
 * @see Data
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class RemoteData extends UnicastRemoteObject implements RemoteDB
{
    private DB data;

    /**
     * Construct a <code>RemoteData</code> instance with an underlying {@link Data Data} instance.
     *
     * @see Data#Data(String)
     * @param dataFilename The name of the file containing the data to provide the interface to. If it can be guaranteed
     * that the <code>RemoteData</code> instance been created (this method has been called previously), this parameter may be passed as <code>null</code>.
     * @throws FileNotFoundException If the data file does not exist, is a directory, cannot be read or cannot be written to.
     * @throws RemoteException If a communications error occurs while this constructor is called over RMI.
     */
    public RemoteData(String dataFilename) throws FileNotFoundException, RemoteException
    {
        super();

        this.data = new Data(dataFilename);
    }

    /**
     * Proxies the method call to the underlying {@link Data#read(int) Data.read(int) method}.
     *
     * @see Data#read(int)
     * @param recNo The record number to read.
     * @return The record data that was read.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public String[] read(int recNo) throws RecordNotFoundException, RemoteException
    {
        return this.data.read(recNo);
    }

    /**
     * Proxies the method call to the underlying {@link Data#update(int, String[], long) Data.update(int, String[], long) method}.
     *
     * @see Data#update(int, String[], long)
     * @param recNo The record number to update.
     * @param data The data to update with.
     * @param lockCookie The cookie value to use to prove that the caller owns the write lock for the record.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws SecurityException If the given cookie value does not validate.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public void update(int recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException, RemoteException
    {
        this.data.update(recNo, data, lockCookie);
    }

    /**
     * Proxies the method call to the underlying {@link Data#delete(int, long) Data.update(int, long) method}.
     *
     * @see Data#delete(int, long)
     * @param recNo The record number to delete.
     * @param lockCookie The cookie value to use to prove that the caller owns the write lock for the record.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws SecurityException If the given cookie value does not validate.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public void delete(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException, RemoteException
    {
        this.data.delete(recNo, lockCookie);
    }

    /**
     * Proxies the method call to the underlying {@link Data#find(String[]) Data.find(String[]) method}.
     *
     * @see Data#find(String[])
     * @param criteria The criteria to search for.
     * @return An array of indicies of data records that match the given criteria.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public int[] find(String[] criteria) throws RemoteException
    {
        return this.data.find(criteria);
    }

    /**
     * Proxies the method call to the underlying {@link Data#create(String[]) Data.create(String[]) method}.
     *
     * @see Data#create(String[])
     * @param data The data to create the new data record with.
     * @return The index of the created record or a value less than zero if an error occurred on the server.
     * @throws DuplicateKeyException If the new data in the record contains a "primary key" that already exists in the data file.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public int create(String[] data) throws DuplicateKeyException, RemoteException
    {
        return this.data.create(data);
    }

    /**
     * Proxies the method call to the underlying {@link Data#lock(int) Data.lock(int) method}.
     *
     * @param recNo The record number to lock.
     * @return A cookie value to use to permit write access to the data record that was locked.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public long lock(int recNo) throws RecordNotFoundException, RemoteException
    {
        return this.data.lock(recNo);
    }

    /**
     * Proxies the method call to the underlying {@link Data#unlock(int, long) Data.unlock(int, lock) method}.
     *
     * @param recNo The record number to lock.
     * @param cookie The cookie value to use to prove that the caller owns the write lock for the record.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws SecurityException If the given cookie value does not validate.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public void unlock(int recNo, long cookie) throws RecordNotFoundException, SecurityException, RemoteException
    {
        this.data.unlock(recNo, cookie);
    }
}
