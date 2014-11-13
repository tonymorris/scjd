package suncertify.db.datafile;

import java.io.IOException;

/**
 * Forms part of a Factory design pattern framework for creating a {@link DataFile DataFile} implementation.
 * Implementers of this interface will return an instance of the {@link DataFile DataFile} implementation.
 *
 * @see DataFile
 * @see DataFileFactoryImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface DataFileFactory
{
    /**
     * Instantiates and returns an instance of a {@link DataFile DataFile} implementation.
     *
     * @param mode The mode in which to open the {@link DataFile DataFile} with.
     * @return A new instance of a {@link DataFile DataFile} implementation.
     * @throws IOException If an I/O Error occurs while instantiating the {@link DataFile DataFile} implementation.
     */
    public DataFile createDataFile(String mode) throws IOException;
}
