package suncertify.db.server;

/**
 * Represents a lock of a data file record.  The lock maintains an internal counter of the number of client threads
 * that are waiting to acquire the lock. It is indeterminate which client will acquire the record lock once the
 * holder releases it. Specifically, clients don't "queue" for the record lock - clients wait, and the client thread
 * that is "notified" will be the next to acquire the record lock. It is not guaranteed which waiting client will
 * be "notified".
 *
 * Multiple clients may compete for the acquisition of a lock for a record.
 * This class is internally <b>thread-safe</b>. That is, multiple client threads can concurrently execute methods on a
 * single instance of this class and be assured that data corruption will not occur.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Lock
{
    private long waitingClients;

    /**
     * Construct a <tt>Lock</tt> with all default values.
     * The number of waiting clients is set to zero.
     */
    public Lock()
    {
        this.waitingClients = 0;
    }

    /**
     * Acquires the record lock for the calling client thread.
     * The number of waiting client threads will be incremented by 1.
     * The calling client thread will be put into wait until the lock becomes available.
     *
     * @throws IllegalStateException If the waiting client thread is interrupted. This should never occur.
     */
    public synchronized void acquire() throws IllegalStateException
    {
        this.waitingClients++;

        try
        {
            wait();
        }
        catch(InterruptedException ie)
        {
            StringBuffer message = new StringBuffer();
            message.append("Thread unexpectedly interrupted: ");
            message.append(ie);

            throw new IllegalStateException(message.toString());
        }
    }

    /**
     * Releases the record lock for the calling client.
     * If there are waiting client threads, a notify will be made to one of the threads
     * (which will bring it out of the wait state and will acquire the lock).
     * The number of waiting client threads will be decremented by 1.
     */
    public synchronized void release()
    {
        if(waitingClients > 0)
        {
            notify();

            this.waitingClients--;
        }
    }

    /**
     * Returns the number of clients that are waiting to acquire this record lock.
     *
     * @return The number of clients that are waiting to acquire this record lock.
     */
    public synchronized long getWaitingClients()
    {
        return waitingClients;
    }

    /**
     * Returns <code>true</code> if there are client threads that are waiting to acquire this record lock, <code>false</code> otherwise.
     *
     * @return <code>true</code> if there are client threads that are waiting to acquire this record lock, <code>false</code> otherwise.
     */
    public synchronized boolean hasWaitingClients()
    {
        return this.waitingClients > 0;
    }

    /**
     * Performs a deep equality comparison on the given parameter against this instance of <tt>Lock</tt>.
     * Returns <code>true</code> if the argument is not <code>null</code>, is an instance of <tt>Lock</tt>,
     * and the number of waiting clients are the same.
     *
     * @param o The object to perform a "deep equality" comparison with this <tt>Lock</tt>.
     * @return <code>true</code> if this <tt>Lock</tt> is determined to be equal to the given object,
     * <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }

        if(o == null)
        {
            return false;
        }

        if(this.getClass() != o.getClass())
        {
            return false;
        }

        Lock lock = (Lock)o;

        return(this.getWaitingClients() == lock.getWaitingClients());
    }

    /**
     * Returns a hash code for the benefit of of data structures that perform a "hash" on their elements
     * (such as java.util.HashMap).
     *
     * @return A hash code for the benefit of of data structures that perform a "hash" on their elements
     * (such as java.util.HashMap).
     */
    public int hashCode()
    {
        final int ODD_PRIME = 461;
        int result = 73;

        result = result * ODD_PRIME + (int)(this.getWaitingClients() >>> 32);

        return result;
    }

    /**
     * Returns a <code>String</code> representation of this <tt>Lock</tt>.
     * The result will contain the following:
     * <li>[<i>number of waiting client threads property</i>]</li>
     *
     * @return A <code>String</code> representation of this <tt>Lock</tt>.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append('[');
        sb.append(this.getWaitingClients());
        sb.append(']');

        return sb.toString();
    }
}
