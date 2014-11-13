package suncertify.db.client;

/**
 * A basic implementation of representing a search criteria of fields to be performed against the data source.
 * Search criteria is represented by an array of <code>String</code> instances that correspond to
 * field values to search for. If the <tt>exactMatch</tt> property is set to <code>true</code>,
 * the field criteria is intended to be matched exactly.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class SearchCriteriaImpl implements SearchCriteria
{
    private String[] fields;
    private boolean exactMatch;

    /**
     * Construct a <code>SearchCriteriaImpl</code> with no fields to search for and exact matching set to <code>false</code>.
     */
    public SearchCriteriaImpl()
    {
        this(null, false);
    }

    /**
     * Construct a <code>SearchCriteriaImpl</code> with no fields to search for and the given exact matching value.
     *
     * @param exactMatch Set whether or not the search criteria is intended to be matched exactly.
     */
    public SearchCriteriaImpl(boolean exactMatch)
    {
        this(null, exactMatch);
    }

    /**
     * Construct a <code>SearchCriteriaImpl</code> with the given fields to search for and exact matching set to <code>false</code>.
     *
     * @param fields The fields to search for from the data source.
     */
    public SearchCriteriaImpl(String[] fields)
    {
        this(fields, false);
    }

    /**
     * Construct a <code>SearchCriteriaImpl</code> with the given fields to search for and the given exact matching value.
     *
     * @param fields The fields to search for from the data source.
     * @param exactMatch Set whether or not the search criteria is intended to be matched exactly.
     */
    public SearchCriteriaImpl(String[] fields, boolean exactMatch)
    {
        setFields(fields);
        setExactMatch(exactMatch);
    }

    /**
     * Return the fields property of the search criteria.
     * This implementation makes a copy of the fields data structure in order to prevent mutability from
     * client classes.
     *
     * @return The fields property of the search criteria.
     */
    public String[] getFields()
    {
        String[] row = new String[this.fields.length];

        System.arraycopy(this.fields, 0, row, 0, row.length);

        return fields;
    }

    /**
     * Sets the fields property of the search criteria.
     * This implementation makes a copy of the fields data structure in order to prevent mutability from
     * client classes.
     *
     * @param fields The new value of the fields property of the search criteria.
     */
    public void setFields(String[] fields)
    {
        if(fields == null)
        {
            fields = new String[0];
        }

        for(int i = 0; i < fields.length; i++)
        {
            if(fields[i] != null)
            {
                fields[i] = fields[i].trim();
            }
        }

        this.fields = new String[fields.length];

        System.arraycopy(fields, 0, this.fields, 0, this.fields.length);

    }

    /**
     * Returns <code>true</code> if the search criteria is intended to be matched exactly, <code>false</code> otherwise.
     *
     * @return <code>true</code> if the search criteria is intended to be matched exactly, <code>false</code> otherwise.
     */
    public boolean isExactMatch()
    {
        return exactMatch;
    }

    /**
     * Sets whether or not the search criteria is intended to be matched exactly.
     *
     * @param exactMatch The new value to determine whether or not the search criteria is intended to be matched exactly.
     */
    public void setExactMatch(boolean exactMatch)
    {
        this.exactMatch = exactMatch;
    }

    /**
     * Performs a "deep equality" comparison between this <tt>SearchCriteriaImpl</tt> and the given object.
     * If the given object is not an instance of <tt>SearchCriteriaImpl</tt>, this method will return <code>false</code>.
     * Only if the exactMatch property and the fields property are equal, will this method return <code>true</code>.
     * The fields are determined to be equal iff (if and only if) they are both equal to <code>null</code> or
     * the underlying arrays are of the same length and each element within the array is equal according to
     * the <code>String.equals(Object)</code> method.
     *
     * @param o The object to perform a "deep equality" comparison with this <tt>SearchCriteriaImpl</tt>.
     * @return <code>true</code> if this <tt>SearchCriteriaImpl</tt> is determined to be equal to the given object,
     * <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }

        if(o == null)
        {
            return false;
        }

        if(this.getClass() != o.getClass())
        {
            return false;
        }

        SearchCriteriaImpl record = (SearchCriteriaImpl)o;

        if(this.exactMatch == record.exactMatch)
        {
            if(this.fields == null && record.fields == null)
            {
                return true;
            }

            if(this.fields == null || record.fields == null)
            {
                return false;
            }

            if(this.fields.length != record.fields.length)
            {
                return false;
            }

            for(int i = 0; i < this.fields.length; i++)
            {
                if(!this.fields[i].equals(record.fields[i]))
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }

        return true;
    }


    /**
     * Returns a hash code for the benefit of of data structures that perform a "hash" on their elements
     * (such as java.util.HashMap).
     *
     * @return A hash code for the benefit of of data structures that perform a "hash" on their elements
     * (such as java.util.HashMap).
     */
    public int hashCode()
    {
        final int ODD_PRIME = 461;
        int result = 73;

        result = result * ODD_PRIME + (this.exactMatch ? 1 : 0);

        if(this.fields != null)
        {
            for(int i = 0; i < this.fields.length; i++)
            {
                result = result * ODD_PRIME + this.fields[i].hashCode();
            }
        }

        return result;
    }


    /**
     * Returns a <code>String</code> representation of this <tt>SearchCriteriaImpl</tt>.
     * The result will contain the following:
     * <li>[<i>exactMatch property</i>]</li>
     * <li>{<i>row property</i>}</li>
     *
     * <br>
     * Each element of the data array property is converted to a <code>String</code>
     * according to the <code>String.toString()</code> method.
     *
     * @return A <code>String</code> representation of this <tt>SearchCriteriaImpl</tt>.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append('[');
        sb.append(this.exactMatch);
        sb.append(']');

        if(this.fields != null)
        {
            sb.append('{');

            for(int i = 0; i < this.fields.length; i++)
            {
                sb.append(this.fields[i]);
            }

            sb.append('}');
        }

        return sb.toString();
    }
}
