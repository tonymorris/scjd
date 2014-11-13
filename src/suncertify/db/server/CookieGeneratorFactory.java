package suncertify.db.server;

/**
 * Forms part of a factory design pattern for generating the cookie values.
 * Provides an interface for creating an implementation of a {@link CookieGenerator CookieGenerator}.
 *
 * @see CookieGeneratorFactoryImpl
 * @see CookieGenerator
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface CookieGeneratorFactory
{
    /**
     * Create and return an instance of a {@link CookieGenerator CookieGenerator}.
     *
     * @return A created instance of a {@link CookieGenerator CookieGenerator}.
     */
    public CookieGenerator createCookieGenerator();
}
