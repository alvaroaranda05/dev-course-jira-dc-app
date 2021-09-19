package com.deiser.jira.dc.listener;


import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.event.events.PluginDisabledEvent;
import com.atlassian.plugin.event.events.PluginEnabledEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class LifecycleEventListener implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(LifecycleEventListener.class);

    private final EventPublisher eventPublisher;

    public LifecycleEventListener(EventPublisher eventPublisher) {
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
    public void onAppEnabled(PluginEnabledEvent pluginEnabledEvent) {
        LOGGER.warn("App " + pluginEnabledEvent.getPlugin().getName() + " has been enabled!!");
    }
}
