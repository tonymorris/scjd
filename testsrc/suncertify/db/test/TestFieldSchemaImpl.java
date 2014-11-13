package suncertify.db.test;

import junit.framework.TestCase;
import suncertify.db.datafile.FieldSchema;
import suncertify.db.datafile.FieldSchemaImpl;

/**
 * An automated test case using the <a href="http://www.junit.org/">JUnit</a> 3.8.1 testing framework.
 * Tests the {@link FieldSchemaImpl SchemaColumnImpl} class.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class TestFieldSchemaImpl extends TestCase
{
    /**
     * Constructs a <tt>TestFieldSchemaImpl</tt> with a null implementation.
     */
    public TestFieldSchemaImpl()
    {

    }

    /**
     * Tests the {@link FieldSchemaImpl#FieldSchemaImpl() SchemaColumnImpl no-arg} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor1()
    {
        FieldSchema fs = new FieldSchemaImpl();

        assertNull("Field Schema should not have a name", fs.getName());
        assertEquals("Field Schema length should be 0", fs.getLength(), 0);
    }

    /**
     * Tests the {@link FieldSchemaImpl#FieldSchemaImpl(String) SchemaColumnImpl(String)} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor2()
    {
        FieldSchema fs = new FieldSchemaImpl("name");

        assertEquals("Field Schema should have a name", fs.getName(), "name");
        assertEquals("Field Schema length should be 0", fs.getLength(), 0);
    }

    /**
     * Tests the {@link FieldSchemaImpl#FieldSchemaImpl(short) SchemaColumnImpl(short)} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor3()
    {
        FieldSchema fs = new FieldSchemaImpl((short)5);

        assertNull("Field Schema should not have a name", fs.getName());
        assertEquals("Field Schema length should be 5", fs.getLength(), 5);
    }

    /**
     * Tests the {@link FieldSchemaImpl#FieldSchemaImpl(short) SchemaColumnImpl(String, short)} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor4()
    {
        FieldSchema fs = new FieldSchemaImpl("name", (short)5);

        assertEquals("Field Schema should have a name", fs.getName(), "name");
        assertEquals("Field Schema length should be 5", fs.getLength(), 5);
    }

    /**
     * Tests the {@link FieldSchemaImpl#equals(Object) SchemaColumnImpl.equals(Obejct)} method and asserts that certain internal
     * states are equal or not equal.
     */
    public void testEquals()
    {
        FieldSchema fs1 = new FieldSchemaImpl();
        FieldSchema fs2 = new FieldSchemaImpl("name");
        FieldSchema fs3 = new FieldSchemaImpl((short)5);
        FieldSchema fs4 = new FieldSchemaImpl("name", (short)5);
        FieldSchema fs5 = new FieldSchemaImpl(null, (short)5);
        FieldSchema fs6 = new FieldSchemaImpl();
        FieldSchema fs7 = new FieldSchemaImpl("name");
        FieldSchema fs8 = new FieldSchemaImpl((short)5);
        FieldSchema fs9 = new FieldSchemaImpl("name", (short)5);
        FieldSchema fs10 = new FieldSchemaImpl(null, (short)5);

        assertEquals("Should be equal", fs1, fs6);
        assertEquals("Should be equal", fs2, fs7);
        assertEquals("Should be equal", fs3, fs8);
        assertEquals("Should be equal", fs4, fs9);
        assertEquals("Should be equal", fs5, fs10);

        assertFalse("Should not be equal", fs1.equals(fs2));
        assertFalse("Should not be equal", fs1.equals(fs3));
        assertFalse("Should not be equal", fs1.equals(fs4));
        assertFalse("Should not be equal", fs1.equals(fs5));
        assertFalse("Should not be equal", fs2.equals(fs3));
        assertFalse("Should not be equal", fs2.equals(fs4));
        assertFalse("Should not be equal", fs2.equals(fs5));
        assertFalse("Should not be equal", fs3.equals(fs4));
        assertFalse("Should not be equal", fs4.equals(fs5));
    }
}
