package suncertify.db.client;

/**
 * An implementation for performing a search on the criteria and only displaying records
 * that match the search criteria exactly.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class ExactMatcherImpl implements ExactMatcher
{
    /**
     * Returns <code>true</code> if the given field matches the given criteria exactly, <code>false</code> otehrwise.
     * If either of the given parameters are equal to <code>null</code> or they do not have the same length,
     * <code>false</code> is returned. If an element in the criteria or the row is assigned to <code>null</code>,
     * or if the criteria element is an empty <code>String</code>, it is ignored in determining if there is a match.
     *
     * @param criteria The criteria to attempt to match.
     * @param row The row to attempt to match the criteria with.
     * @return <code>true</code> if the given field matches the given criteria exactly, <code>false</code> otehrwise.
     */
    public boolean isExactMatch(String[] criteria, String[] row)
    {
        if(row == null || criteria == null || criteria.length != row.length)
        {
            return false;
        }

        for(int i = 0; i < row.length; i++)
        {
            if(criteria[i] != null && row[i] != null && criteria[i].length() > 0 && !criteria[i].equals(row[i].trim()))
            {
                return false;
            }
        }

        return true;
    }
}
