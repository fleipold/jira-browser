package org.programmiersportgruppe.jirabrowser;


import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import java.beans.PropertyChangeListener;
import java.util.function.BiFunction;

public class ConverterFactory {
    public static ValueModel combine(ValueModel a, ValueModel b, BiFunction<Object, Object, Object> function) {
        ValueHolder result = new ValueHolder(function.apply(a.getValue(), b.getValue()));
        PropertyChangeListener listener = evt -> result.setValue(function.apply(a.getValue(), b.getValue()));

        a.addValueChangeListener(listener);
        b.addValueChangeListener(listener);

        return result;
    }
}
