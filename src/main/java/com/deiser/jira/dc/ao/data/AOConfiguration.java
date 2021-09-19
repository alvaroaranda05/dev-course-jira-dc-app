package com.deiser.jira.dc.ao.data;

import net.java.ao.RawEntity;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.PrimaryKey;
import net.java.ao.schema.StringLength;
import net.java.ao.schema.Table;

/**
 * The interface Configuration.
 */
@Table("AOConfiguration")
public interface AOConfiguration extends RawEntity<String> {

    String COLUMN_KEY = "KEY";
    String COLUMN_VALUE = "VALUE";

    @NotNull
    @PrimaryKey("KEY")
    @StringLength(100)
    String getKey();

    @StringLength(100)
    void setKey(String key);

    @StringLength(StringLength.UNLIMITED)
    String getValue();

    @StringLength(StringLength.UNLIMITED)
    void setValue(String value);


}
