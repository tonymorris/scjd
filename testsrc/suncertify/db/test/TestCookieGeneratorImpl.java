package suncertify.db.test;

import junit.framework.TestCase;
import suncertify.db.server.CookieGenerator;
import suncertify.db.server.CookieGeneratorImpl;

/**
 * An automated test case using the <a href="http://www.junit.org/">JUnit</a> 3.8.1 testing framework.
 * Tests the {@link CookieGeneratorImpl CookieGeneratorImpl} class.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class TestCookieGeneratorImpl extends TestCase
{
    /**
     * Constructs a <tt>TestCookieGeneratorImpl</tt> with a null implementation.
     */
    public TestCookieGeneratorImpl()
    {

    }

    /**
     * Creates a {@link CookieGeneratorImpl CookieGeneratorImpl} and asserts that converting to and from
     * cookie values should return the same result.  Tests upper and lower bounds
     * (<tt>Integer.MAX_VALUE</tt> and <tt>Integer.MIN_VALUE</tt>) and a random selection of numbers.
     */
    public void testCookieGenerator()
    {
        CookieGenerator cg = new CookieGeneratorImpl();

        long cookie;
        int recordNumber;

        cookie = cg.getCookie(0);
        recordNumber = cg.getRecordNumber(cookie);

        assertEquals("Retrieving cookie, then record number should return the same", recordNumber, 0);

        cookie = cg.getCookie(1);
        recordNumber = cg.getRecordNumber(cookie);

        assertEquals("Retrieving cookie, then record number should return the same", recordNumber, 1);

        cookie = cg.getCookie(4567);
        recordNumber = cg.getRecordNumber(cookie);

        assertEquals("Retrieving cookie, then record number should return the same", recordNumber, 4567);

        cookie = cg.getCookie(Integer.MAX_VALUE);
        recordNumber = cg.getRecordNumber(cookie);

        assertEquals("Retrieving cookie, then record number should return the same", recordNumber, Integer.MAX_VALUE);

        cookie = cg.getCookie(Integer.MIN_VALUE);
        recordNumber = cg.getRecordNumber(cookie);

        assertEquals("Retrieving cookie, then record number should return the same", recordNumber, Integer.MIN_VALUE);

        cookie = cg.getCookie(Integer.MAX_VALUE - 1);
        recordNumber = cg.getRecordNumber(cookie);

        assertEquals("Retrieving cookie, then record number should return the same", recordNumber, Integer.MAX_VALUE - 1);

        cookie = cg.getCookie(Integer.MIN_VALUE + 1);
        recordNumber = cg.getRecordNumber(cookie);

        assertEquals("Retrieving cookie, then record number should return the same", recordNumber, Integer.MIN_VALUE + 1);
    }
}
