package org.programmiersportgruppe.jirabrowser;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.FormLayout;
import org.programmiersportgruppe.jgoodies.misc.IsNotNullConverter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fleipold on 26/03/2015.
 */
public class DetailsBuilder {

    private final DefaultFormBuilder outerBuilder;

    public DetailsBuilder() {

        outerBuilder = new DefaultFormBuilder(new FormLayout("fill:200dlu:"));


    }

    public void textArea(ValueModel valueModel, String label) {
        //builder.appendSeparator(label);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("fill:200dlu:"));


        builder.appendSeparator(label);

        //builder.appendRow("fill:100dlu:grow");
        builder.appendRow("pref");
        JTextArea textArea = BasicComponentFactory.createTextArea(valueModel);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        builder.append(textArea);

        JPanel localPanel = builder.getPanel();
        PropertyConnector.connectAndUpdate(new IsNotNullConverter(valueModel), localPanel, "visible");

        outerBuilder.append(localPanel);
    }



    public JPanel build() {

        return outerBuilder.build();
    }
}
