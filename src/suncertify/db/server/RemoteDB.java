package suncertify.db.server;

import suncertify.db.RecordNotFoundException;
import suncertify.db.DuplicateKeyException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Defines the same interface as the {@link suncertify.db.DB DB} interface, however,
 * provides the ability for the methods to be called over RMI.
 * Allows client classes to access the back-end data file,
 * which contains record data.
 *
 * @see suncertify.db.DB
 * @see suncertify.db.Data
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface RemoteDB extends Remote
{
    /**
     * Reads the given record number from the data file.
     *
     * @param recNo The record number to read.
     * @return The record data that was read.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public String[] read(int recNo) throws RecordNotFoundException, RemoteException;

    /**
     * Updates the given record number with the given data. The data's primary key value is ignored.
     * The lock cookie must validate for the record that is attempting to be updated.
     *
     * @param recNo The record number to update.
     * @param data The data to update with.
     * @param lockCookie The cookie value to use to prove that the caller owns the write lock for the record.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws SecurityException If the given cookie value does not validate.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public void update(int recNo, String[] data, long lockCookie) throws RecordNotFoundException, SecurityException, RemoteException;

    /**
     * Deletes the given record number. The lock cookie must validate for the record that is attempting to be deleted.
     *
     * @param recNo The record number to delete.
     * @param lockCookie The cookie value to use to prove that the caller owns the write lock for the record.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws SecurityException If the given cookie value does not validate.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public void delete(int recNo, long lockCookie) throws RecordNotFoundException, SecurityException, RemoteException;

    /**
     * Searches for data records that match the given criteria.
     *
     * @param criteria The criteria to search for.
     * @return An array of indicies of data records that match the given criteria.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public int[] find(String[] criteria) throws RemoteException;

    /**
     * Creates a data record with the given record data.
     *
     * @param data The data to create the new data record with.
     * @return The index of the created record or a value less than zero if an error occurred on the server.
     * @throws DuplicateKeyException If the new data in the record contains a "primary key" that already exists in the data file.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public int create(String[] data) throws DuplicateKeyException, RemoteException;

    /**
     * Attempts to acquire the lock for the given data record. If the lock is in use,
     * the calling client thread will be put on wait.
     *
     * @param recNo The record number to lock.
     * @return A cookie value to use to permit write access to the data record that was locked.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public long lock(int recNo) throws RecordNotFoundException, RemoteException;

    /**
     * Releases the lock for the given record number. If there is one or more client threads that are
     * waiting to acquire the lock, one of them will be notified that the lock has become available.
     * The lock cookie must validate for the record that is attempting to be unlocked.
     *
     * @param recNo The record number to lock.
     * @param cookie The cookie value to use to prove that the caller owns the write lock for the record.
     * @throws RecordNotFoundException If the record number does not exist.
     * @throws SecurityException If the given cookie value does not validate.
     * @throws RemoteException If a communications error occurs while this method is called over RMI.
     */
    public void unlock(int recNo, long cookie) throws RecordNotFoundException, SecurityException, RemoteException;
}
