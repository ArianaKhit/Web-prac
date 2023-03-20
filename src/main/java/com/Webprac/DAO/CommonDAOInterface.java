package com.Webprac.DAO;

import com.Webprac.tables.CommonEntity;
import java.util.Collection;

public interface CommonDAOInterface<T extends CommonEntity<ID>, ID> {
    T getById(ID id);

    Collection<T> getAll();

    void save(T entity);

    void saveCollection(Collection<T> entities);

    void delete(T entity);

    void deleteById(ID id);

    void update(T entity);
}