package com.deiser.jira.dc.action;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.bc.user.search.UserSearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.web.action.JiraWebActionSupport;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TopBarPageAction extends JiraWebActionSupport {

    public String getLoggedUserWithHtml() {
        ApplicationUser loggedInUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        return loggedInUser != null ? loggedInUser.getDisplayName() : null;
    }

    public List<String> getProjectsWithHtml() {
        ProjectManager projectManager = ComponentAccessor.getProjectManager();
        return projectManager.getProjects().stream()
                .filter(Objects::nonNull)
                .map(Project::getName)
                .collect(Collectors.toList());
    }

    public List<String> getFieldsWithHtml() {
        FieldManager fieldManager = ComponentAccessor.getFieldManager();
        return fieldManager.getNavigableFields().stream()
                .filter(Objects::nonNull)
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    private void testJiraComponents() {
        //Managers do operations
        //Services require validations for certain operations
        IssueManager issueManager = ComponentAccessor.getIssueManager();
        IssueService issueService = ComponentAccessor.getIssueService();
        ProjectManager projectManager = ComponentAccessor.getProjectManager();
        ProjectService projectService = ComponentAccessor.getOSGiComponentInstanceOfType(ProjectService.class);
        UserManager userManager = ComponentAccessor.getUserManager();
        GroupManager groupManager = ComponentAccessor.getGroupManager();
        ProjectRoleManager projectRoleManager = ComponentAccessor.getOSGiComponentInstanceOfType(ProjectRoleManager.class);
        FieldManager fieldManager = ComponentAccessor.getFieldManager();
        CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();

        //Get logged in user
        JiraAuthenticationContext jiraAuthenticationContext = ComponentAccessor.getJiraAuthenticationContext();
        ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();

        //Get i18n text
        I18nHelper i18nHelper = ComponentAccessor.getI18nHelperFactory().getInstance(loggedInUser);
        String i18nText = i18nHelper.getText("my.text.in.the.18n.files");
    }
}
