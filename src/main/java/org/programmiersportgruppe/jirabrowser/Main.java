package org.programmiersportgruppe.jirabrowser;

import org.programmiersportgruppe.swingutils.UIUtils;

import javax.swing.*;
import java.io.File;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, InterruptedException {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Jira Browser");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        TicketReader reader = new TicketReader();
        File jiraDirectory = new File(".");

        Stream<JiraTicket> tickets = asList(jiraDirectory.listFiles())
                .stream()
                .filter(file -> file.getName().endsWith(".json") && !file.getName().equals("sync_state.json"))
                .map(reader::readTicket)
                .filter(ticket -> ticket != null);


        BrowserViewModel viewModel = new BrowserViewModel(tickets.collect(toList()));
        BrowserViewBuilder viewBuilder = new BrowserViewBuilder();
        fixLnF();

        showInFrame(viewBuilder.buildPanel(viewModel), "Jira Tickets");
    }

    private static void fixLnF() {
        final Object osName = System.getProperties().get("os.name");
        if (osName.equals("Mac OS X")){
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Jira Browser");
        } else {
            UIUtils.loadPlasticLnF();
        }
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

