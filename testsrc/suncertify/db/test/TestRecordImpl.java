package suncertify.db.test;

import junit.framework.TestCase;
import suncertify.db.DataRecord;
import suncertify.db.DataRecordImpl;

/**
 * An automated test case using the <a href="http://www.junit.org/">JUnit</a> 3.8.1 testing framework.
 * Tests the {@link suncertify.db.DataRecordImpl DataRecordImpl} class.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0
 */
public class TestRecordImpl extends TestCase
{
    /**
     * Constructs a <tt>TestRecordImpl</tt> with a nul implementation.
     */
    public TestRecordImpl()
    {

    }

    /**
     * Tests the {@link suncertify.db.DataRecordImpl#DataRecordImpl() DataRecordImpl no-arg} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor1()
    {
        DataRecord rec = new DataRecordImpl();

        assertNull("DataRecord should contain no data (null)", rec.getData());
        assertTrue("DataRecord should be flagged as not deleted", !rec.isDeleted());
    }

    /**
     * Tests the {@link suncertify.db.DataRecordImpl#DataRecordImpl(boolean) DataRecordImpl(boolean)} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor2()
    {
        DataRecord rec = new DataRecordImpl(true);

        assertNull("DataRecord should contain no data (null)", rec.getData());
        assertTrue("DataRecord should be flagged as deleted", rec.isDeleted());
    }

    /**
     * Tests the {@link suncertify.db.DataRecordImpl#DataRecordImpl(String[]) DataRecordImpl(String[])} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor3()
    {
        DataRecord rec = new DataRecordImpl(new String[]{"blah", "blah"});

        assertEquals("DataRecord should contain 2 data fields", rec.getData().length, 2);
        assertTrue("DataRecord should be flagged as not deleted", !rec.isDeleted());
    }

    /**
     * Tests the {@link suncertify.db.DataRecordImpl#DataRecordImpl(boolean, String[]) DataRecordImpl(boolean, String[])} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor4()
    {
        DataRecord rec = new DataRecordImpl(true, new String[]{"blah", "blah"});

        assertEquals("DataRecord should contain 2 data fields", rec.getData().length, 2);
        assertTrue("DataRecord should be flagged as deleted", rec.isDeleted());
    }

    /**
     * Tests the {@link suncertify.db.DataRecordImpl#equals(Object) DataRecordImpl.equals(Obejct)} method and asserts that certain internal
     * states are equal or not equal.
     */
    public void testEquals()
    {
        DataRecord rec1 = new DataRecordImpl(true, new String[]{"blah", "blah"});
        DataRecord rec2 = new DataRecordImpl(false, new String[]{"blah", "blah"});
        DataRecord rec3 = new DataRecordImpl(true, new String[]{"blah1", "blah"});
        DataRecord rec4 = new DataRecordImpl(false, new String[]{"blah", "blah1"});
        DataRecord rec5 = new DataRecordImpl(true, new String[]{"blah", "blah", "blah"});
        DataRecord rec6 = new DataRecordImpl(true, new String[]{"blah1", "blah"});
        DataRecord rec7 = new DataRecordImpl(false, new String[]{"blah", "blah1"});

        assertFalse("Should be not equal", rec1.equals(rec2));
        assertFalse("Should be not equal", rec1.equals(rec3));
        assertFalse("Should be not equal", rec1.equals(rec4));
        assertFalse("Should be not equal", rec2.equals(rec3));
        assertFalse("Should be not equal", rec2.equals(rec4));
        assertFalse("Should be not equal", rec3.equals(rec4));
        assertFalse("Should be not equal", rec1.equals(rec5));
        assertFalse("Should be not equal", rec5.equals(rec1));

        assertEquals("Should be equal", rec3, rec6);
        assertEquals("Should be equal", rec4, rec7);
    }
}
