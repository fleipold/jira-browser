package org.programmiersportgruppe.jirabrowser;


import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.BindingConverter;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.collect.ArrayListModel;
import org.programmiersportgruppe.jgoodies.listadapter.MultiSelectionInList;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


public class BrowserViewModel {

    private final List<JiraTicket> allTickets;
    public final ValueModel filteredTicketsHolder = new ValueHolder(null, true);
    public final ValueModel selectedTicket = new ValueHolder(null, true);

    public final BeanTableAdapter<JiraTicket> tableAdapter = new BeanTableAdapter<>(
            new BrowserViewBuilder.ColumnModel<JiraTicket, JiraKey>("Key", t -> t.getKey(), JiraKey.class, true),
            new BrowserViewBuilder.ColumnModel<JiraTicket, String>("Status", t -> t.getStatus(), String.class, true),
            new BrowserViewBuilder.ColumnModel<JiraTicket, String>("Type", t -> t.getType(), String.class, true),
            new BrowserViewBuilder.ColumnModel<JiraTicket, String>("Summary", t -> t.getSummary(), String.class, false)
    );


    private final PresentationModel<JiraTicket> details = new PresentationModel<>(selectedTicket);

    public final ValueModel summary = details.getModel("summary");
    public final ValueModel key = details.getModel("key");

    public final ValueModel descriptionModel = getOptionalModel("description");
    public final ValueModel storyModel = getOptionalModel("story");
    public final ValueModel acceptanceCriteria = getOptionalModel("acceptanceCriteria");
    public final ValueModel releaseNotes = getOptionalModel("releaseNotes");

    public final Action openBrowserAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (ticketSelection.getSelection() != null) {
                openWebpage(URI.create(ticketSelection.getSelection().getDisplayUri()));
            }
        }
    };

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public final MultiSelectionInList statusFilter;

    private ConverterValueModel getOptionalModel(String propertyName) {
        return new ConverterValueModel(details.getComponentModel(propertyName), new OptionalStringConverter());
    }

    public final ValueModel filterQuery = new ValueHolder("");

    public final SelectionInList<JiraTicket> ticketSelection;

    public BrowserViewModel(List<JiraTicket> tickets) {

        tableAdapter.externalStringRenderer(jiraTicket ->
            "* [" + jiraTicket.getKey() + "](" + jiraTicket.getDisplayUri() + ") : " + jiraTicket.getSummary()
        );
        this.allTickets =tickets;
        this.ticketSelection = new SelectionInList<JiraTicket>(filteredTicketsHolder, selectedTicket);
        statusFilter = new MultiSelectionInList(new ArrayListModel<>(allTickets.stream().map(e -> e.getStatus()).distinct().collect(toList())));
        statusFilter.getSelection().addAll((ArrayListModel)statusFilter.getList());

        filter();
        this.filterQuery.addValueChangeListener(evt -> {
            filter();
        });

        statusFilter.getSelection().addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) { filter(); }

            @Override
            public void intervalRemoved(ListDataEvent e) { filter(); }

            @Override
            public void contentsChanged(ListDataEvent e) { filter(); }
        });

    }

    private void filter() {
        String query = ((String) filterQuery.getValue()).toLowerCase().trim();
        List<JiraTicket> filteredTickets = allTickets
                .stream().filter(ticket ->
                 ( ticket.getSummary().toLowerCase().contains(query)
                            || ticket.getDescription().map(String::toLowerCase).orElse("").contains(query)
                            || ticket.getKey().toString().toLowerCase().contains(query))
                 && statusFilter.getSelection().contains(ticket.getStatus())
                )

                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .collect(toList());
        filteredTicketsHolder.setValue(filteredTickets);
    }


    private static class OptionalStringConverter implements BindingConverter {
        @Override
        public Object targetValue(Object sourceValue) {
            if (sourceValue == null)
                return null;
            return ((Optional<String>)sourceValue).orElse(null);
        }

        @Override
        public Object sourceValue(Object targetValue) {
            return Optional.ofNullable((String)targetValue);
        }
    }
}
