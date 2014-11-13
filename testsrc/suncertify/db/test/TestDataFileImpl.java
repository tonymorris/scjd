package suncertify.db.test;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

import suncertify.db.datafile.FieldSchema;
import suncertify.db.datafile.FieldSchemaImpl;
import suncertify.db.datafile.DataFile;
import suncertify.db.datafile.DataFileHeader;
import suncertify.db.datafile.DataFileImpl;
import suncertify.db.DataRecord;
import suncertify.db.DataRecordImpl;

/**
 * An automated test case using the <a href="http://www.junit.org/">JUnit</a> 3.8.1 testing framework.
 * Tests the {@link DataFileImpl DataFileImpl} class.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class TestDataFileImpl extends TestCase
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

    /**
     * Constructs a <tt>TestDataFileImpl</tt> with a null implementation.
     */
    public TestDataFileImpl()
    {

    }

    /**
     * Tests the {@link DataFileImpl#DataFileImpl(String, String) DataFileImpl(String, String)} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor1()
    {
        DataFile file = null;
        File f = null;

        try
        {
            f = File.createTempFile("TestDataFileImpl", "TestCase.db");

            file = new DataFileImpl(f.getAbsolutePath(), "rw");

            assertEquals("File length should be zero", file.length(), 0);
        }
        catch(IOException ioe)
        {
            fail(ioe.toString());
        }
        finally
        {
            if(f != null && f.exists())
            {
                f.delete();
            }

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
     * Tests the {@link DataFileImpl#DataFileImpl(File, String) DataFileImpl(File, String)} constructor and asserts that it's state
     * (internal data members) have certain values.
     */
    public void testConstructor2()
    {
        DataFile file = null;
        File f = null;

        try
        {
            f = File.createTempFile("TestDataFileImpl", "TestCase.db");

            file = new DataFileImpl(f, "rw");

            assertEquals("File length should be zero", file.length(), 0);
        }
        catch(IOException ioe)
        {
            fail(ioe.toString());
        }
        finally
        {
            if(f != null && f.exists())
            {
                f.delete();
            }

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
     * Tests the {@link DataFileImpl#lengthInRecords() DataFileImpl.lengthInRecords()} method.
     * Creates a temporary data file, writes sample records and asserts that the length is the same as the
     * number of sample records.
     */
    public void testRecordLength()
    {
        DataFile file = null;
        File f = null;

        try
        {
            f = File.createTempFile("TestDataFileImpl", "TestCase.db");

            DataFileHeader header = new DataFileHeader(MAGIC_NUMBER, DATA_OFFSET, (short)SCHEMA.length, SCHEMA);

            file = new DataFileImpl(f, "rw");

            file.writeHeader(header);

            int length = (int)file.lengthInRecords();

            file.seekRecord(length);

            file.writeRecord(new DataRecordImpl(new String[]{"testName0", "testLocation0", "testSpecialties0", "450", "670", "testOwn0"}));
            file.writeRecord(new DataRecordImpl(new String[]{"testName0", "testLocation0", "testSpecialties0", "450", "670", "testOwn0"}));
            file.writeRecord(new DataRecordImpl(new String[]{"testName0", "testLocation0", "testSpecialties0", "450", "670", "testOwn0"}));
            file.writeRecord(new DataRecordImpl(new String[]{"testName0", "testLocation0", "testSpecialties0", "450", "670", "testOwn0"}));
            file.writeRecord(new DataRecordImpl(new String[]{"testName0", "testLocation0", "testSpecialties0", "450", "670", "testOwn0"}));

            assertEquals("Should be 5 records in the file", file.lengthInRecords(), 5);

            file.setRecordLength(8);

            assertEquals("Should be 8 records in the file", file.lengthInRecords(), 8);

            file.setRecordLength(3);

            assertEquals("Should be 3 records in the file", file.lengthInRecords(), 3);
        }
        catch(IOException ioe)
        {
            fail(ioe.toString());
        }
        finally
        {
            if(f != null && f.exists())
            {
                f.delete();
            }

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
     * Tests the {@link DataFileImpl#writeRecord(suncertify.db.DataRecord) DataFileImpl.writeRecord(DataRecord)} method and
     * the {@link DataFileImpl#seekRecord(int) DataFileImpl.seekRecord(int)} method.
     * Creates a temporary data file, writes sample records, seeks to certain records, and asserts that
     * the record before writing to file equals the record after being read back from the file.
     */
    public void testWriteRecord()
    {
        DataFile file = null;
        File f = null;

        try
        {
            f = File.createTempFile("TestDataFileImpl", "TestCase.db");

            DataFileHeader header = new DataFileHeader(MAGIC_NUMBER, DATA_OFFSET, (short)SCHEMA.length, SCHEMA);

            file = new DataFileImpl(f, "rw");

            file.writeHeader(header);

            int length = (int)file.lengthInRecords();

            file.seekRecord(length);

            DataRecord rec1 = new DataRecordImpl(new String[]{"testName0", "testLocation0", "testSpecialties0", "450", "670", "testOwn0"});
            DataRecord rec2 = new DataRecordImpl(new String[]{"testName1", "testLocation1", "testSpecialties1", "451", "671", "testOwn1"});
            DataRecord rec3 = new DataRecordImpl(new String[]{"testName2", "testLocation2", "testSpecialties2", "452", "672", "testOwn2"});
            DataRecord rec4 = new DataRecordImpl(new String[]{"testName3", "testLocation3", "testSpecialties3", "453", "673", "testOwn3"});
            DataRecord rec5 = new DataRecordImpl(new String[]{"testName4", "testLocation4", "testSpecialties4", "454", "674", "testOwn4"});

            file.writeRecord(rec1);
            file.writeRecord(rec2);
            file.writeRecord(rec3);
            file.writeRecord(rec4);
            file.writeRecord(rec5);

            DataRecord rec;

            file.seekRecord(0);
            rec = file.nextRecord();

            for(int i = 0; i < rec.getData().length; i++)
            {
                rec.getData()[i] = rec.getData()[i].trim();
            }

            assertEquals("DataRecord data should be the same after going to file and read back again (and fields are trimmed)", rec1, rec);

            file.seekRecord(3);
            rec = file.nextRecord();

            for(int i = 0; i < rec.getData().length; i++)
            {
                rec.getData()[i] = rec.getData()[i].trim();
            }

            assertEquals("DataRecord data should be the same after going to file and read back again (and fields are trimmed)", rec4, rec);

            DataFileHeader header2 = file.readHeader();

            assertEquals("Records header should be the same after going to file and read back again (and fields are trimmed)", header2, header);

            file.clearCachedHeader();

            DataFileHeader header3 = file.readHeader();

            assertEquals("Records header should be the same after going to file and read back again (and fields are trimmed)", header3, header);

            int counter = 0;

            while(file.hasMoreRecords())
            {
                rec = file.nextRecord();

                counter++;
            }

            assertEquals("DataRecord hasMoreRecords() should return true 5 times before returning false", counter, 5);

            assertFalse("File should have no more data", file.hasMoreData());
        }
        catch(IOException ioe)
        {
            fail(ioe.toString());
        }
        finally
        {
            if(f != null && f.exists())
            {
                f.delete();
            }

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
