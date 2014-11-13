package suncertify.db.test;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.DB;
import suncertify.db.datafile.FieldSchema;
import suncertify.db.datafile.FieldSchemaImpl;
import suncertify.db.datafile.DataFile;
import suncertify.db.datafile.DataFileHeader;
import suncertify.db.datafile.DataFileImpl;

/**
 * An automated test case using the <a href="http://www.junit.org/">JUnit</a> 3.8.1 testing framework.
 * Tests the {@link Data Data} class.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class TestData extends TestCase
{
    private final static int MAGIC_NUMBER = 0x00002020;
    private final static int DATA_OFFSET = 0x00000046;

    private final static FieldSchema[] SCHEMA = new FieldSchema[]
    {
        new FieldSchemaImpl("name", (short)32),
        new FieldSchemaImpl("location", (short)64),
        new FieldSchemaImpl("specialties", (short)64),
        new FieldSchemaImpl("size", (short)6),
        new FieldSchemaImpl("rate", (short)8),
        new FieldSchemaImpl("owner", (short)8)
    };

    private static File temp;

    static
    {
        if(temp == null)
        {
            DataFile file = null;

            try
            {
                temp = File.createTempFile("TestData", "TestCase.db");
                temp.deleteOnExit();

                DataFileHeader header = new DataFileHeader(MAGIC_NUMBER, DATA_OFFSET, (short)SCHEMA.length, SCHEMA);

                file = new DataFileImpl(temp.getAbsolutePath(), "rw");

                file.writeHeader(header);
            }
            catch(Exception ioe)
            {
                fail(ioe.toString());
            }
            finally
            {
                if(file != null)
                {
                    try
                    {
                        file.close();
                    }
                    catch(IOException ioe)
                    {
                        fail(ioe.toString());
                    }
                }
            }
        }
    }

    /**
     * Constructs a <tt>TestData</tt> with a null implementation.
     */
    public TestData()
    {

    }

    /**
     * Tests the {@link Data#create(String[]) Data.create(String[])} method.
     * Creates several data records with sample data and asserts that they exist within the data file.
     */
    public void testCreate()
    {
        DataFile file = null;

        try
        {
            createRecord(new String[]{"testName0", "testLocation0", "testSpecialties0", "450", "670", "testOwn0"});
            createRecord(new String[]{"testName1", "testLocation1", "testSpecialties1", "451", "671", "testOwn1"});
            createRecord(new String[]{"testName2", "testLocation2", "testSpecialties2", "452", "672", "testOwn2"});
            createRecord(new String[]{"testName3", "testLocation3", "testSpecialties3", "453", "673", "testOwn3"});
            createRecord(new String[]{"testName4", "testLocation4", "testSpecialties4", "454", "674", "testOwn4"});
            createRecord(new String[]{"testName5", "testLocation5", "testSpecialties5", "455", "675", "testOwn5"});
            createRecord(new String[]{"testName6", "testLocation6", "testSpecialties6", null, "656", "testOwn6"});
            createRecord(new String[]{"testName7", "testLocation7", null, "457", null, null});

            file = new DataFileImpl(temp.getAbsolutePath(), "r");

            assertEquals("Ensuring length after creation of records", file.lengthInRecords(), 8);
        }
        catch(DuplicateKeyException dke)
        {
            fail(dke.toString());
        }
        catch(IOException ioe)
        {
            fail(ioe.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    fail(ioe.toString());
                }
            }
        }
    }

    /**
     * Tests the {@link Data#create(String[]) Data.create(String[])} method.
     * Creates a data record with a <code>null</code> key and asserts that
     * a {@link DuplicateKeyException DuplicateKeyException} is thrown.
     */
    public void testCreateWithNullKey()
    {
        DataFile file = null;

        try
        {
            DataFileHeader header = new DataFileHeader(MAGIC_NUMBER, DATA_OFFSET, (short)SCHEMA.length, SCHEMA);

            file = new DataFileImpl(temp.getAbsolutePath(), "rw");

            file.writeHeader(header);

            file.close();

            createRecord(new String[]{null, "testLocationx", "testSpecialtiesx", "45x", "67x", "testOwnx"});

            fail("Permitted creation of record with null key");
        }
        catch(DuplicateKeyException dke)
        {
            return;
        }
        catch(IOException ioe)
        {
            fail(ioe.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    fail(ioe.toString());
                }
            }
        }

        fail("Permitted to execute to the end of method");
    }

    /**
     * Tests the {@link Data#create(String[]) Data.create(String[])} method.
     * Creates two data records with the same key and asserts that
     * a {@link DuplicateKeyException DuplicateKeyException} is thrown.
     */
    public void testCreateWithDuplicateKey()
    {
        try
        {
            createRecord(new String[]{"testNamey", "testLocationy", "testSpecialtiesy", "45y", "67y", "testOwny"});
            createRecord(new String[]{"testNamey", "testLocationy", "testSpecialtiesz", "45z", "67z", "testOwnz"});

            fail("Permitted creation of record with duplicate key");
        }
        catch(DuplicateKeyException dke)
        {

        }
        catch(FileNotFoundException fnfe)
        {
            fail(fnfe.toString());
        }
    }

    /**
     * Tests the {@link Data#read(int) Data.read(int)} method.
     * Creates a sample record and asserts that a following read will read the same record.
     */
    public void testRead()
    {
        String[] record = new String[]{"testNamez", "testLocationz", "testSpecialtiesz", "45z", "67z", "testOwnz"};

        try
        {
            DB data = new Data(temp.getAbsolutePath());

            int recNo = createRecord(record);
            String[] read = data.read(recNo);

            assertTrue("Different record sizes [" + read.length + "] != [" + record.length + "]", read.length == record.length);

            for(int i = 0; i < read.length; i++)
            {
                assertFalse("Records are different at index " + i + " [" + read[i] + " != [" + record[i] + "]", read[i] == null && record[i] != null);
                assertFalse("Records are different at index " + i + " [" + read[i] + " != [" + record[i] + "]", record[i] == null && read[i] != null);
                assertFalse("Records are different at index " + i + " [" + read[i].trim() + " != [" + record[i] + "]", read[i] != null && record[i] != null && !read[i].trim().equals(record[i]));
            }
        }
        catch(RecordNotFoundException rnfe)
        {
            fail(rnfe.getMessage());
        }
        catch(DuplicateKeyException dke)
        {
            fail(dke.getMessage());
        }
        catch(FileNotFoundException fnfe)
        {
            fail(fnfe.getMessage());
        }
    }

    /**
     * Tests the {@link Data#update(int, String[], long) Data.update(int, String[], long)} method.
     * Updates an existing data record and asserts that a following read will read the same record
     * with the updated data.
     */
    public void testUpdate()
    {
        String[] record = new String[]{"a value that will never be a key " + System.currentTimeMillis(), "another value that will never be a key " + System.currentTimeMillis(), "testSpecialtiesb", "45b", "67b", "testOwnb"};

        try
        {
            DB data = new Data(temp.getAbsolutePath());

            int recNo = 3;

            long lockCookie = data.lock(recNo);

            data.update(recNo, record, lockCookie);

            data.unlock(recNo, lockCookie);

            String[] read = data.read(recNo);

            assertEquals("Different record sizes [" + read.length + "] != [" + record.length + "]", read.length, record.length);

            for(int i = 2; i < read.length; i++)
            {
                assertFalse("Records are different at index " + i + " [" + read[i] + " != [" + record[i] + "]", read[i] == null && record[i] != null);
                assertFalse("Records are different at index " + i + " [" + read[i] + " != [" + record[i] + "]", record[i] == null && read[i] != null);
                assertFalse("Records are different at index " + i + " [" + read[i].trim() + " != [" + record[i] + "]", read[i] != null && record[i] != null && !read[i].trim().equals(record[i]));
            }

            if(read.length > 0 && record.length > 0)
            {
                assertFalse("Update should never overwrite the existing key", read[0].trim().equals(record[0]));
            }
        }
        catch(RecordNotFoundException rnfe)
        {
            fail(rnfe.getMessage());
        }
        catch(SecurityException se)
        {
            fail(se.getMessage());
        }
        catch(FileNotFoundException fnfe)
        {
            fail(fnfe.getMessage());
        }
    }

    /**
     * Tests the {@link Data#delete(int, long) Data.delete(int, long)} method.
     * Deletes an existing data record, and asserts that a following read will return <code>null</code>.
     */
    public void testDelete()
    {
        try
        {
            DB data = new Data(temp.getAbsolutePath());

            int recNo;
            long lockCookie;
            String[] read;

            recNo = 0;

            lockCookie = data.lock(recNo);

            data.delete(recNo, lockCookie);

            data.unlock(recNo, lockCookie);

            read = data.read(recNo);

            assertNull("Should return null because it was just deleted", read);

            recNo = 3;

            lockCookie = data.lock(recNo);

            data.delete(recNo, lockCookie);

            data.unlock(recNo, lockCookie);

            read = data.read(recNo);

            assertNull("Should return null because it was just deleted", read);
        }
        catch(RecordNotFoundException rnfe)
        {
            fail(rnfe.getMessage());
        }
        catch(SecurityException se)
        {
            fail(se.getMessage());
        }
        catch(FileNotFoundException fnfe)
        {
            fail(fnfe.getMessage());
        }
    }

    /**
     * Tests the {@link Data#find(String[]) Data.find(String[])} method.
     * Creates a new data file, adds several sample records and asserts that certain search criteria
     * will return the correct number of records from the data file.
     */
    public void testFind()
    {
        DataFile file = null;
        File temp2 = null;

        try
        {
            temp2 = File.createTempFile("TestData.testFind", "TestCase.db");

            DataFileHeader header = new DataFileHeader(MAGIC_NUMBER, DATA_OFFSET, (short)SCHEMA.length, SCHEMA);

            file = new DataFileImpl(temp2.getAbsolutePath(), "rw");

            file.writeHeader(header);

            DB data = new Data(temp2.getAbsolutePath());

            String[] record;

            record = new String[]{"testName0", "testLocation0", "testSpecialties0", "450", "670", "testOwn0"};
            data.create(record);
            record = new String[]{"testName1", "testLocation1", "testSpecialties1", "451", "671", "testOwn1"};
            data.create(record);
            record = new String[]{"testName2", "testLocation2", "testSpecialties2", "452", "672", "testOwn2"};
            data.create(record);
            record = new String[]{"testName3", "testLocation3", "testSpecialties3", "453", "673", "testOwn3"};
            data.create(record);
            record = new String[]{"testName4", "testLocation4", "testSpecialties4", "454", "674", "testOwn4"};
            data.create(record);
            record = new String[]{"testName5", "testLocation5", "testSpecialties5", "455", "675", "testOwn5"};
            data.create(record);
            record = new String[]{"testName6", "testLocation6", "testSpecialties6", null, "656", "testOwn6"};
            data.create(record);
            record = new String[]{"testName7", "testLocation7", null, "457", null, null};
            data.create(record);

            int[] indicies;

            indicies = data.find(new String[]{null, null, null, null, null, null});
            assertEquals("Should find 8 records", indicies.length, 8);

            indicies = data.find(new String[]{"test", null, null, null, null, null});
            assertEquals("Should find 8 records", indicies.length, 8);

            indicies = data.find(new String[]{"testName1", null, null, null, null, null});
            assertEquals("Should find 1 record", indicies.length, 1);

            indicies = data.find(new String[]{"test", null, null, "4", null, null});
            assertEquals("Should find 7 records", indicies.length, 7);

            int recNo = 0;
            long lockCookie;

            lockCookie = data.lock(recNo);

            data.delete(recNo, lockCookie);

            data.unlock(recNo, lockCookie);

            recNo = 3;

            lockCookie = data.lock(recNo);

            data.delete(recNo, lockCookie);

            data.unlock(recNo, lockCookie);

            indicies = data.find(new String[]{null, null, null, null, null, null});
            assertEquals("Should find 6 records", indicies.length, 6);

            indicies = data.find(new String[]{"test", null, null, null, null, null});
            assertEquals("Should find 6 records", indicies.length, 6);

            indicies = data.find(new String[]{"testName1", null, null, null, null, null});
            assertEquals("Should find 1 record", indicies.length, 1);

            indicies = data.find(new String[]{"test", null, null, "4", null, null});
            assertEquals("Should find 5 records", indicies.length, 5);
        }
        catch(Exception ioe)
        {
            fail(ioe.toString());
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException ioe)
                {
                    fail(ioe.toString());
                }
            }

            if(temp2 != null && temp2.exists())
            {
                temp2.delete();
            }
        }
    }

    private int createRecord(String[] record) throws DuplicateKeyException, FileNotFoundException
    {
        DB data = new Data(temp.getAbsolutePath());

        int index = data.create(record);

        assertFalse("Internal Server Error - new record index [" + index + " < 0]", index < 0);

        return index;
    }
}
