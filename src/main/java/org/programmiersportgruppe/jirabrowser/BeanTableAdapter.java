package org.programmiersportgruppe.jirabrowser;

import com.jgoodies.binding.adapter.AbstractTableAdapter;

import static java.util.Arrays.asList;

public class BeanTableAdapter<T> extends AbstractTableAdapter<T> {
    public final BrowserViewBuilder.ColumnModel<T, ?>[] columns;

    public BeanTableAdapter(BrowserViewBuilder.ColumnModel<T, ?>... columns) {
        super(asList(columns).stream().map(c -> c.title).toArray(size -> new String[size]));
        this.columns = columns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return columns[columnIndex].accessor.apply(getRow(rowIndex));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns[columnIndex].clazz;
    }


}
