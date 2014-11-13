package suncertify.db.server;

/**
 * Provides an interface for managing record locks.
 * DataRecord locks are acquired and released by clients to enforce serial write access to a data record.
 *
 * @see RecordLockManagerImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface RecordLockManager
{
    /**
     * Make an attempt to acquire the lock for the given data record number.
     * Client threads that attempting to acquire the record lock while it is in use should be put into
     * the wait state.
     *
     * @param recordNumber The data record number to attempt to acquire the lock for.
     * @return A cookie value to indicate ownership of the lock that can be used for subsequent
     * write accesses to the data record.
     */
    public long lock(int recordNumber);

    /**
     * Release the lock for the given data record.
     * If there are any client threads waiting for the record lock, one should be "notified" that the
     * lock is available so that it can make an attempt to acquire it.
     *
     * @param recordNumber The data record number to release the lock for.
     */
    public void unlock(int recordNumber);

    /**
     * Returns <code>true</code> if the given cookie value is valid for the given record number, <code>false</code> otherwise.
     *
     * @param recordNumber The data record number to validate the cookie value for.
     * @param cookie The cookie value to use to attempt to validate the record number.
     * @return <code>true</code> if the given cookie value is valid for the given record number, <code>false</code> otherwise.
     */
    public boolean isValidCookie(int recordNumber, long cookie);
}
