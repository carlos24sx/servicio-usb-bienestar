package co.edu.unisimon.bienestar.business.domain.service;

import java.util.List;
import java.util.Optional;

/**
 * @author William Torres
 * @version 1.0
 */
public interface IService<T, E> {

    List<T> findAll();

    T save(T t);

    Optional<T> findById(E id);

    void delete(T t);

}