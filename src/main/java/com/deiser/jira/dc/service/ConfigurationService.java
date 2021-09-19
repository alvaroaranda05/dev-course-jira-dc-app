package com.deiser.jira.dc.service;

import com.deiser.jira.dc.model.Configuration;

import java.util.List;

/**
 * Configuration service
 */
public interface ConfigurationService {

    List<Configuration> get();

    Configuration get(String key);

    boolean exist(String key);

    Configuration create(String key, String value);

    Configuration update(String key, String value);

    boolean delete(String key);
}
