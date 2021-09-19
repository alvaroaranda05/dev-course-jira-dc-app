package com.deiser.jira.dc.context;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import org.apache.log4j.Logger;


import java.util.HashMap;
import java.util.Map;

public class IssuePanelContextProvider extends AbstractJiraContextProvider {

    private static final Logger LOGGER = Logger.getLogger(IssuePanelContextProvider.class);

    public static final String ISSUE_PARAM = "issue";
    public static final String ISSUE_KEY_PARAM = "issueKey";

    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();

        //TODO uncomment this to get all Context parameters
        //jiraHelper.getContextParams().entrySet().forEach(entry -> LOGGER.warn("Entry: " + entry));

        Issue currentIssue = (Issue) jiraHelper.getContextParams().get(ISSUE_PARAM);
        contextMap.put(ISSUE_KEY_PARAM, currentIssue.getKey());

        return contextMap;
    }
}
