package suncertify.db.client;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

/**
 * An implementation of the table model that is used in the configuration dialog to
 * allow the user to configure the schema of the data.
 *
 * @see SchemaColumn
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class ConfigurationSchemaTableModelImpl extends AbstractTableModel implements ConfigurationSchemaTableModel
{
    private final String[] COLUMN_NAMES = {"Key", "Display Name", "Length"};
    private List[] data;

    /**
     * Construct a <code>ConfigurationSchemaTableModelImpl</code> with an empty data model.
     */
    public ConfigurationSchemaTableModelImpl()
    {
        data = new ArrayList[COLUMN_NAMES.length];

        for(int i = 0; i < data.length; i++)
        {
            data[i] = new ArrayList();
        }
    }

    /**
     * Return the number of columns in the schema table model.
     * This will always return three (3), each column representing a property of
     * a {@link SchemaColumn SchemaColumn}.

     * @return The number of schema columns in the schema table model.
     */
    public int getColumnCount()
    {
        return COLUMN_NAMES.length;
    }

    /**
     * Return the number of schema column descriptors (each represented by a row) in the schema table model.
     *
     * @return The number of schema column descriptors (each represented by a row) in the schema table model.
     */
    public int getRowCount()
    {
        return data[0].size();
    }

    /**
     * Returns the value at the given row index and column index in the schema table model.
     *
     * @param row The row index at which to retrieve the value for.
     * @param col The column index at which to retrieve the value for.
     * @return The value at the given row index and column index in the schema table model.
     */
    public Object getValueAt(int row, int col)
    {
        return data[col].get(row);
    }

    /**
     * Determines if the table model cell at the given row index and column index is editable.
     *
     * @param row The row index in the table model at which to determine if the table cell is editable.
     * @param col The column index in the table model at which to determine if the table cell is editable.
     * @return <code>true</code> if the table model cell at the given row index and column index is editable, <code>false</code> otherwise.
     */
    public boolean isCellEditable(int row, int col)
    {
        return true;
    }

    /**
     * Returns the name of the column at the given column index (indexed from 0) in the schema table model.
     *
     * @param col The index of the column to retrieve the name for.
     * @return The name of the column at the given column index in the schema table model.
     */
    public String getColumnName(int col)
    {
        return COLUMN_NAMES[col];
    }

    /**
     * Returns the class type of the column at the given index in the schema table model.
     *
     * @param col The index of the column at which to retrieve the class type for.
     * @return The class type of the column at the given index in the schema table model.
     */
    public Class getColumnClass(int col)
    {
        return getValueAt(0, col).getClass();
    }

    /**
     * Sets the value at the given row index and column index to the given value in the table model.
     *
     * @param value The new value to set.
     * @param row The row index at which to set the new value.
     * @param col The column index at which to set the new value.
     */
    public void setValueAt(Object value, int row, int col)
    {
        if(row < data[col].size())
        {
            data[col].set(row, value);
        }

        fireTableCellUpdated(row, col);
    }

    /**
     * Add a schema column descriptor to the table model with the given schema column.
     *
     * @param sc The schema column descriptor to add to the table model.
     */
    public void addRow(SchemaColumn sc)
    {
        data[0].add(new Boolean(sc.isKey()));
        data[1].add(sc.getDisplayName());
        data[2].add(String.valueOf(sc.getLength()));

        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    /**
     * Delete a schema column descriptor from the table at the given index in the table model.
     *
     * @param index The index at which to delete a schema column descriptor in the table model.
     */
    public void deleteRow(int index)
    {
        data[0].remove(index);
        data[1].remove(index);
        data[2].remove(index);

        fireTableRowsDeleted(index, index);
    }

    /**
     * Returns the schema column descriptor and the given index in the table model.
     *
     * @param index The index at which to get the schema column descriptor.
     * @return The schema column descriptor and the given index in the table model.
     */
    public SchemaColumn getRow(int index)
    {
        boolean key = ((Boolean)data[0].get(index)).booleanValue();
        String displayName = (String)data[1].get(index);
        short length = Short.parseShort(((String)data[2].get(index)));

        return new SchemaColumnImpl(key, displayName, length);
    }

    /**
     * Move the schema column descriptor at the given index up one position in the table model.
     *
     * @param index The index at which to move the schema column descriptor up one position in the table model.
     */
    public void moveUp(int index)
    {
        Object temp0 = data[0].get(index - 1);
        Object temp1 = data[1].get(index - 1);
        Object temp2 = data[2].get(index - 1);

        data[0].set(index - 1, data[0].get(index));
        data[1].set(index - 1, data[1].get(index));
        data[2].set(index - 1, data[2].get(index));

        data[0].set(index, temp0);
        data[1].set(index, temp1);
        data[2].set(index, temp2);

        fireTableRowsUpdated(index - 1, index);
    }

    /**
     * Move the schema column descriptor at the given index down one position in the table model.
     *
     * @param index The index at which to move the schema column descriptor down one position in the table model.
     */
    public void moveDown(int index)
    {
        Object temp0 = data[0].get(index + 1);
        Object temp1 = data[1].get(index + 1);
        Object temp2 = data[2].get(index + 1);

        data[0].set(index + 1, data[0].get(index));
        data[1].set(index + 1, data[1].get(index));
        data[2].set(index + 1, data[2].get(index));

        data[0].set(index, temp0);
        data[1].set(index, temp1);
        data[2].set(index, temp2);

        fireTableRowsUpdated(index, index + 1);
    }

    /**
     * Clear all schema column descriptor from the table model.
     */
    public void clear()
    {
        int size = data[0].size();

        for(int i = 0; i < data.length; i++)
        {
            data[i].clear();
        }

        fireTableRowsDeleted(0, size);
    }
}
