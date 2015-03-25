package org.programmiersportgruppe.jirabrowser;


import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.*;

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
    public final ValueModel descriptionModel = getOptionalModel("description");
    public final ValueModel storyModel = getOptionalModel("story");
    public final ValueModel acceptanceCriteria = getOptionalModel("acceptanceCriteria");
    public final ValueModel releaseNotes = getOptionalModel("releaseNotes");



    private ConverterValueModel getOptionalModel(String propertyName) {
        return new ConverterValueModel(details.getComponentModel(propertyName), new OptionalStringConverter());
    }

    public final ValueModel filterQuery = new ValueHolder("");

    public final SelectionInList<JiraTicket> ticketSelection;

    public BrowserViewModel(List<JiraTicket> tickets) {
        this.allTickets =tickets;
        this.ticketSelection = new SelectionInList<JiraTicket>(filteredTicketsHolder, selectedTicket);
        filter();

        this.filterQuery.addValueChangeListener(evt -> {
            filter();
        });
    }

    private void filter() {
        String query = ((String) filterQuery.getValue()).toLowerCase().trim();
        System.out.println(allTickets.size());
        List<JiraTicket> filteredTickets = allTickets
                .stream().filter(ticket ->
                    ticket.getSummary().toLowerCase().contains(query)
                            || ticket.getDescription().map(String::toLowerCase).orElse("").contains(query)
                            || ticket.getKey().toString().toLowerCase().contains(query)
                )

                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .collect(toList());

        System.out.println(filteredTickets.size());
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
