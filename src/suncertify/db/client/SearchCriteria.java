package suncertify.db.client;

/**
 * Represents the possible search criteria for data from the data source.
 * Search criteria is represented by an array of <code>String</code> instances that correspond to
 * field values to search for. If the <tt>exactMatch</tt> property is set to <code>true</code>,
 * the field criteria is intended to be matched exactly.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface SearchCriteria
{
    /**
     * Return the fields property of the search criteria. Implementers may or may not maintain a copy of the fields data structure
     * in order to prevent mutability from client classes. These implementers are required to document this behaviour
     * if this is the case.
     *
     * @return The fields property of the search criteria.
     */
    public String[] getFields();

    /**
     * Sets the fields property of the search criteria. Implementers may or may not maintain a copy of the fields data structure
     * in order to prevent mutability from client classes. These implementers are required to document this behaviour
     * if this is the case.
     *
     * @param fields The new value of the fields property of the search criteria.
     */
    public void setFields(String[] fields);

    /**
     * Returns <code>true</code> if the search criteria is intended to be matched exactly, <code>false</code> otherwise.
     *
     * @return <code>true</code> if the search criteria is intended to be matched exactly, <code>false</code> otherwise.
     */
    public boolean isExactMatch();

    /**
     * Sets whether or not the search criteria is intended to be matched exactly.
     *
     * @param exactMatch The new value to determine whether or not the search criteria is intended to be matched exactly.
     */
    public void setExactMatch(boolean exactMatch);
}
