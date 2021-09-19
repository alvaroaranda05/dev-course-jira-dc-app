package com.deiser.jira.dc.ao.data;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.deiser.jira.dc.ao.AoBaseManagerImpl;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.function.Function;

import static com.deiser.jira.dc.ao.data.AOConfiguration.COLUMN_KEY;
import static com.deiser.jira.dc.ao.data.AOConfiguration.COLUMN_VALUE;


public class ConfigurationMgrImpl extends AoBaseManagerImpl<AOConfiguration, String> implements ConfigurationMgr {

    private static final String QUERY_KEY = " " + COLUMN_KEY + " = ? ";

    private static final String ORDER_BY_KEY = COLUMN_KEY + " " + "ASC";
    private static final String[] ALL_FIELDS = new String[]{COLUMN_KEY, COLUMN_VALUE};

    public ConfigurationMgrImpl(ActiveObjects ao) {super(ao);}


    @Override
    public AOConfiguration create(String key) {
        Validate.isTrue(isValid(key));
        return super.create(new DBParam(COLUMN_KEY, key));
    }

    @Override
    public AOConfiguration create(String key, String value) {
        Validate.isTrue(isValid(key));
        return super.create(new DBParam(COLUMN_KEY, key), new DBParam(COLUMN_VALUE, value));
    }


    @Override
    public void save(AOConfiguration configuration){
        Validate.isTrue((configuration != null) && isValid(configuration.getKey()));
        configuration.save();
    }


    @Override
    public void delete(AOConfiguration... entities) {
        super.delete(entities);
    }


    private boolean isValid(String key){
        return StringUtils.isNotBlank(key);
    }


    @Override
    public boolean existKey(String key) {
        return (get(key) != null);
    }


    @Override
    public String getValue(String key) {
        AOConfiguration configuration = getConfiguration(key);
        return (configuration != null) ? configuration.getValue() : null;
    }


    @Override
    public String getValueWithStream(String key){
        AOConfiguration configuration = getConfigurationWithStream(key);
        return (configuration != null) ? configuration.getValue() : null;
    }


    @Override
    public AOConfiguration getConfiguration(String key) {
        Function<String, AOConfiguration[]> accessWithFind = (keyValue) -> super.getByFilter(QUERY_KEY, keyValue);
        return validateAndGetConfiguration(key, accessWithFind);
    }


    @Override
    public AOConfiguration getConfigurationWithStream(String key){
        Function<String, AOConfiguration[]> accessWithStream = (keyValue) -> super.getByFilterWithStream(QUERY_KEY, keyValue);
        return validateAndGetConfiguration(key, accessWithStream);
    }


    private AOConfiguration validateAndGetConfiguration(String key, Function<String, AOConfiguration[]> accessToBbdd){
        if(StringUtils.isEmpty(key)){return null;}
        AOConfiguration[] configurations = accessToBbdd.apply(key);
        return ArrayUtils.isNotEmpty(configurations) ? configurations[0] : null;
    }

    @Override
    public <D> void fill(List<D> list, Function<AOConfiguration, ? extends D> function) {
        super.fill(list, Query.select(getFields()).order(ORDER_BY_KEY), function);
    }


    @Override
    protected Class<AOConfiguration> getEntityClass() {
        return AOConfiguration.class;
    }



    @Override
    protected String[] getArrayFields() {
        return ALL_FIELDS;
    }


    @Override
    public AOConfiguration[] get() {
        return super.getByOrder(ORDER_BY_KEY);
    }
}
