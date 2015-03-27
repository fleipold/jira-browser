package org.programmiersportgruppe.jirabrowser;

import org.programmiersportgruppe.swingutils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) {

        String[] availableFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();


        TicketReader reader = new TicketReader();
        File jiraDirectory = new File("/Users/fleipold/clients/nokia/jira/PBAPI/raw");

        Stream<JiraTicket> tickets = asList(jiraDirectory.listFiles())
                .stream()
                .filter(file -> file.getName().endsWith(".json") && !file.getName().equals("sync_state.json"))
                .map(reader::readTicket);


        BrowserViewModel viewModel = new BrowserViewModel(tickets.collect(toList()));
        BrowserViewBuilder viewBuilder = new BrowserViewBuilder();
        UIUtils.fixLnF();

        showInFrame(viewBuilder.buildPanel(viewModel), "Jira Tickets");
    }

    public static void showInFrame(JComponent pane, String title) {
        final JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(pane);
        frame.pack();
        frame.setVisible(true);
        UIUtils.locateOnScreenCenter(frame);
    }

}

