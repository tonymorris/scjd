package suncertify.db.test;

import junit.framework.TestCase;
import suncertify.db.DataRecordImpl;
import suncertify.db.server.RecordMatcher;
import suncertify.db.server.RecordMatcherImpl;

/**
 * An automated test case using the <a href="http://www.junit.org/">JUnit</a> 3.8.1 testing framework.
 * Tests the {@link RecordMatcherImpl RecordMatcherImpl} class.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class TestRecordMatcherImpl extends TestCase
{
    /**
     * Constructs a <tt>TestRecordMatcherImpl</tt> with a null implementation.
     */
    public TestRecordMatcherImpl()
    {

    }

    /**
     * Creates a {@link RecordMatcherImpl RecordMatcherImpl} and asserts that sample values will
     * match according to the intended implementation of the class.  Also asserts that certain samples
     * will not match.
     */
    public void testRecordMatcher()
    {
        RecordMatcher matcher = new RecordMatcherImpl();

        assertTrue("Should match", matcher.matches(new DataRecordImpl(new String[]{"blah", "blah"}), new String[]{null, null}));
        assertTrue("Should match", matcher.matches(new DataRecordImpl(new String[]{"blah", "blah"}), new String[]{"blah", null}));
        assertTrue("Should match", matcher.matches(new DataRecordImpl(new String[]{"blah", "blah"}), new String[]{"blah", "blah"}));
        assertTrue("Should match", matcher.matches(new DataRecordImpl(new String[]{"blah", "blah"}), new String[]{"b", "b"}));
        assertTrue("Should match", matcher.matches(new DataRecordImpl(new String[]{null, "blah"}), new String[]{null, null}));
        assertTrue("Should match", matcher.matches(new DataRecordImpl(new String[]{null, null}), new String[]{null, null}));

        assertFalse("Should not match", matcher.matches(new DataRecordImpl(new String[]{"bla", "blah"}), new String[]{"blah", "blah"}));
        assertFalse("Should not match", matcher.matches(new DataRecordImpl(new String[]{"blah", "bla"}), new String[]{"blah", "blah"}));
        assertFalse("Should not match", matcher.matches(new DataRecordImpl(new String[]{"blah", "blah", null}), new String[]{"blah", "blah"}));
        assertFalse("Should not match", matcher.matches(new DataRecordImpl(new String[]{"blah", "blah"}), new String[]{"blah", "blah", null}));
        assertFalse("Should not match", matcher.matches(new DataRecordImpl(new String[]{"blah", "blah", "blah"}), new String[]{"blah", "blah"}));
        assertFalse("Should not match", matcher.matches(new DataRecordImpl(new String[]{"blah", "blah"}), new String[]{"blah", "blah", "blah"}));
    }
}
