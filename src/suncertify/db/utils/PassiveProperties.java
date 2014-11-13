package suncertify.db.utils;

import java.util.Properties;
import java.util.Enumeration;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Provides the ability of <code>java.util.Properties</code> as well as the ability to store
 * properties in a property file without overwriting properties unnecessarily.
 * For example, if a file contains the property <code>x=5</code>, and a call is made to
 * {@link #store(String, String) store(String, String)} and there is no '<code>x</code>' property
 * set, the existing value of <code>x</code> will not be overwritten.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class PassiveProperties extends Properties
{
    /**
     * Construct a default <code>PassiveProperties</code> instance.
     */
    public PassiveProperties()
    {
        super();
    }

    /**
     * Construct a <code>PassiveProperties</code> with the given set of properties.
     * After a call to {@link #store(String, String) store(String, String)}, existing properties
     * plus this given set of properties will be written. In the event of a collision, the value in the given
     * properties as the parameter to this constructor will take precedence.
     *
     * @param props the set of properties to construct this <code>PassiveProperties</code> with.
     */
    public PassiveProperties(Properties props)
    {
        Enumeration keys = props.propertyNames();

        while(keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();

            setProperty(key, props.getProperty(key));
        }
    }

    /**
     * Passively store the properties in the given file name with the given header value.
     * This method is the equivalent to calling {@link #store(File, String) store(File, String)}.
     *
     * @param filename The file name to passively store the properties in.
     * @param header The header to write to the property file.
     * @throws IOException If an I/O error occurs during reading or writing the properties file.
     */
    public void store(String filename, String header) throws IOException
    {
        store(new File(filename), header);
    }

    /**
     * Passively store the properties in the given file name with the given header value.
     *
     * @param f The file to passively store the properties in.
     * @param header The header to write to the property file.
     * @throws IOException If an I/O error occurs during reading or writing the properties file.
     */
    public void store(File f, String header) throws IOException
    {
        Properties existing = new Properties();

        if(f.exists() && !f.isDirectory() && f.canRead())
        {
            FileInputStream fis = new FileInputStream(f);
            existing.load(fis);
            fis.close();
        }

        Enumeration keys = existing.propertyNames();

        while(keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();

            if(getProperty(key) == null)
            {
                setProperty(key, existing.getProperty(key));
            }
        }

        FileOutputStream fos = new FileOutputStream(f);
        store(fos, header);
        fos.close();
    }
}
