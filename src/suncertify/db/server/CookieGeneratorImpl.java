package suncertify.db.server;

/**
 * Provides an implementation of a {@link CookieGenerator CookieGenerator}.
 * This implementation makes a mild attempt to "obscure" the actual record number within the generated cookie value.
 *
 * @see CookieGenerator
 * @see CookieGeneratorFactory
 * @see CookieGeneratorFactoryImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class CookieGeneratorImpl implements CookieGenerator
{
    /**
     * Constructs a <tt>CookieGeneratorImpl</tt> with a null implementation.
     */
    public CookieGeneratorImpl()
    {

    }

    /**
     * Generate a cookie value from a record number.
     * Puts the record number into a <code>long</code>, shifts left 32 bits,
     * then performs a logical AND with a random <code>int</code>.
     * The bottom 32 bits of the <code>long</code> have no meaning.
     *
     * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/expressions.doc.html#5121">Java Language Specification 15.19</a>
     *
     * @param recordNumber The record number to generate the cookie value for.
     * @return The generated cookie value.
     */
    public long getCookie(int recordNumber)
    {
        long value = recordNumber;

        value = value << 32;

        value = value & (int)(Math.random() * Integer.MIN_VALUE);

        return value;
    }

    /**
     * Generates a record number from a cookie value.
     * Reverses everything that had occurred in the generation of the cookie to determine the record number.
     * Performs an "unsigned right shift" 32 bits, then downcasts to <code>int</code>.
     *
     * See <a href="http://java.sun.com/docs/books/jls/second_edition/html/expressions.doc.html#5121">Java Language Specification 15.19</a>
     *
     * @param cookie The cookie value to generate the record number for.
     * @return The record number.
     */
    public int getRecordNumber(long cookie)
    {
        cookie = cookie >>> 32;

        return (int)cookie;
    }
}
