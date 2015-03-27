package org.programmiersportgruppe.jirabrowser;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.function.Function;


public class BrowserViewBuilder {

    public static class ColumnModel<T, E> {
        public final String title;
        public final Function<T, E> accessor;
        public final Class<E> clazz;
        public final boolean isShort;

        public ColumnModel(String title, Function<T, E> accessor, Class<E> clazz, boolean isShort) {
            this.title = title;
            this.accessor = accessor;
            this.clazz = clazz;
            this.isShort = isShort;
        }
    }

    public static JXTable createTable(SelectionInList selectionInList, BeanTableAdapter<?> tableAdapter) {
        JXTable table = new JXTable(tableAdapter);
        Bindings.bind(table, selectionInList);
        table.setColumnControlVisible(true);
        table.setHighlighters(
                HighlighterFactory.createAlternateStriping());
        List<TableColumn> cols = table.getColumns(true);
        int i=0;
        for (ColumnModel column: tableAdapter.columns) {
            if (column.isShort) {
                cols.get(i).setMinWidth(cols.get(i).getPreferredWidth());
            }
            i++;

        }
        table.packAll();
        return table;
    }

    public JComponent buildPanel(BrowserViewModel model) {
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("fill:p:grow", "fill:p:grow"));

        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        pane.add(buildTablePanel(model));
        pane.add(buildDetailsPanel(model));

        pane.setBorder(null);

        builder.add(pane);
        builder.border(Borders.DLU4);
        return builder.build();

    }

    private JScrollPane buildDetailsPanel(BrowserViewModel model) {
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:p, 4dlu, fill:200dlu:grow"));
        ValueModel heading = ConverterFactory.combine(model.key, model.summary, (a, b) -> {
            if (a == null || b == null)
                return null;

            return a.toString() + ": " + b.toString();
        });

        JLabel headingLabel = BasicComponentFactory.createLabel(heading);
        headingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JComponent separator = new DefaultComponentFactory().createSeparator(headingLabel);
        builder.append(separator);

        DetailsBuilder detailsBuilder = new DetailsBuilder();

        detailsBuilder.textArea(model.descriptionModel, "Description");
        detailsBuilder.textArea(model.storyModel, "Story");
        detailsBuilder.textArea(model.acceptanceCriteria, "Acceptance Criteria");

        builder.appendRow("fill:p:grow");


        builder.append(detailsBuilder.build(), 3);

        JScrollPane jScrollPane = new JScrollPane(builder.build());
        jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return jScrollPane;
    }

    private JPanel buildTablePanel(BrowserViewModel model) {
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:p, 4dlu, fill:200dlu:grow"), new FormDebugPanel());
        JXTable table = createTable(model.ticketSelection, model.tableAdapter);

        builder.append("Filter", BasicComponentFactory.createTextField(model.filterQuery, false));
        builder.appendRow("fill:400dlu:grow");
        builder.append(new JScrollPane(table), 3);
        return builder.build();
    }


}
