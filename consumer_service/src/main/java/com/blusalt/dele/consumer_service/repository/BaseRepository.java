package com.blusalt.dele.consumer_service.repository;

import com.blusalt.dele.consumer_service.domain.request.PaginationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.javers.spring.annotation.JaversAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

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
    //The filter map object takes care of both OR, AND & LIKE;
    //If the value is of type List means it represents OR;
    //If the value is of type Map means it represents LIKE;
    //If the value is of type String[] means it represents in;
    //By default all value are chained with AND
    //If the value is of type Pair<Date,Date> means it represents in;
    //If value is of type AtomicReference means it represents !=
    @SuppressWarnings("unchecked")
    public <T> Page<T> findAllBy(Class<T> type, Map<String, Object> filterTemp, PaginationRequest page) {

        Map<String, Object> filter = new HashMap<>(filterTemp);

        AtomicReference<String> sqlQuery = new AtomicReference<>();
        sqlQuery.set("select e from  " + type.getSimpleName() + " e " + (filter.isEmpty() ? "" : "where "));

        filter.entrySet().stream().filter(o -> o.getValue() == null).forEach(i -> sqlQuery.set(sqlQuery.get() + " " + i.getKey() + " IS NULL AND"));

        filter.entrySet().stream().filter(o -> (o.getValue() != null && !(o.getValue() instanceof List<?>)
                        && !(o.getValue() instanceof Map) && !(o.getValue() instanceof AtomicReference<?>) && !(o.getValue() instanceof String[])
                        && !(o.getValue() instanceof Pair)))
                .forEach(i -> sqlQuery.set(sqlQuery.get() + " " + i.getKey() + " = :" + i.getKey() + " AND"));

        filter.entrySet().stream().filter(k -> k.getValue() instanceof String[]).forEach(m ->
                sqlQuery.set(sqlQuery.get() + " " + m.getKey() + " in :" + m.getKey() + " AND"));

        filter.entrySet().stream().filter(k -> k.getValue() instanceof Pair).forEach(m -> sqlQuery.set(sqlQuery.get() + " " + m.getKey() + " BETWEEN :" + m.getKey() + "Start AND :" + m.getKey() + "End AND"));

        filter.entrySet().stream().filter(k -> k.getValue() instanceof AtomicReference).forEach(m ->
        {
            if (Objects.equals(m.getValue(), "null")) {
                sqlQuery.set(sqlQuery.get() + " " + m.getKey() + " is not :" + m.getKey() + " AND");

            } else {
                sqlQuery.set(sqlQuery.get() + " " + m.getKey() + " != :" + m.getKey() + " AND");
            }
        });

        filter.keySet().stream().filter(o -> (filter.get(o) instanceof Map))
                .forEach(i -> ((Map<String, String>) filter.get(i)).keySet().forEach(k -> {
                    String value = ((Map<String, String>) filter.get(i)).get(k);
                    sqlQuery.set(sqlQuery.get() + " " + k + " LIKE :" + k + " OR");
                }));
        if (filter.keySet().stream().anyMatch(o -> (filter.get(o) instanceof Map))) {
            sqlQuery.set(sqlQuery.get().substring(0, sqlQuery.get().length() - 2));
            sqlQuery.set(sqlQuery.get() + "AND");
        }


        filter.keySet().stream().filter(s -> (filter.get(s) != null && filter.get(s) instanceof List<?>)).forEach(s -> buildQueryString(filter, sqlQuery, s));

        sqlQuery.set(filter.isEmpty() ? sqlQuery.get() : sqlQuery.get().substring(0, sqlQuery.get().length() - 4));

        TypedQuery<Long> countQuery = entityManager.createQuery(sqlQuery.get().replace("select e from", "select count(e) from"), Long.class);
        TypedQuery<T> typeQuery = entityManager.createQuery(sqlQuery.get() + " ORDER BY createdDate DESC", type);

        filter.entrySet().stream().filter(i -> i.getValue() != null && !(i.getValue() instanceof List<?>) && !(i.getValue() instanceof Map)
                && !(i.getValue() instanceof AtomicReference<?>) && !(i.getValue() instanceof String[]) && !(i.getValue() instanceof Pair)).forEach(i -> {
            typeQuery.setParameter(i.getKey(), i.getValue());
            countQuery.setParameter(i.getKey(), i.getValue());
        });
        filter.entrySet().stream().filter(k -> k.getValue() instanceof String[]).forEach(m -> {
            List<String> value = Arrays.asList((String[]) m.getValue());
            typeQuery.setParameter(m.getKey(), value);
            countQuery.setParameter(m.getKey(), value);
        });

        filter.entrySet().stream().filter(k -> k.getValue() instanceof Pair).forEach(m -> {

            Pair<Date, Date> value = (Pair<Date, Date>) m.getValue();
            typeQuery.setParameter(m.getKey() + "Start", value.getKey());
            typeQuery.setParameter(m.getKey() + "End", value.getValue());
            countQuery.setParameter(m.getKey() + "Start", value.getKey());
            countQuery.setParameter(m.getKey() + "End", value.getValue());
        });

        filter.entrySet().stream().filter(i -> i.getValue() != null && (i.getValue() instanceof AtomicReference<?>)).forEach(h -> {
            typeQuery.setParameter(h.getKey(), ((AtomicReference<?>) h.getValue()).get());
            countQuery.setParameter(h.getKey(), ((AtomicReference<?>) h.getValue()).get());
        });

        filter.keySet().stream().filter(o -> filter.get(o) != null && (filter.get(o) instanceof List<?>)).forEach(i -> {
            List<?> values = (List<?>) filter.get(i);
            IntStream.range(0, values.size()).forEach(index -> {
                typeQuery.setParameter(i + "" + index, values.get(index));
                countQuery.setParameter(i + "" + index, values.get(index));
            });
        });

        filter.keySet().stream().filter(o -> filter.get(o) != null && (filter.get(o) instanceof Map))
                .forEach(i -> ((Map<String, String>) filter.get(i)).keySet().forEach(k -> {
                    String value = ((Map<String, String>) filter.get(i)).get(k);

                    typeQuery.setParameter(k, value);
                    countQuery.setParameter(k, value);

                }));

        Long contentSize = countQuery.getSingleResult();
        page.setPage(page.getPage() <= 1 ? 1 : page.getPage());
        page.setSize(page.getSize() == 0 ? (contentSize.intValue() == 0 ? 1 : contentSize.intValue()) : page.getSize());

        typeQuery.setFirstResult((page.getPage() - 1) * page.getSize()).setMaxResults(page.getSize());

        return new PageImpl<>(typeQuery.getResultList(), PageRequest.of(page.getPage() - 1, page.getSize()),
                contentSize);
    }

    private void buildQueryString(Map<String, Object> filter, AtomicReference<String> sqlQuery, String s) {
        sqlQuery.set(sqlQuery.get() + "(");
        IntStream.range(0, ((List<?>) filter.get(s)).size()).forEach(index -> sqlQuery.set(sqlQuery.get() + " " + s + " = :" + s + "" + index + " OR"));
        sqlQuery.set(sqlQuery.get().substring(0, sqlQuery.get().length() - 3) + ") AND");
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