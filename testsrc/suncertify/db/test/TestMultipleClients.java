package suncertify.db.test;

import junit.framework.TestCase;
import suncertify.db.datafile.FieldSchema;
import suncertify.db.datafile.FieldSchemaImpl;
import suncertify.db.datafile.DataFile;
import suncertify.db.datafile.DataFileHeader;
import suncertify.db.datafile.DataFileImpl;
import suncertify.db.DB;
import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * An automated test case using the <a href="http://www.junit.org/">JUnit</a> 3.8.1 testing framework.
 * Tests the {@link Data Data} class by simulating multiple clients accessing it as different <code>Thread</code>s
 * of execution.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class TestMultipleClients extends TestCase
{
    private final static int MAGIC_NUMBER = 0x00002020;
    private final static int DATA_OFFSET = 0x00000046;

    private File f;
    private DB data;

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
     * Constructs a <tt>TestMultipleClients</tt> with a null implementation.
     */
    public TestMultipleClients()
    {

    }

    /**
     * Sets up a temporary data file with a header containing a valid schema.
     */
    public void setUp()
    {
        DataFile df = null;

        try
        {
            f = File.createTempFile("TestData", "TestCase.db");

            DataFileHeader header = new DataFileHeader(MAGIC_NUMBER, DATA_OFFSET, (short)SCHEMA.length, SCHEMA);

            df = new DataFileImpl(f.getAbsolutePath(), "rw");

            df.writeHeader(header);

            data = new Data(f.getAbsolutePath());
        }
        catch(IOException ioe)
        {
            StringWriter sw = new StringWriter();
            ioe.printStackTrace(new PrintWriter(sw));
            fail(sw.toString());
        }
        finally
        {
            if(df != null)
            {
                try
                {
                    df.close();
                }
                catch(IOException ioe)
                {
                    StringWriter sw = new StringWriter();
                    ioe.printStackTrace(new PrintWriter(sw));
                    fail(sw.toString());
                }
            }
        }
    }

    /**
     * Tests the {@link Data#create(String[]) Data.create(String[])} method with 10 concurrent clients.
     */
    public void test10CreateClients()
    {
        Thread[] clients = new Thread[10];

        for(int i = 0; i < clients.length; i++)
        {
            clients[i] = new Thread(new CreateClient(this, data));
        }

        for(int i = 0; i < clients.length; i++)
        {
            clients[i].start();
        }

        for(int i = 0; i < clients.length; i++)
        {
            try
            {
                clients[i].join();
            }
            catch(InterruptedException ie)
            {
                StringWriter sw = new StringWriter();
                ie.printStackTrace(new PrintWriter(sw));
                fail(sw.toString());
            }
        }
    }

    /**
     * Tests the {@link Data#lock(int) Data.lock(int)} method with 10 concurrent clients.
     */
    public void test10LockClients()
    {
        Thread[] clients = new Thread[10];

        for(int i = 0; i < clients.length; i++)
        {
            clients[i] = new Thread(new LockClient(this, data));
        }

        for(int i = 0; i < clients.length; i++)
        {
            clients[i].start();
        }

        for(int i = 0; i < clients.length; i++)
        {
            try
            {
                clients[i].join();
            }
            catch(InterruptedException ie)
            {
                StringWriter sw = new StringWriter();
                ie.printStackTrace(new PrintWriter(sw));
                fail(sw.toString());
            }
        }
    }

    /**
     * Tests the {@link Data#update(int, String[], long) Data.update(int, String[], long)} method with 10 concurrent clients.
     */
    public void test10UpdateClients()
    {
        Thread[] clients = new Thread[10];

        for(int i = 0; i < clients.length; i++)
        {
            clients[i] = new Thread(new UpdateClient(this, data));
        }

        for(int i = 0; i < clients.length; i++)
        {
            clients[i].start();
        }

        for(int i = 0; i < clients.length; i++)
        {
            try
            {
                clients[i].join();
            }
            catch(InterruptedException ie)
            {
                StringWriter sw = new StringWriter();
                ie.printStackTrace(new PrintWriter(sw));
                fail(sw.toString());
            }
        }
    }

    /**
     * Tests the {@link Data#find(String[]) Data.find(String[])} method with 10 concurrent clients.
     */
    public void test10FindClients()
    {
        Thread[] clients = new Thread[10];

        for(int i = 0; i < clients.length; i++)
        {
            clients[i] = new Thread(new FindClient(this, data));
        }

        for(int i = 0; i < clients.length; i++)
        {
            clients[i].start();
        }

        for(int i = 0; i < clients.length; i++)
        {
            try
            {
                clients[i].join();
            }
            catch(InterruptedException ie)
            {
                StringWriter sw = new StringWriter();
                ie.printStackTrace(new PrintWriter(sw));
                fail(sw.toString());
            }
        }
    }

    /**
     * Tests the {@link Data#delete(int, long) Data.delete(int, long)} method with 10 concurrent clients.
     */
    public void test10DeleteClients()
    {
        Thread[] clients = new Thread[10];

        for(int i = 0; i < clients.length; i++)
        {
            clients[i] = new Thread(new DeleteClient(this, data));
        }

        for(int i = 0; i < clients.length; i++)
        {
            clients[i].start();
        }

        for(int i = 0; i < clients.length; i++)
        {
            try
            {
                clients[i].join();
            }
            catch(InterruptedException ie)
            {
                StringWriter sw = new StringWriter();
                ie.printStackTrace(new PrintWriter(sw));
                fail(sw.toString());
            }
        }
    }

    /**
     * Deletes any temporary files that were created during the test case.
     */
    public void tearDown()
    {
        if(f != null && f.exists())
        {
            f.delete();
        }
    }

    private class CreateClient implements Runnable
    {
        private TestCase tc;
        private DB data;

        CreateClient(TestCase tc, DB data)
        {
            this.tc = tc;
            this.data = data;
        }

        public void run()
        {
            try
            {
                data.create(new String[]{"testName0" + System.currentTimeMillis() * Math.random() * 1024, "testLocation0", "testSpecialties0", "450", "670", "testOwn0"});
                data.create(new String[]{"testName1" + System.currentTimeMillis() * Math.random() * 1024, "testLocation1", "testSpecialties1", "451", "671", "testOwn1"});
                data.create(new String[]{"testName2" + System.currentTimeMillis() * Math.random() * 1024, "testLocation2", "testSpecialties2", "452", "672", "testOwn2"});
                data.create(new String[]{"testName3" + System.currentTimeMillis() * Math.random() * 1024, "testLocation3", "testSpecialties3", "453", "673", "testOwn3"});
                data.create(new String[]{"testName4" + System.currentTimeMillis() * Math.random() * 1024, "testLocation4", "testSpecialties4", "454", "674", "testOwn4"});
                data.create(new String[]{"testName5" + System.currentTimeMillis() * Math.random() * 1024, "testLocation5", "testSpecialties5", "455", "675", "testOwn5"});
                data.create(new String[]{"testName6" + System.currentTimeMillis() * Math.random() * 1024, null, "testSpecialties6", null, "656", "testOwn6"});
            }
            catch(DuplicateKeyException dke)
            {
                StringWriter sw = new StringWriter();
                dke.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
        }
    }

    private class LockClient implements Runnable
    {
        private TestCase tc;
        private DB data;

        LockClient(TestCase tc, DB data)
        {
            this.tc = tc;
            this.data = data;
        }

        public void run()
        {
            try
            {
                int index;

                index = data.create(new String[]{"testName0" + System.currentTimeMillis() * Math.random() * 1024, "testLocation0", "testSpecialties0", "450", "670", "testOwn0"});

                tc.assertTrue("create exited with error code ", index >= 0);

                index = data.create(new String[]{"testName1" + System.currentTimeMillis() * Math.random() * 1024, "testLocation1", "testSpecialties1", "451", "671", "testOwn1"});

                tc.assertTrue("create exited with error code ", index >= 0);

                index = data.create(new String[]{"testName2" + System.currentTimeMillis() * Math.random() * 1024, "testLocation2", "testSpecialties2", "452", "672", "testOwn2"});

                tc.assertTrue("create exited with error code ", index >= 0);

                int recNo = 2;

                long lockCookie = data.lock(recNo);

                try
                {
                    Thread.sleep(10);
                }
                catch(InterruptedException ie)
                {
                    StringWriter sw = new StringWriter();
                    ie.printStackTrace(new PrintWriter(sw));
                    tc.fail(sw.toString());
                }

                data.unlock(recNo, lockCookie);
            }
            catch(RecordNotFoundException rnfe)
            {
                StringWriter sw = new StringWriter();
                rnfe.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
            catch(DuplicateKeyException dke)
            {
                StringWriter sw = new StringWriter();
                dke.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
            catch(SecurityException se)
            {
                StringWriter sw = new StringWriter();
                se.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
        }
    }

    private class UpdateClient implements Runnable
    {
        private TestCase tc;
        private DB data;

        UpdateClient(TestCase tc, DB data)
        {
            this.tc = tc;
            this.data = data;
        }

        public void run()
        {
            String[] record = new String[]{"a value that will never be a key " + System.currentTimeMillis(), "testLocationb", "testSpecialtiesb", "45b", "67b", "testOwnb"};

            try
            {
                int index;

                index = data.create(new String[]{"testName0" + System.currentTimeMillis() * Math.random() * 1024, "testLocation0", "testSpecialties0", "450", "670", "testOwn0"});

                tc.assertTrue("create exited with error code ", index >= 0);

                index = data.create(new String[]{"testName1" + System.currentTimeMillis() * Math.random() * 1024, "testLocation1", "testSpecialties1", "451", "671", "testOwn1"});

                tc.assertTrue("create exited with error code ", index >= 0);

                index = data.create(new String[]{"testName2" + System.currentTimeMillis() * Math.random() * 1024, "testLocation2", "testSpecialties2", "452", "672", "testOwn2"});

                tc.assertTrue("create exited with error code ", index >= 0);

                int recNo = 2;

                try
                {
                    Thread.sleep(10);
                }
                catch(InterruptedException ie)
                {
                    StringWriter sw = new StringWriter();
                    ie.printStackTrace(new PrintWriter(sw));
                    tc.fail(sw.toString());
                }

                long lockCookie = data.lock(recNo);

                try
                {
                    Thread.sleep(10);
                }
                catch(InterruptedException ie)
                {
                    StringWriter sw = new StringWriter();
                    ie.printStackTrace(new PrintWriter(sw));
                    tc.fail(sw.toString());
                }

                data.update(recNo, record, lockCookie);

                try
                {
                    Thread.sleep(10);
                }
                catch(InterruptedException ie)
                {
                    StringWriter sw = new StringWriter();
                    ie.printStackTrace(new PrintWriter(sw));
                    tc.fail(sw.toString());
                }

                data.unlock(recNo, lockCookie);

                try
                {
                    Thread.sleep(10);
                }
                catch(InterruptedException ie)
                {
                    StringWriter sw = new StringWriter();
                    ie.printStackTrace(new PrintWriter(sw));
                    tc.fail(sw.toString());
                }

                String[] read = data.read(recNo);

                tc.assertNotNull("Should read a record", read);
            }
            catch(RecordNotFoundException rnfe)
            {
                StringWriter sw = new StringWriter();
                rnfe.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
            catch(DuplicateKeyException dke)
            {
                StringWriter sw = new StringWriter();
                dke.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
            catch(SecurityException se)
            {
                StringWriter sw = new StringWriter();
                se.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
        }
    }

    private class FindClient implements Runnable
    {
        private TestCase tc;
        private DB data;

        FindClient(TestCase tc, DB data)
        {
            this.tc = tc;
            this.data = data;
        }

        public void run()
        {
            int[] indicies;

            indicies = data.find(new String[]{null, null, null, null, null, null});

            tc.assertNotNull("must not return null (indicating an error)", indicies);

            indicies = data.find(new String[]{"test", null, null, null, null, null});

            tc.assertNotNull("must not return null (indicating an error)", indicies);

            indicies = data.find(new String[]{"testName1", null, null, null, null, null});

            tc.assertNotNull("must not return null (indicating an error)", indicies);

            indicies = data.find(new String[]{"test", null, null, "4", null, null});

            tc.assertNotNull("must not return null (indicating an error)", indicies);
        }
    }

    private class DeleteClient implements Runnable
    {
        private TestCase tc;
        private DB data;

        DeleteClient(TestCase tc, DB data)
        {
            this.tc = tc;
            this.data = data;
        }

        public void run()
        {
            try
            {
                data.create(new String[]{"testName0" + System.currentTimeMillis() * Math.random() * 1024, "testLocation0", "testSpecialties0", "450", "670", "testOwn0"});
                data.create(new String[]{"testName1" + System.currentTimeMillis() * Math.random() * 1024, "testLocation1", "testSpecialties1", "451", "671", "testOwn1"});
                data.create(new String[]{"testName2" + System.currentTimeMillis() * Math.random() * 1024, "testLocation2", "testSpecialties2", "452", "672", "testOwn2"});
                data.create(new String[]{"testName3" + System.currentTimeMillis() * Math.random() * 1024, "testLocation3", "testSpecialties3", "453", "673", "testOwn3"});
                data.create(new String[]{"testName4" + System.currentTimeMillis() * Math.random() * 1024, "testLocation4", "testSpecialties4", "454", "674", "testOwn4"});
                data.create(new String[]{"testName5" + System.currentTimeMillis() * Math.random() * 1024, "testLocation5", "testSpecialties5", "455", "675", "testOwn5"});
                data.create(new String[]{"testName6" + System.currentTimeMillis() * Math.random() * 1024, null, "testSpecialties6", null, "656", "testOwn6"});

                int length = data.find(new String[]{null, null, null, null, null, null}).length;

                int recNo = ((int)Math.random() * length);
                long lockCookie;

                lockCookie = data.lock(recNo);

                data.delete(recNo, lockCookie);

                data.unlock(recNo, lockCookie);
            }
            catch(RecordNotFoundException rnfe)
            {
                StringWriter sw = new StringWriter();
                rnfe.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
            catch(SecurityException se)
            {
                StringWriter sw = new StringWriter();
                se.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
            catch(DuplicateKeyException dke)
            {
                StringWriter sw = new StringWriter();
                dke.printStackTrace(new PrintWriter(sw));
                tc.fail(sw.toString());
            }
        }
    }
}
