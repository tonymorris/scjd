package suncertify.db.test;

import junit.framework.TestCase;
import suncertify.db.server.FieldMatcher;
import suncertify.db.server.FieldMatcherImpl;

/**
 * An automated test case using the <a href="http://www.junit.org/">JUnit</a> 3.8.1 testing framework.
 * Tests the {@link FieldMatcherImpl FieldMatcherImpl} class.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class TestFieldMatcherImpl extends TestCase
{
    /**
     * Constructs a <tt>TestFieldMatcherImpl</tt> with a null implementation.
     */
    public TestFieldMatcherImpl()
    {

    }

    /**
     * Creates a {@link FieldMatcherImpl FieldMatcherImpl} and asserts that sample values will
     * match according to the intended implementation of the class.  Also asserts that certain samples
     * will not match.
     */
    public void testFieldMatcher()
    {
        FieldMatcher matcher = new FieldMatcherImpl();

        assertTrue("Should match", matcher.matches("blah", "blah"));
        assertTrue("Should match", matcher.matches("blah", "bla"));
        assertTrue("Should match", matcher.matches("blah", ""));
        assertTrue("Should match", matcher.matches(null, null));
        assertTrue("Should match", matcher.matches("", null));
        assertTrue("Should match", matcher.matches("", ""));

        assertFalse("Should not match", matcher.matches("bla", "blah"));
        assertFalse("Should not match", matcher.matches(null, ""));
        assertFalse("Should not match", matcher.matches("", "b"));
    }
}
