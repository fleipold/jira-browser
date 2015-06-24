package org.programmiersportgruppe.jirabrowser;

import com.jgoodies.binding.adapter.AbstractTableAdapter;

import java.util.function.Function;

import static java.util.Arrays.asList;

public class BeanTableAdapter<T> extends AbstractTableAdapter<T> {
    public final BrowserViewBuilder.ColumnModel<T, ?>[] columns;

    private Function<T, String> externalStringRenderer = null;

    public BeanTableAdapter(BrowserViewBuilder.ColumnModel<T, ?>... columns) {
        super(asList(columns).stream().map(c -> c.title).toArray(size -> new String[size]));
        this.columns = columns;
    }


    public BeanTableAdapter<T> externalStringRenderer(Function<T, String> renderer) {
        this.externalStringRenderer = renderer;
        return this;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return columns[columnIndex].accessor.apply(getRow(rowIndex));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns[columnIndex].clazz;
    }


    public String getExternalString(int row) {
        if (externalStringRenderer != null) {
            return externalStringRenderer.apply(getRow(row));
        }
        else return "...";
    }

    public String getExternalHtmlRep(int row) {
        return "";
    }
}
