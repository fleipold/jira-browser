package org.programmiersportgruppe.jirabrowser.misc;


import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import java.util.List;

public class SimpleSelection<T> {
    private ValueModel listHolder;
    private ValueModel simpleSelectionHolder;

    public SimpleSelection(ValueModel listHolder, ValueModel simpleSelectionHolder) {
        this.listHolder = listHolder;
        this.simpleSelectionHolder = simpleSelectionHolder;
    }

    public SimpleSelection() {
        this(new ValueHolder(true),new ValueHolder(true));
    }

    public ValueModel getListHolder() {
        return listHolder;
    }

    public ValueModel getSimpleSelectionHolder() {
        return simpleSelectionHolder;
    }

    public T getSelection(){
        return (T) simpleSelectionHolder.getValue();
    }

    public List<T> getList() {
        return (List<T>) listHolder.getValue();
    }
}
