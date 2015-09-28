package org.programmiersportgruppe.jirabrowser;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.BindingConverter;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.programmiersportgruppe.jgoodies.listadapter.MultiSelectionInList;
import org.programmiersportgruppe.jirabrowser.misc.DoubleClickAdapter;
import org.programmiersportgruppe.jirabrowser.misc.SimpleSelection;
import org.programmiersportgruppe.jirabrowser.misc.TableTransferHandler;

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

    public static<T> JXTable createTable(SimpleSelection simpleSelection, BeanTableAdapter<T> tableAdapter) {
        JXTable table = new JXTable(tableAdapter);
        table.setDragEnabled(true);

        table.setTransferHandler(new TableTransferHandler(tableAdapter));

        SelectionInList<Object> selectionInList = new SelectionInList<>();

        selectionInList.setList((List<Object>) simpleSelection.getListHolder().getValue());

        simpleSelection.getListHolder().addValueChangeListener(evt ->
            selectionInList.setList((List<Object>) simpleSelection.getListHolder().getValue())
        );

        selectionInList.getSelectionIndexHolder().addValueChangeListener(evt -> {
                Integer rawSelectionIndex = selectionInList.getSelectionIndex();
                if (rawSelectionIndex == -1) {
                    simpleSelection.getSimpleSelectionHolder().setValue(null);
                    return;
                }
                int newIndex = table.convertRowIndexToModel(rawSelectionIndex);
                Object selectedValue = simpleSelection.getList().get(newIndex);
                simpleSelection.getSimpleSelectionHolder().setValue(selectedValue);
            }
        );

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

        pane.setBorder(Borders.DLU4);
        return pane;
    }

    private JComponent buildDetailsPanel(BrowserViewModel model) {
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:p, 4dlu, fill:300dlu:grow"));
        ValueModel heading = ConverterFactory.combine(model.key, model.summary, (a, b) -> {
            if (a == null || b == null)
                return null;

            return a.toString() + ": " + b.toString();
        });

        JLabel headingLabel = BasicComponentFactory.createLabel(heading);

        headingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JComponent separator = new DefaultComponentFactory().createSeparator(headingLabel);
        builder.append(separator,3);

        TicketViewBuilder ticketViewBuilder = new TicketViewBuilder();

        ticketViewBuilder.textArea(model.labels, "Labels");
        ticketViewBuilder.textArea(model.descriptionModel, "Description");
        ticketViewBuilder.textArea(model.storyModel, "Story");
        ticketViewBuilder.textArea(model.acceptanceCriteria, "Acceptance Criteria");

        builder.appendRow("fill:p:grow");

        builder.append(ticketViewBuilder.build(), 3);

        JScrollPane jScrollPane = new JScrollPane(builder.build());
        jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return jScrollPane;
    }

    private JPanel buildTablePanel(BrowserViewModel model) {
        DefaultFormBuilder outerBuilder = new DefaultFormBuilder(new FormLayout("fill:300dlu:grow"));

        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:p, 4dlu, fill:200dlu:grow"));

        JXTable table = createTable(model.ticketSelection, model.tableAdapter);

        table.addMouseListener(new DoubleClickAdapter(model.openBrowserAction));

        builder.appendSeparator("Filter");
        builder.append("&Text", BasicComponentFactory.createTextField(model.filterQuery, false));
        builder.appendRow("top:p");
        builder.append("&Status", createMultiCheckbox(model.statusFilter));
        builder.appendRelatedComponentsGapRow();

        outerBuilder.append(builder.build());
        outerBuilder.appendRow("fill:400dlu:grow");
        outerBuilder.append(new JScrollPane(table));
        return outerBuilder.build();
    }

    private Component createMultiCheckbox(MultiSelectionInList multiSelectionInList) {
        DefaultFormBuilder checkboxBuilder = new DefaultFormBuilder(new FormLayout("left:p,4dlu,left:p,4dlu,left:p"));
        for (int i=0; i< multiSelectionInList.getList().getSize(); i++){
            ValueHolder value = new ValueHolder(false);
            Object currentListElement = multiSelectionInList.getList().getElementAt(i);
            if (multiSelectionInList.getSelection().contains(currentListElement)) {
                value.setValue(true);
            }

            checkboxBuilder.append(BasicComponentFactory.createCheckBox(value, currentListElement.toString()));
            value.addValueChangeListener(evt -> {
              if ((Boolean) value.getValue()) {
                  if (!multiSelectionInList.getSelection().contains(currentListElement)){
                      multiSelectionInList.getSelection().add(currentListElement);
                  }
              }
              else{
                multiSelectionInList.getSelection().remove(currentListElement);
              }
            });
        }

        return checkboxBuilder.getPanel();


    }


}
