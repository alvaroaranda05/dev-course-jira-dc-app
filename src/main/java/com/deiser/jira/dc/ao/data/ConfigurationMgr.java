package com.deiser.jira.dc.ao.data;

import com.deiser.jira.dc.ao.AoBaseManager;

public interface ConfigurationMgr extends AoBaseManager<AOConfiguration, String> {

    AOConfiguration create(String key);

    AOConfiguration create(String key, String value);

    void save(AOConfiguration configuration);

    AOConfiguration getConfiguration(String key);

    AOConfiguration getConfigurationWithStream(String key);

    String getValue(String key);

    String getValueWithStream(String key);

    boolean existKey(String key);

    void delete(AOConfiguration... entities);
}
