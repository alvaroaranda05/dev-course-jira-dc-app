package com.deiser.jira.dc.ao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.deiser.jira.dc.exception.UndeletableEntityException;
import net.java.ao.DBParam;
import net.java.ao.Query;
import net.java.ao.RawEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class AoBaseManagerImpl<T extends RawEntity<K>, K extends Object> implements AoBaseManager<T, K> {

    private static final Logger LOGGER = Logger.getLogger(AoBaseManagerImpl.class);


    protected static final String PLACEHOLDER_COMMA_LIST = "placeholderCommaList";
    public static final int PARAMETER_BLOCKS = 1000;

    protected static final String COMMA = ",";

    protected final ActiveObjects ao;

    public AoBaseManagerImpl(ActiveObjects ao) {
        this.ao = ao;
    }

    protected T create(DBParam... parameter) {
        return (T) this.ao.create(this.getEntityClass(), parameter);
    }

    public void save(T entity) {
        entity.save();
    }

    public void delete(T... entities) {

        if (entities == null || entities.length <= 0) {
            return;
        }

        try {
            this.beforeDelete(entities);
            deleteAllEntities(entities);
            this.afterDelete(entities);
        } catch (Exception exception) {
            throw new UndeletableEntityException(exception);
        }
    }


    private void deleteAllEntities(T... entities){
        List<T[]> listArrayEntities = splitArray(entities);

        if(!listArrayEntities.isEmpty()){
            for(T[] array: listArrayEntities){
                this.ao.delete(array);
            }
        }
    }


    private List<T[]> splitArray(T... entities){
        List<T[]> listArrays = new ArrayList();

        int lengthToSplit = PARAMETER_BLOCKS;
        int arrayLength = entities.length;

        for (int i = 0; i < arrayLength; i = i + lengthToSplit) {
            if (arrayLength < i + lengthToSplit){
                lengthToSplit = arrayLength - i;
            }
            listArrays.add(ArrayUtils.subarray(entities, i, lengthToSplit));
        }

        return listArrays;
    }

    protected void beforeDelete(T... entities) {
    }


    protected void afterDelete(T... entities){
    }

    public T get(K id) {
        return (T) this.ao.get(this.getEntityClass(), id);
    }

    public T[] get(K... ids) {
        return (T[]) this.ao.get(this.getEntityClass(), ids);
    }

    public T[] get(Query query) {
        T[] result = (T[]) this.ao.find(this.getEntityClass(), query);
        return (result != null) ? result : this.getEmptyArray();
    }

    public T[] getWithStream(final Query query) {
        final List<T> entityList = new ArrayList<T>();
        this.ao.stream(getEntityClass(), query, t -> entityList.add(t));
        return (T[]) entityList.toArray(this.getEmptyArray());
    }

    public T[] get() {
        T[] result = this.ao.find(this.getEntityClass());
        return (result != null) ? result : this.getEmptyArray();
    }

    public T[] getWithStream() {
        final List<T> entityList = new ArrayList<T>();
        this.ao.stream(getEntityClass(), t -> entityList.add(t));
        return (T[]) entityList.toArray(this.getEmptyArray());
    }

    public T get(T entity) {
        return this.get(entity);
    }

    public T[] getByOrder(String orderBy) {
        return this.get(Query.select().order(orderBy));
    }

    protected T[] getByFilter(String filterQuery, Object... parameter) {
        T[] resultList = get(Query.select().where(filterQuery, parameter));
        return (resultList == null) ? this.getEmptyArray() : resultList;
    }

    protected T[] getByFilterWithOrder(String filterQuery, String order, Object... parameter){
        T[] resultList = get(Query.select().where(filterQuery, parameter).order(order));
        return (resultList == null) ? this.getEmptyArray() : resultList;
    }

    protected T[] getByFilterWithStream(String filterQuery, Object... parameter) {
        T[] resultList = getWithStream(Query.select(getFields()).from(this.getEntityClass()).where(filterQuery, parameter));
        return (resultList == null) ? this.getEmptyArray() : resultList;
    }


    protected T[] getByFilterWithStreamWithOrder(String filterQuery, String order, Object... parameter) {
        T[] resultList = getWithStream(Query.select(getFields()).from(this.getEntityClass()).where(filterQuery, parameter).order(order));
        return (resultList == null) ? this.getEmptyArray() : resultList;
    }

    protected <D> void fill(List<D> list, Function<T, ? extends D> transformer, String filterQuery, Object... parameter) {
        fill(list, Query.select(getFields()).from(this.getEntityClass()).where(filterQuery, parameter), transformer);
    }

    protected <D> void fillWithOrder(List<D> list, Function<T, ? extends D> transformer, String filterQuery, String fieldOrder, Object... parameter) {
        fill(list, Query.select(getFields()).from(this.getEntityClass()).where(filterQuery, parameter).order(fieldOrder), transformer);
    }


    protected String getFields() {
        return Stream.of(getArrayFields()).collect(Collectors.joining(COMMA));
    }

    @Override
    public <D> void fill(final List<D> list, Function<T, ? extends D> function) {
        if (list == null) {
            return;
        }

        this.ao.stream(getEntityClass(), t -> {
            D result = function.apply(t);
            if (result != null) {
                list.add(result);
            }
        });
    }

    @Override
    public <D> void fill(final List<D> list, Query query, Function<T, ? extends D> function) {
        if (list == null) {
            return;
        }

        this.ao.stream(getEntityClass(), query, t -> {
            D result = function.apply(t);
            if (result != null) {
                list.add(result);
            }
        });
    }

    protected T[] getEmptyArray() {
        return (T[]) Array.newInstance(getEntityClass(), 0);
    }

    protected String replacePlaceholderWithData(String query, String... data) {
        String dataStr = StringUtils.join(data, COMMA);
        return StringUtils.replace(query, PLACEHOLDER_COMMA_LIST, dataStr);
    }

    protected abstract String[] getArrayFields();

    protected abstract Class<T> getEntityClass();
}
