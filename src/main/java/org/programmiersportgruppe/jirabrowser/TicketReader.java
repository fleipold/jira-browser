package org.programmiersportgruppe.jirabrowser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;


public class TicketReader {

    ObjectMapper mapper = new ObjectMapper();


    public JiraTicket readTicket(File file){
        try {
            return new JiraTicket(mapper.readTree(file));
        } catch (Exception e) {
            return null;
        }
    }
}
