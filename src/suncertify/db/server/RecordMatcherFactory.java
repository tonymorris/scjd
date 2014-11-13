package suncertify.db.server;

/**
 * Forms part of a factory design pattern for providing a data record matching implementation.
 * Implementers will provide a factory that returns a concrete implementation of matching data records
 * with certain criteria.
 *
 * @see RecordMatcherFactoryImpl
 * @see RecordMatcher
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface RecordMatcherFactory
{
    /**
     * Create and return an instance of a {@link RecordMatcher RecordMatcher}.
     *
     * @return A created instance of a {@link RecordMatcher RecordMatcher}.
     */
    public RecordMatcher createRecordMatcher();
}
