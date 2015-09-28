package org.programmiersportgruppe.jirabrowser.misc;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.BindingConverter;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.binding.value.ValueModel;

import java.util.function.Function;


public class Converter {
    public static ValueModel convert(ValueModel source, Function<Object, Object> mapping) {

        return new ConverterValueModel(source, new BindingConverter() {
            @Override
            public Object targetValue(Object sourceValue) {
                return mapping.apply(sourceValue);
            }

            @Override
            public Object sourceValue(Object targetValue) {
                throw new UnsupportedOperationException("This is a one way converter");
            }
        });
    }
}
