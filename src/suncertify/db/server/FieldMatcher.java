package suncertify.db.server;

/**
 * Provides an interface for determining a "match" on a data field given matching criteria.
 *
 * @see FieldMatcherImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface FieldMatcher
{
    /**
     * Returns <code>true</code> if the given field matches the given criteria, <code>false</code> otherwise.
     *
     * @param field The field to attempt to match.
     * @param criteria The criteria to attempt to match the field with.
     * @return <code>true</code> if the field matches the criteria, <code>false</code> otherwise.
     */
    public boolean matches(String field, String criteria);
}
