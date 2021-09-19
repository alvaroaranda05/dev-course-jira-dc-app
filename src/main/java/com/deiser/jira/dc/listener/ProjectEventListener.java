package com.deiser.jira.dc.listener;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.ProjectCreatedEvent;
import com.atlassian.jira.event.ProjectDeletedEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


public class ProjectEventListener implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(ProjectEventListener.class);

    private final EventPublisher eventPublisher;

    public ProjectEventListener(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterPropertiesSet() {
        eventPublisher.register(this);
    }

    @Override
    public void destroy() {
        eventPublisher.unregister(this);
    }

    @EventListener
    public void onProjectCreatedEvent(ProjectCreatedEvent projectCreatedEvent) {
        LOGGER.warn("Project created: " + projectCreatedEvent.getProject().getName());
    }

    @EventListener
    public void onProjectDeleteEvent(ProjectDeletedEvent projectDeletedEvent) {
        LOGGER.warn("Project deleted: " + projectDeletedEvent.getProject().getName());
    }
}
