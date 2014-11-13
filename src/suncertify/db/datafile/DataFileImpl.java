package suncertify.db.datafile;

import suncertify.db.DataRecord;
import suncertify.db.DataRecordImpl;

import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileDescriptor;

/**
 * Encapsulates functionality for reading and writing to and from the data file.
 * This functionality makes the distinction between the data file header and the records
 * that are contained within the data file.  The functionality is provided by "wrapping" a
 * <code>java.io.RandomAccessFile</code> and exposing an API that allows manipulation of data structures
 * that are specific to the data file.<br>
 * <u>Example:</u>
 *
<pre>
        DataFile df1 = new DataFileImpl(args[0], "r");
        DataFile df2 = new DataFileImpl(args[1], "rw");

        df2.writeHeader(df1.readHeader());

        while(df1.hasMoreRecords())
        {
            df2.writeRecord(df1.nextRecord());
        }

        df2.close();
        df1.close();
</pre>
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class DataFileImpl implements DataFile, DataFileConstants
{
    private RandomAccessFile raf;
    private DataFileHeader header;

    /**
     * Opens the underlying <code>java.io.RandomAccessFile</code> with the given file and mode.
     * <br>
     * <b><u>Valid modes.</u></b>
     * <li><b>"r"</b></li>
     * Open the data file for reading only. Invoking any of the <tt>write</tt> methods of the resulting object
     * will cause a <code>java.io.IOException</code> to be thrown.
     *
     * <li><b>"rw"</b></li>
     * Open the data file for reading and writing.  If the file does not already exist then an attempt will be made to create it.
     *
     * <li><b>"rws"</b></li>
     * Open for reading and writing, as with <tt>"rw"</tt>, and also require that every update to the file's
     * content or metadata be written synchronously to the underlying storage device.
     *
     * <li><b>"rwd"</b></li>
     * Open the data file for reading and writing, as with <tt>"rw"</tt>, and also require that every update to the file's
     * content be written synchronously to the underlying storage device.
     *
     * @see java.io.RandomAccessFile#RandomAccessFile(String name, String mode)
     * @param name The name of the data file to open.
     * @param mode The mode to open the data file with.
     * @throws FileNotFoundException If the file exists but is a directory rather than a regular file,
     * or cannot be opened or created for any other reason.
     * @throws IllegalArgumentException If the mode argument is not equal to one of
     * <tt>"r"</tt>, <tt>"rw"</tt>, <tt>"rws"</tt>, or <tt>"rwd"</tt>.
     * @throws SecurityException If a security manager exists and it's <code>checkRead</code> method denies read
     * access to the file or the mode is "rw" and the security manager's <code>checkWrite</code> method denies write access to the file.
     */
    public DataFileImpl(String name, String mode) throws FileNotFoundException, IllegalArgumentException, SecurityException
    {
        raf = new RandomAccessFile(name, mode);
    }

    /**
     * Opens the underlying <code>java.io.RandomAccessFile</code> with the given file and mode.
     * <br>
     * <b><u>Valid modes.</u></b>
     * <li><b>"r"</b></li>
     * Open the data file for reading only. Invoking any of the <tt>write</tt> methods of the resulting object
     * will cause a <code>java.io.IOException</code> to be thrown.
     *
     * <li><b>"rw"</b></li>
     * Open the data file for reading and writing.  If the file does not already exist then an attempt will be made to create it.
     *
     * <li><b>"rws"</b></li>
     * Open for reading and writing, as with <tt>"rw"</tt>, and also require that every update to the file's
     * content or metadata be written synchronously to the underlying storage device.
     *
     * <li><b>"rwd"</b></li>
     * Open the data file for reading and writing, as with <tt>"rw"</tt>, and also require that every update to the file's
     * content be written synchronously to the underlying storage device.
     *
     * @see java.io.RandomAccessFile#RandomAccessFile(String name, String mode)
     * @param file The file to open.
     * @param mode The mode to open the data file with.
     * @throws FileNotFoundException If the file exists but is a directory rather than a regular file,
     * or cannot be opened or created for any other reason.
     * @throws IllegalArgumentException If the mode argument is not equal to one of
     * <tt>"r"</tt>, <tt>"rw"</tt>, <tt>"rws"</tt>, or <tt>"rwd"</tt>.
     * @throws SecurityException If a security manager exists and it's <code>checkRead</code> method denies read
     * access to the file or the mode is "rw" and the security manager's <code>checkWrite</code> method denies write access to the file.
     */
    public DataFileImpl(File file, String mode) throws FileNotFoundException, IllegalArgumentException, SecurityException
    {
        raf = new RandomAccessFile(file, mode);
    }

    /**
     * Returns the underlying {@link DataFileHeader DataFileHeader} of this data file.
     * If the header value is equal to <tt>null</tt>, the header data will be read from the data file.
     * Otherwise, the "cached" header data will be returned. In order to "erase the cached header data" which
     * will force the header data to be read from the data file, call {@link #setHeader setHeader(null)}.
     *
     * @return A data structure that encapsulates the header of a data file.
     * @throws IOException If an I/O error occurs when trying to read the header data from the data file.
     * If the underlying "cached" header value exists (not equal to <tt>null</tt>), then this exception will never be thrown.
     */
    public DataFileHeader getHeader() throws IOException
    {
        // if there is a header in cache, return it
        if(this.header != null)
        {
            return this.header;
        }

        // There is no header in cache, create in from the data file
        DataFileHeader header = readHeader();

        // Cache the new header
        this.header = header;

        return header;
    }

    /**
     * Sets the underlying header value of the data file. This method will not write anything to the file, but will update
     * any "cached" header data value. Passing a <tt>null</tt> argument will "clear the cached header data" and any subsequent
     * calls that require the header data will force the reading of the header data from the file, as opposed to using the "cached" value.
     *
     * @param header The new header data. A value of <tt>null</tt> will "clear the cached header data" and any subsequent
     * calls that require the header data will force the reading of the header data from the file, as opposed to using the "cached" value.
     */
    public void setHeader(DataFileHeader header)
    {
        this.header = header;
    }

    /**
     * Clears any value that has been cached in memory as a header in the data file. Any subsequent calls that require the reading
     * of the data file will be read directly from the file, as opposed to using a cached value. That is, a cached value
     * will be used if there is one and calling this method removes any cached value.
     */
    public void clearCachedHeader()
    {
        this.header = null;
    }

    /**
     * Seeks the underlying file pointer to the given record, which is indexed from zero.
     * Any subsequent read or write calls will occur at this position in the data file.
     * If no header data has been previously "cached", the method will read the header data from the data file,
     * rather than use the "cached" value.
     *
     * @param recordNumber The index (starting at zero) of the record to seek the file pointer to.
     * @throws IOException If an I/O error occurs when trying to read the header data from the data file.
     * If the underlying "cached" header value exists (not equal to <tt>null</tt>), then this exception will never be thrown.
     */
    public void seekRecord(int recordNumber) throws IOException
    {
        DataFileHeader header = getHeader();

        raf.seek(header.getDataOffset() + recordNumber * header.recordLength());
    }

    /**
     * Reads the header data from the data file. The file pointer will be located at the end of the header data
     * after this method has completed execution. No attempt is made to "cache" the data for further attempts
     * to access the header data.
     *
     * @return The header data that was read from the data file.
     * @throws EOFException If the size and format of the header data is invalid.
     * i.e. end of file is reached before valid header data has been properly read.
     * @throws IOException If an I/O Error occurs while reading the header data.
     */
    public DataFileHeader readHeader() throws IOException
    {
        DataFileHeader header = new DataFileHeader();

        raf.seek(0);

        header.setMagicNumber(raf.readInt());
        header.setDataOffset(raf.readInt());
        header.setTotalFields(raf.readShort());

        FieldSchema[] schema = new FieldSchemaImpl[header.getTotalFields()];

        for(int i = 0; i < schema.length; i++)
        {
            short s = raf.readShort();

            if(s > 0)
            {
                byte[] b = new byte[s];
                raf.readFully(b);

                schema[i] = new FieldSchemaImpl(new String(b), raf.readShort());
            }
        }

        header.setSchema(schema);

        return header;
    }

    /**
     * Determines if the data file contains more data to be read. This is determined by comparing the size of
     * the file with the position of the file pointer. No guarantee is made that the remaining data is, in fact, a valid record.
     *
     * @return <tt>true</tt> If the data file contains more data to be read.
     * @throws IOException If an I/O Error occurs while attempting to determine the file pointer position or the
     * length of the data file.
     */
    public boolean hasMoreData() throws IOException
    {
        return (raf.getFilePointer() < raf.length());
    }

    /**
     * Determines if the data file contains at least one more record to be read. This is determined by comparing the size of
     * the file with the position of the file pointer. A data file has more records if the number of bytes remaining,
     * plus the length of a record (determined with {@link DataFileHeader#recordLength() DataFileHeader#recordLength()}
     * is less than or equal to the total number of bytes in the data file.
     * If no header data has been previously "cached", the method will read the header data from the data file,
     * rather than use the "cached" value.
     *
     * @return <tt>true</tt> If the data file contains more data to be read.
     * @throws IOException If an I/O Error occurs while attempting to determine the file pointer position, the
     * length of the data file or attempting to get the header data of the data file.
     */
    public boolean hasMoreRecords() throws IOException
    {
        DataFileHeader header = getHeader();

        return ((raf.getFilePointer() + header.recordLength()) <= raf.length());
    }

    /**
     * Reads and returns a record from the current file pointer position of the data file.
     * If no header data has been previously "cached", the method will read the header data from the data file,
     * rather than use the "cached" value.
     *
     * @return A record from the current file pointer position of the data file.
     * @throws EOFException If the size and format of the record data is invalid.
     * i.e. end of file is reached before a valid record has been properly read.
     * @throws IOException If an I/O Error occurs while reading the record data.
     */
    public DataRecord nextRecord() throws EOFException, IOException
    {
        DataFileHeader header = getHeader();

        DataRecord record = new DataRecordImpl();

        char flag = raf.readChar();

        if(flag == VALID_RECORD)
        {
            record.setDeleted(false);
        }
        else if(flag == DELETED_RECORD)
        {
            record.setDeleted(true);
        }
        else
        {
            StringBuffer message = new StringBuffer();
            message.append("Invalid DataRecord Deleted Flag: ");
            message.append('[');
            message.append(flag);
            message.append(" != " + VALID_RECORD);
            message.append(" && ");
            message.append(flag);
            message.append(" != " + DELETED_RECORD);
            message.append(']');

            throw new IOException(message.toString());
        }

        String[] fields = new String[header.getTotalFields()];

        for(short s = 0; s < fields.length; s++)
        {
            byte[] data = new byte[header.getSchema()[s].getLength()];

            raf.readFully(data);

            fields[s] = new String(data);
        }

        record.setData(fields);

        return record;
    }

    /**
     * Writes the given header data to the beginning of the data file. After this method has completed execution,
     * the file position pointer will be at the end of the header data.
     *
     * @param header The header data to write to the data file.
     * @throws IOException If an I/O Error occurs while writing the header data.
     */
    public void writeHeader(DataFileHeader header) throws IOException
    {
        raf.seek(0);

        raf.writeInt(header.getMagicNumber());
        raf.writeInt(header.getDataOffset());
        raf.writeShort(header.getTotalFields());

        FieldSchema[] schema = header.getSchema();

        for(int i = 0; i < schema.length; i++)
        {
            raf.writeShort(schema[i].getName().length());
            raf.write(schema[i].getName().getBytes());
            raf.writeShort(schema[i].getLength());
        }

        this.header = header;
    }

    /**
     * Writes a record from the current file pointer position of the data file.
     *
     * @param record The record to write to the data file.
     * @throws IOException If an I/O Error occurs while writing the record data.
     */
    public void writeRecord(DataRecord record) throws IOException
    {
        raf.writeChar(record.isDeleted() ? DELETED_RECORD : VALID_RECORD);

        DataFileHeader header = getHeader();

        String[] fields = record.getData();

        for(short s = 0; s < fields.length; s++)
        {
            byte[] field = new byte[header.getSchema()[s].getLength()];

            for(int i = 0; i < field.length; i++)
            {
                field[i] = RECORD_PADDING;
            }

            if(fields[s] != null)
            {
                for(int i = 0; i < field.length && i < fields[s].length(); i++)
                {
                    field[i] = (byte)fields[s].charAt(i);
                }
            }

            raf.write(field);
        }
    }

    /**
     * Returns the opaque file descriptor object associated with the underlying <code>java.io.RandomAccessFile</code>.
     *
     * @see java.io.RandomAccessFile#getFD()
     * @return The opaque file descriptor object associated with the underlying <code>java.io.RandomAccessFile</code>.
     * @throws IOException If an I/O Error occurs while attempting to get the underlying file descriptor.
     */
    public FileDescriptor getFD() throws IOException
    {
        return raf.getFD();
    }

    /**
     * Returns the current file pointer offset, in bytes, of the underlying <code>java.io.RandomAccessFile</code>.
     * This is the position at which the next read or write will occur.
     *
     * @see java.io.RandomAccessFile#getFilePointer()
     * @return The current file pointer offset, in bytes, of the underlying <code>java.io.RandomAccessFile</code>.
     * This is the position at which the next read or write will occur.
     * @throws IOException If an I/O Error occurs while attempting to get the underlying file pointer offset.
     */
    public long getFilePointer() throws IOException
    {
        return raf.getFilePointer();
    }

    /**
     * Returns the length, in bytes, of the underlying <code>java.io.RandomAccessFile</code>.
     *
     * @see java.io.RandomAccessFile#length()
     * @return The length, in bytes, of the underlying <code>java.io.RandomAccessFile</code>.
     * @throws IOException If an I/O Error occurs while attempting to get the underlying file length.
     */
    public long length() throws IOException
    {
        return raf.length();
    }

    /**
     * Sets the record length (number of records) of the underlying <code>java.io.RandomAccessFile</code>.
     * If the number of records currently in the data file is greater than the new length,
     * the data file will be truncated. If the number of records currently in the data file is less than
     * the new length, the data file will be extended. The contents of the extended portion are undefined.
     * If no header data has been previously "cached", the method will read the header data from the data file,
     * rather than use the "cached" value.
     *
     * @see java.io.RandomAccessFile#setLength(long newLength)
     * @param totalRecords The new length of the data file, measured as records.
     * @throws IOException If an I/O Error occurs when attempting to set the new length of the data file.
     */
    public void setRecordLength(int totalRecords) throws IOException
    {
        DataFileHeader header = getHeader();

        raf.setLength(header.getDataOffset() + totalRecords * header.recordLength());
    }

    /**
     * Returns the length, in records, of the data file.
     *
     * @return The length, in records, of the data file.
     * @throws IOException If an I/O Error occurs when attempting to determine the length, in records, of the data file.
     */
    public long lengthInRecords() throws IOException
    {
        DataFileHeader header = getHeader();

        return ((raf.length() - header.getDataOffset()) / header.recordLength());
    }

    /**
     * Closes the underlying <code>java.io.RandomAccessFile</code> and any system resources associated with it.
     * After closing, no more read or write operations can be performed.
     *
     * @see java.io.RandomAccessFile#close()
     * @throws IOException If an I/O Error occurs when attempting to close the underlying <code>java.io.RandomAccessFile</code>.
     */
    public void close() throws IOException
    {
        raf.close();
    }
}
