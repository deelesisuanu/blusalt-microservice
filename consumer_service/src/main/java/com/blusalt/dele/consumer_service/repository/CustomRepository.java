package com.blusalt.dele.consumer_service.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.text.MessageFormat;

@Slf4j
@RequiredArgsConstructor
public class CustomRepository {

    protected final EntityManager entityManager;

    public boolean isValidId(String tableName, Long id) {
        return !entityManager.createQuery("select e from " + tableName + " e where e.id = " + id).getResultList().isEmpty();
    }

    public boolean isExist(String tableName, String columnName, Object value) {
        String sqlQuery = MessageFormat.format("select e from  {0} e where LOWER(e.{1}) = LOWER(:value)", tableName, columnName);
        return !entityManager.createQuery(sqlQuery)
                .setParameter("value", value)
                .getResultList()
                .isEmpty();
    }

}
