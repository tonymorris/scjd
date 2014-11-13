package suncertify.db;

import suncertify.db.server.ui.ServerFrame;
import suncertify.db.server.ui.ServerConfiguration;
import suncertify.db.server.ui.ServerConfigurationImpl;
import suncertify.db.client.DataFrame;
import suncertify.db.client.ClientConstants;
import suncertify.db.client.MiddleLocator;
import suncertify.db.client.Configuration;
import suncertify.db.client.ConfigurationImpl;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.util.Properties;

/**
 * Provides the single initial point of execution of the application (the main method).
 * The user can start the application in one of three modes:
 * <li>Server mode (by passing the {@link Constants#CLA_SERVER server command line argument}).</li>
 * <li>Client local mode (by passing the {@link Constants#CLA_ALONE client local command line argument}).</li>
 * <li>Client networked mode (by not passing any command line argument).</li>
 * <br>Any additional command line arguments are ignored.
 *
 * @see Constants#CLA_SERVER
 * @see Constants#CLA_ALONE
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Main implements Constants, ClientConstants
{
    /**
     * Begin execution of the application.
     * The user can start the application in one of three modes:
     * <li>Server mode (by passing the {@link Constants#CLA_SERVER server command line argument}).</li>
     * <li>Client local mode (by passing the {@link Constants#CLA_ALONE client local command line argument}).</li>
     * <li>Client networked mode (by not passing any command line argument).</li>
     * <br>Any additional command line arguments are ignored.
     *
     * @see Constants#CLA_SERVER
     * @see Constants#CLA_ALONE
     * @param args The command line arguments to start the application with.
     */
    public static void main(String[] args)
    {
        Configuration configuration = null;

        try
        {
            // load the configuration
            configuration = getClientConfiguration();
        }
        catch(IOException ioe)
        {
            System.err.println(ioe);
            System.exit(1);
        }
        catch(ConfigurationException ce)
        {
            System.err.println(ce);
            System.exit(2);
        }

        if(args.length == 0)
        {
            client(true, configuration);
        }
        else if(args[0].equalsIgnoreCase(CLA_ALONE))
        {
            client(false, configuration);
        }
        else if(args[0].equalsIgnoreCase(CLA_SERVER))
        {
             server();
        }
        else
        {
            System.err.println(usage());
        }
    }

    // start the server
    private static void server()
    {
        // default the look and feel to the system look and feel
        try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException cnfe)
		{
            System.err.println(cnfe);
		}
		catch (InstantiationException ie)
		{
            System.err.println(ie);
		}
		catch (IllegalAccessException iae)
		{
            System.err.println(iae);
		}
		catch (UnsupportedLookAndFeelException ulnfe)
		{
            System.err.println(ulnfe);
		}

        ServerConfiguration configuration = null;

        try
        {
            // load the configuration
            configuration = getServerConfiguration();
        }
        catch(IOException ioe)
        {
            System.err.println(ioe);
            System.exit(1);
        }
        catch(ConfigurationException ce)
        {
            System.err.println(ce);
            System.exit(2);
        }

        ServerFrame sf = new ServerFrame(configuration, "Bodgitt and Scarper, Limited Liability Company - Server Application");
        sf.pack();
        MiddleLocator locator = new MiddleLocator();
        locator.locate(sf);
        sf.show();
    }

    // start the client in either local or networked mode (depending on the passed parameter)
    private static void client(boolean networkedMode, Configuration configuration)
    {
        // default the look and feel to the system look and feel
        try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException cnfe)
		{
            System.err.println(cnfe);
		}
		catch (InstantiationException ie)
		{
            System.err.println(ie);
		}
		catch (IllegalAccessException iae)
		{
            System.err.println(iae);
		}
		catch (UnsupportedLookAndFeelException ulnfe)
		{
            System.err.println(ulnfe);
		}

        // show the user interface
        DataFrame df = new DataFrame(networkedMode, configuration, "Bodgitt and Scarper, Limited Liability Company - Client Application");
        df.pack();
        MiddleLocator locator = new MiddleLocator();
        locator.locate(df);
        df.show();
    }

    // loads suncertify.properties - returns null if there is a problem
    private static Properties getConfigurationProperties() throws IOException
    {
        File f = new File(PROPERTIES_FILE);

        if(f.exists() && !f.isDirectory() && f.canRead())
        {
            FileInputStream fis = new FileInputStream(f);
            Properties props = new Properties();
            props.load(fis);

            return props;
        }
        else
        {
            return null;
        }
    }

    // loads the data client configuration - if there is a problem, returns the default
    private static Configuration getClientConfiguration() throws IOException, ConfigurationException
    {
        Properties props = getConfigurationProperties();

        return (props == null ? new ConfigurationImpl() : new ConfigurationImpl(props));
    }

    // loads the data server configuration - if there is a problem, returns the default
    private static ServerConfiguration getServerConfiguration() throws IOException, ConfigurationException
    {
        Properties props = getConfigurationProperties();

        return (props == null ? new ServerConfigurationImpl() : new ServerConfigurationImpl(props));
    }

    private static String usage()
    {
        return "java -jar runme.jar [server | alone]";
    }
}
