package suncertify.db.server;

/**
 * An implementation of the {@link FieldMatcher FieldMatcher} interface that matches
 * fields according to the following specification:
 * <li>if the criteria is equal to <code>null</code>, return <code>true</code>.
 * <li>if the field is equal to <code>null</code>, return <code>false</code>.
 * <li>if the field starts with the criteria text, return <code>true</code>.
 * <li>otherwise, return <code>false</code>.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class FieldMatcherImpl implements FieldMatcher
{
    /**
     * Constructs a <tt>FieldMatcherImpl</tt> with a null implementation.
     */
    public FieldMatcherImpl()
    {

    }

    /**
     * Matches fields according to the following specification:
     * <li>if the criteria is equal to <code>null</code>, return <code>true</code>.
     * <li>if the field is equal to <code>null</code>, return <code>false</code>.
     * <li>if the field starts with the criteria text, return <code>true</code>.
     * <li>otherwise, return <code>false</code>.
     *
     * @param field The to attempt to match.
     * @param criteria The criteria to attempt to match the field with.
     * @return <code>true</code> if the field matches the criteria, <code>false</code> otherwise.
     */
    public boolean matches(String field, String criteria)
    {
        if(criteria == null)
        {
            return true;
        }

        if(field == null)
        {
            return false;
        }

        return(field.startsWith(criteria));
    }
}
