package suncertify.db.server;

import suncertify.db.DataRecord;

/**
 * Provides an interface to determine if a data file record matches given criteria.
 *
 * @see RecordMatcherImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface RecordMatcher
{
    /**
     * Returns <code>true</code> if the given record matches the given criteria, <code>false</code> otherwise.
     *
     * @param rec The data record to attempt to match with the criteria.
     * @param criteria The criteria to attempt to match the data record with.
     * @return <code>true</code> if the given record matches the given criteria, <code>false</code> otherwise.
     */
    public boolean matches(DataRecord rec, String[] criteria);
}
