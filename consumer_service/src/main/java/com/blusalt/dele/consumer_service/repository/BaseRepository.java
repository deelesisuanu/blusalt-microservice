package com.blusalt.dele.consumer_service.repository;

import lombok.extern.slf4j.Slf4j;
import org.javers.spring.annotation.JaversAuditable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deelesi Suanu <deelesisuanu@gmail.com>
 * @philosophy Quality must be enforced, otherwise it won't happen. We programmers must be required to write tests, otherwise we won't do it!
 * <p>
 * ------
 * Tip: Always code as if the guy who ends up maintaining your code will be a violent psychopath who knows where you live.
 * ------
 * @derived_from Mr Stephen Obi
 */

@Transactional
@Repository
@Slf4j
public class BaseRepository extends CustomRepository {

    public BaseRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @JaversAuditable
    public <T> T save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Transactional
    public <T> void refresh(T t) {
        entityManager.refresh(t);
    }

    @JaversAuditable
    public <T> void update(T entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }

    @SuppressWarnings("UnusedReturnValue")
    @JaversAuditable
    public <T> T directUpdate(T entity) {
        return entityManager.merge(entity);
    }

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    public int runNativeQuery(String query) {
        return entityManager.createNativeQuery(query).executeUpdate();
    }

    @JaversAuditable
    public <T> void delete(T entity) {
        entityManager.remove(entity);
    }

    public <T> void detach(T entity) {
        entityManager.detach(entity);
    }

    public <T> List<T> saveAll(Iterable<T> entities) {
        List<T> result = new ArrayList<>();
        for (T entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    public <T> T untrackedSave(T entity) {
        entityManager.persist(entity);
        return entity;
    }

}