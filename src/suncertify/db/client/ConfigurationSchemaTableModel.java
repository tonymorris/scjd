package suncertify.db.client;

import javax.swing.table.TableModel;

/**
 * The table model that is used in the configuration dialog to
 * allow the user to configure the schema of the data.
 *
 * @see SchemaColumn
 * @see ConfigurationSchemaTableModelImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface ConfigurationSchemaTableModel extends TableModel
{
    /**
     * Add a schema column descriptor to the table model with the given schema column.
     *
     * @param sc The schema column descriptor to add to the table model.
     */
    public void addRow(SchemaColumn sc);

    /**
     * Delete a schema column descriptor from the table at the given index in the table model.
     *
     * @param index The index at which to delete a schema column descriptor in the table model.
     */
    public void deleteRow(int index);

    /**
     * Returns the schema column descriptor and the given index in the table model.
     *
     * @param index The index at which to get the schema column descriptor.
     * @return The schema column descriptor and the given index in the table model.
     */
    public SchemaColumn getRow(int index);

    /**
     * Move the schema column descriptor at the given index up one position in the table model.
     *
     * @param index The index at which to move the schema column descriptor up one position in the table model.
     */
    public void moveUp(int index);

    /**
     * Move the schema column descriptor at the given index down one position in the table model.
     *
     * @param index The index at which to move the schema column descriptor down one position in the table model.
     */
    public void moveDown(int index);

    /**
     * Clear all schema column descriptor from the table model.
     */
    public void clear();
}
