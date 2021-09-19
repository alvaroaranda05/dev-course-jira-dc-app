package com.deiser.jira.dc.ao;

import net.java.ao.Query;
import net.java.ao.RawEntity;

import java.util.List;
import java.util.function.Function;


public interface AoBaseManager  <T extends RawEntity<K>, K extends Object>{

    T get(K id);

    T[] get(K... ids);

    T[] get(Query query);

    T[] getWithStream(Query query);

    <D> void fill(List<D> list, Query query, Function<T, ? extends D> transformer);

    T[] get();

    T[] getWithStream();

    <D> void fill(List<D> list, Function<T, ? extends D> transformer);

    T[] getByOrder(String orderBy);

    void delete(T... entities);

    void save(T entity);
}
