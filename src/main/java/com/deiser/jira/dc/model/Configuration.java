package com.deiser.jira.dc.model;

import com.deiser.jira.dc.ao.data.AOConfiguration;

public class Configuration {
    private String key;
    private String value;

    public Configuration(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Configuration(AOConfiguration aoConfiguration) {
        this.key = aoConfiguration.getKey();
        this.value = aoConfiguration.getValue();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
