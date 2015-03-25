package org.programmiersportgruppe.jirabrowser;

public class JiraKey implements Comparable<JiraKey> {
    String project;
    int ticket;

    public JiraKey(String stringRepresentation) {
        String[] elements = stringRepresentation.split("-");
        if (elements.length != 2)
            throw new RuntimeException("Problems parsing jira key: '"+ stringRepresentation +"'");
        this.project = elements[0];
        this.ticket  =  Integer.parseInt(elements[1]);


    }

    @Override
    public String toString() {
        return project + "-" + ticket;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiraKey jiraKey = (JiraKey) o;

        if (ticket != jiraKey.ticket) return false;
        if (project != null ? !project.equals(jiraKey.project) : jiraKey.project != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = project != null ? project.hashCode() : 0;
        result = 31 * result + ticket;
        return result;
    }

    @Override
    public int compareTo(JiraKey o) {
        if (o == null) {
            return -1;
        }
        return Integer.compare(ticket, o.ticket);
    }
}
