package tech.itpark.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);

    T save(T entity);
    boolean removeById(ID id);
}
