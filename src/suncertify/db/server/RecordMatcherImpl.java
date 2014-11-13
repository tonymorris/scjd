package suncertify.db.server;

import suncertify.db.DataRecord;

/**
 * An implementation of {@link RecordMatcher RecordMatcher} that matches records by ensuring
 * that all fields match using the {@link FieldMatcherImpl FieldMatcherImpl} implementation class.
 *
 * @see RecordMatcherFactory
 * @see RecordMatcherFactoryImpl
 * @see FieldMatcher
 * @see FieldMatcherImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class RecordMatcherImpl implements RecordMatcher
{
    private FieldMatcher matcher;

    /**
     * Constructs a <tt>RecordMatcherImpl</tt> with an underlying {@link FieldMatcherImpl FieldMatcherImpl}.
     */
    public RecordMatcherImpl()
    {
        matcher = new FieldMatcherImpl();
    }

    /**
     * Returns <code>true</code> if all the fields of the data record match according to the implementation
     * of {@link FieldMatcherImpl FieldMatcherImpl}, <code>false</code> otherwise.
     * Also ensures that the criteria length and the records' underlying data have the same length; if not,
     * <code>false</code> is returned.
     *
     * @param rec The record to attempt to match with the given criteria.
     * @param criteria The criteria to attempt to match the record with.
     * @return <code>true</code> if all the fields of the data record match according to the implementation
     * of {@link FieldMatcherImpl FieldMatcherImpl}, <code>false</code> otherwise. if the criteria length
     * and the records' underlying data do not have the same length, <code>false</code> is returned.
     */
    public boolean matches(DataRecord rec, String[] criteria)
    {
        String[] data = rec.getData();

        if(data.length != criteria.length)
        {
            return false;
        }

        for(int i = 0; i < data.length; i++)
        {
            if(!matcher.matches(data[i], criteria[i]))
            {
                return false;
            }
        }

        return true;
    }
}
