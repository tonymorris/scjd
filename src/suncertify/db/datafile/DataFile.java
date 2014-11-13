package suncertify.db.datafile;

import suncertify.db.DataRecord;

import java.io.IOException;
import java.io.EOFException;

/**
 * Provides an interface for reading and writing to and from the data file.
 * This interface makes the distinction between the data file header and the records
 * that are contained within the data file.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface DataFile extends RecordIterator
{
    /**
     * Returns the underlying {@link DataFileHeader DataFileHeader} of this data file.
     * If the header value is equal to <tt>null</tt>, the header data will be read from the data file.
     * Otherwise, the "cached" header data will be returned. In order to "erase the cached header data" which
     * will force the header data to be read from the data file, call {@link #setHeader setHeader(null)}.
     *
     * @see #readHeader()
     * @return A data structure that encapsulates the header of a data file.
     * @throws IOException If an I/O error occurs when trying to read the header data from the data file.
     * If the underlying "cached" header value exists (not equal to <tt>null</tt>), then this exception will never be thrown.
     */
    public DataFileHeader getHeader() throws IOException;

    /**
     * Sets the underlying header value of the data file.
     *
     * @param header The new header data.
     */
    public void setHeader(DataFileHeader header);

    /**
     * Clears any value that has been cached in memory as a header in the data file.
     */
    public void clearCachedHeader();

    /**
     * Seeks the underlying file pointer to the given record, which is indexed from zero.
     * Any subsequent read or write calls will occur at this position in the data file.
     *
     * @param recordNumber The index (starting at zero) of the record to seek the file pointer to.
     * @throws IOException If an I/O error occurs when trying to read the header data from the data file.
     */
    public void seekRecord(int recordNumber) throws IOException;

    /**
     * Reads the header data from the data file. The file pointer will be located at the end of the header data
     * after this method has completed execution. No attempt is made to "cache" the data for further attempts
     * to access the header data.
     *
     * @see #getHeader()
     * @return The header data that was read from the data file.
     * @throws EOFException If the size and format of the header data is invalid.
     * i.e. end of file is reached before valid header data has been properly read.
     * @throws IOException If an I/O Error occurs while reading the header data.
     */
    public DataFileHeader readHeader() throws IOException;

    /**
     * Determines if the data file contains more data to be read.
     * No guarantee is made that the remaining data is, in fact, a valid record.
     *
     * @return <tt>true</tt> If the data file contains more data to be read.
     * @throws IOException If an I/O Error occurs while attempting to determine if the data file contains more data.
     */
    public boolean hasMoreData() throws IOException;

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
     * @throws EOFException If the size and format of the record data is invalid.
     * i.e. end of file is reached before a valid record has been properly read.
     * @throws IOException If an I/O Error occurs while reading the record data.
     */
    public DataRecord nextRecord() throws EOFException, IOException;

    /**
     * Writes the given header data to the beginning of the data file. After this method has completed execution,
     * the file position pointer will be at the end of the header data.
     *
     * @param header The header data to write to the data file.
     * @throws IOException If an I/O Error occurs while writing the header data.
     */
    public void writeHeader(DataFileHeader header) throws IOException;

    /**
     * Writes a record from the current file pointer position of the data file.
     *
     * @param record The record to write to the data file.
     * @throws IOException If an I/O Error occurs while writing the record data.
     */
    public void writeRecord(DataRecord record) throws IOException;

    /**
     * Returns the current file pointer offset, in bytes, of the underlying data file.
     * This is the position at which the next read or write will occur.
     *
     * @return The current file pointer offset, in bytes, of the underlying data file.
     * This is the position at which the next read or write will occur.
     * @throws IOException If an I/O Error occurs while attempting to get the underlying data file pointer offset.
     */
    public long getFilePointer() throws IOException;

    /**
     * Returns the length, in bytes, of the underlying data file.
     *
     * @see java.io.RandomAccessFile#length()
     * @return The length, in bytes, of the underlying data file.
     * @throws IOException If an I/O Error occurs while attempting to get the underlying data file length.
     */
    public long length() throws IOException;

    /**
     * Sets the record length (number of records) of the underlying data file.
     * If the number of records currently in the data file is greater than the new length,
     * the data file will be truncated. If the number of records currently in the data file is less than
     * the new length, the data file will be extended. The contents of the extended portion are undefined.
     *
     * @param totalRecords The new length of the data file, measured as records.
     * @throws IOException If an I/O Error occurs when attempting to set the new length of the data file.
     */
    public void setRecordLength(int totalRecords) throws IOException;

    /**
     * Returns the length, in records, of the data file.
     *
     * @return The length, in records, of the data file.
     * @throws IOException If an I/O Error occurs when attempting to determine the length, in records, of the data file.
     */
    public long lengthInRecords() throws IOException;

    /**
     * Closes the underlying data file and any system resources associated with it.
     * After closing, no more read or write operations can be performed.
     *
     * @throws IOException If an I/O Error occurs when attempting to close the underlying data file.
     */
    public void close() throws IOException;
}