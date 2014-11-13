package suncertify.db.datafile;

import java.io.IOException;

/**
 * Forms part of a Factory design pattern framework for creating a {@link DataFile DataFile} implementation.
 * Provides a concrete implementation of the factory that returns an instance of {@link DataFileImpl DataFileImpl}.
 *
 * @see DataFile
 * @see DataFileImpl
 * @see DataFileFactory
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class DataFileFactoryImpl implements DataFileFactory
{
    private String dataFilename;

    /**
     * Constructs a <tt>DataFileFactoryImpl</tt> with the file name of the underlying data file.
     *
     * @param dataFilename The file name of the underlying data file.
     */
    public DataFileFactoryImpl(String dataFilename)
    {
        this.dataFilename = dataFilename;
    }

    /**
     * Instantiates and returns an instance of a {@link DataFileImpl DataFileImpl}.
     *
     * @param mode The mode in which to open the {@link DataFileImpl DataFileImpl} with.
     * @return A new instance of a {@link DataFileImpl DataFileImpl}.
     * @throws IOException If an I/O Error occurs while instantiating the {@link DataFileImpl DataFileImpl}.
     */
    public DataFile createDataFile(String mode) throws IOException
    {
        return new DataFileImpl(dataFilename, mode);
    }
}
