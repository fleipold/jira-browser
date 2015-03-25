package org.programmiersportgruppe.jirabrowser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.jgoodies.binding.beans.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class JiraTicket extends Model  {

    ISO8601DateFormat df = new ISO8601DateFormat();

    private final Optional<String> audience;
    private String projectKey;
    private String summary;
    private String key;
    private String type;
    private String status;

    private Optional<String> story;
    private Optional<String> description;
    private Optional<String> acceptanceCriteria;
    private Optional<String> releaseN;

    private String reporter;
    private Date created;
    private Date updated;

    public JiraTicket(JsonNode issue) {

        /*
         fields =  issue.get("fields")
    type = fields.get("issuetype").get("name")
    key = issue.get("key")
    status = fields.get("status").get("name")
    reporter = fields.get("reporter").get("name")
    story = fields.get("customfield_10501")
    description = fields.get("description")
    acceptance_criteria = fields.get("customfield_10500")
    labels = fields.get("labels")
    comments = fields.get("comment").get("comments")
    links = fields.get("issuelinks")
    attachments = fields.get("attachment")
    project_key = fields.get("project").get("key")

    release_notes = fields.get("customfield_10806")
    audience = fields.get("customfield_10020")
    created = DateTime.parse(fields.get("created"))
    updated = DateTime.parse(fields.get("updated"))

     uri = URI(issue.get("self"))
     uri.path = "/browse/#{key}"


    if (audience)
        audience = audience.get("value")
    end
         */
        JsonNode fields = issue.get("fields");


        JsonNode summaryNode = fields.get("summary");
        summary = summaryNode.textValue();

        type = fields.get("issuetype").get("name").textValue();
        key = issue.get("key").textValue();
        status = fields.get("status").get("name").textValue();
        reporter = fields.get("reporter").get("name").textValue();
        story = Optional.ofNullable(fields.get("customfield_10501")).map(JsonNode::textValue);
        description = Optional.ofNullable(fields.get("description")).map(JsonNode::textValue);
        acceptanceCriteria = Optional.ofNullable(fields.get("customfield_10500")).map(JsonNode::textValue);
//        labels = fields.get("labels").textValue();
//        comments = fields.get("comment").get("comments").textValue();
//        links = fields.get("issuelinks").textValue();
//        attachments = fields.get("attachment")
        projectKey = fields.get("project").get("key").textValue();
        releaseN = Optional.ofNullable(fields.get("customfield_10806")).map(JsonNode::textValue);
        audience = Optional.ofNullable(fields.get("customfield_10020")).map(JsonNode::textValue);
        try {
            created = df.parse(fields.get("created").textValue());
            updated = df.parse(fields.get("updated").textValue());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public String getSummary() {
        return summary;
    }


    public Optional<String> getAudience() {
        return audience;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public JiraKey getKey() {
        return new JiraKey(key);
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Optional<String> getStory() {
        return story;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public Optional<String> getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public Optional<String> getReleaseNotes() {
        return releaseN;
    }

    public String getReporter() {
        return reporter;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }


}
