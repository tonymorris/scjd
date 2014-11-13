package suncertify.db.client;

/**
 * Provides the interface contract for performing a search on the criteria and only displaying records
 * that match the search criteria exactly.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface ExactMatcher
{
    /**
     * Returns <code>true</code> if the given field matches the given criteria exactly, <code>false</code> otehrwise.
     *
     * @param criteria The criteria to attempt to match.
     * @param row The row to attempt to match the criteria with.
     * @return <code>true</code> if the given field matches the given criteria exactly, <code>false</code> otehrwise.
     */
    public boolean isExactMatch(String[] criteria, String[] row);
}
