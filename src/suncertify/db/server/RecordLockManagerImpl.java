package suncertify.db.server;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

/**
 * A manager of data file record locks to enforce serial write access to each record.
 * DataRecord locks are acquired and released by clients.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class RecordLockManagerImpl implements RecordLockManager
{
    private CookieGeneratorFactory cgFactory;
    private Map locks;

    /**
     * Constructs a <tt>RecordLockManagerImpl</tt>.
     * The underlying {@link CookieGeneratorFactory CookieGeneratorFactory} is instantiated as a
     * {@link CookieGeneratorFactoryImpl CookieGeneratorFactoryImpl}.
     * The underlying storage of record locks is provided by a <code>java.util.HashMap</code>.
     * The <code>HashMap</code> is synchronized using <code>java.util.Collections</code>.
     */
    public RecordLockManagerImpl()
    {
        locks = Collections.synchronizedMap(new HashMap());
        cgFactory = new CookieGeneratorFactoryImpl();
    }

    /**
     * Make an attempt to acquire the record lock for the given record number.
     * If the record lock is currently in use, the client thread will be put into wait
     * with a call to {@link Lock#acquire() Lock.acquire()}.
     *
     * @param recordNumber The record number to attempt to acquire the lock for.
     * @return The generated cookie value corresponding to the given record number.
     */
    public long lock(int recordNumber)
    {
        Integer key = new Integer(recordNumber);
        long cookie = cgFactory.createCookieGenerator().getCookie(recordNumber);

        Lock lock;

        synchronized(locks)
        {
            lock = (Lock)locks.get(key);

            // if the record isn't locked
            if(lock == null)
            {
                lock = new Lock();
                locks.put(key, lock);

                return cookie;
            }
        }

        lock.acquire();

        return cookie;
    }

    /**
     * Release the record lock for the given record number.
     * If there are any client threads waiting to acquire the lock, one of them will be
     * notified will a call to {@link Lock#release() Lock.release()}.
     *
     * @param recordNumber The record number to release the lock for.
     */
    public void unlock(int recordNumber)
    {
        Integer key = new Integer(recordNumber);

        synchronized(locks)
        {
            Lock lock = (Lock)locks.get(key);

            // only continue if an attempt was made to unlock an already locked record
            if(lock != null)
            {
                synchronized(lock)
                {
                    // notify one of the waiting clients
                    if(!lock.hasWaitingClients())
                    {
                        locks.remove(key);
                    }

                    lock.release();
                }
            }
        }
    }

    /**
     * Returns <code>true</code> if the given cookie value is valid for the given record number, <code>false</code> otherwise.
     * This depends on the underlying implementation of {@link CookieGeneratorFactory CookieGeneratorFactory}, which in this case,
     * is a {@link CookieGeneratorFactoryImpl CookieGeneratorFactoryImpl}.
     *
     * @param recordNumber The cookie value to use to attempt to validate the record number.
     * @param cookie The cookie value to use to attempt to validate the record number.
     * @return <code>true</code> if the given cookie value is valid for the given record number, <code>false</code> otherwise.
     */
    public boolean isValidCookie(int recordNumber, long cookie)
    {
        return (cgFactory.createCookieGenerator().getRecordNumber(cookie) == recordNumber);
    }
}
