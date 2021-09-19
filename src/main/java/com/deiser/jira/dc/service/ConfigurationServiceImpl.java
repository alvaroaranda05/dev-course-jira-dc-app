package com.deiser.jira.dc.service;

import com.deiser.jira.dc.ao.data.AOConfiguration;
import com.deiser.jira.dc.ao.data.ConfigurationMgr;
import com.deiser.jira.dc.model.Configuration;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ConfigurationServiceImpl implements ConfigurationService {

    private static final Logger LOGGER = Logger.getLogger(ConfigurationServiceImpl.class);

    private ConfigurationMgr configurationMgr;

    public ConfigurationServiceImpl(ConfigurationMgr configurationMgr) {
        this.configurationMgr = configurationMgr;
    }

    @Override
    public List<Configuration> get() {
        return Arrays.stream(configurationMgr.get())
                .map(Configuration::new)
                .collect(Collectors.toList());
    }

    @Override
    public Configuration get(String key) {
        return exist(key)
                ? new Configuration(configurationMgr.getConfiguration(key))
                : null;
    }

    @Override
    public boolean exist(String key) {
        return configurationMgr.existKey(key);
    }

    @Override
    public Configuration create(String key, String value) {
        if (exist(key)) return null;

        return new Configuration(configurationMgr.create(key, value));
    }

    @Override
    public Configuration update(String key, String value) {
        AOConfiguration aoConfiguration = configurationMgr.getConfiguration(key);
        if (aoConfiguration == null) return null;

        aoConfiguration.setValue(value);
        configurationMgr.save(aoConfiguration);
        return new Configuration(aoConfiguration);
    }

    @Override
    public boolean delete(String key) {
        AOConfiguration aoConfiguration = configurationMgr.getConfiguration(key);

        if (aoConfiguration == null) return false;
        configurationMgr.delete(aoConfiguration);
        return true;
    }
}
