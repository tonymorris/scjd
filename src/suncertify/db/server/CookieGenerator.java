package suncertify.db.server;

/**
 * Provides an interface for generating a cookie value from a record and vice versa.
 *
 * @see CookieGeneratorImpl
 * @see CookieGeneratorFactory
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface CookieGenerator
{
    /**
     * Generate a cookie value from a record number.
     *
     * @param recordNumber The record number to generate the cookie for
     * @return The generated cookie value.
     */
    public long getCookie(int recordNumber);

    /**
     * Generate a record number from a cookie value.
     *
     * @param cookie The cookie to generate the record number for
     * @return The generated record number value.
     */
    public int getRecordNumber(long cookie);
}
