package suncertify.db.datafile;

import suncertify.db.DataRecord;

import java.io.IOException;

/**
 * Provides a method of iterating data records in the data file in an implementation-independant manner.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface RecordIterator
{
    /**
     * Determines if the data file contains at least one more record to be read.
     *
     * @return <tt>true</tt> If the data file contains enough data for one data record to be read, <code>false</code> otherwise.
     * @throws IOException If an I/O Error occurs while attempting to determine if the data file contains more records.
     */
    public boolean hasMoreRecords() throws IOException;

    /**
     * Reads and returns a record from the current file pointer position of the data file.
     * The data file pointer will be shifted along to end of the data record that was just read.
     *
     * @return A record from the current file pointer position of the data file.
     * @throws IOException If an I/O Error occurs while reading the record data.
     */
    public DataRecord nextRecord() throws IOException;
}
