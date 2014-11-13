package suncertify.db.server;

/**
 * Provides an implementation of a {@link CookieGeneratorFactory CookieGeneratorFactory} that instantiates
 * a {@link CookieGeneratorImpl CookieGeneratorImpl}.
 *
 * @see CookieGeneratorFactory
 * @see CookieGenerator
 * @see CookieGeneratorImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class CookieGeneratorFactoryImpl implements CookieGeneratorFactory
{
    /**
     * Constructs a <tt>CookieGeneratorFactoryImpl</tt> with a null implementation.
     */
    public CookieGeneratorFactoryImpl()
    {

    }

    /**
     * Returns an instance of {@link CookieGeneratorImpl CookieGeneratorImpl}.
     *
     * @return An instance of {@link CookieGeneratorImpl CookieGeneratorImpl}.
     */
    public CookieGenerator createCookieGenerator()
    {
        return new CookieGeneratorImpl();
    }
}
