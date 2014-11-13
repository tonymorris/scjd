package suncertify.db.server;

/**
 * An implementation of a {@link RecordMatcherFactory RecordMatcherFactory} that returns
 * a concrete implementation of {@link RecordMatcher RecordMatcher}.
 * The concrete implementation is a {@link RecordMatcherImpl RecordMatcherImpl}.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class RecordMatcherFactoryImpl implements RecordMatcherFactory
{
    /**
     * Constructs a <tt>RecordMatcherFactoryImpl</tt> with a null implementation.
     */
    public RecordMatcherFactoryImpl()
    {

    }

    /**
     * Creates and returns an instance of {@link RecordMatcherImpl RecordMatcherImpl}.
     *
     * @return An instance of {@link RecordMatcherImpl RecordMatcherImpl}.
     */
    public RecordMatcher createRecordMatcher()
    {
        return new RecordMatcherImpl();
    }
}
