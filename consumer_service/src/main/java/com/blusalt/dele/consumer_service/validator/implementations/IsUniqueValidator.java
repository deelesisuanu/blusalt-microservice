package com.blusalt.dele.consumer_service.validator.implementations;

import com.blusalt.dele.consumer_service.repository.BaseRepository;
import com.blusalt.dele.consumer_service.validator.IsUnique;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

@Slf4j
public class IsUniqueValidator implements ConstraintValidator<IsUnique, Object> {

    private final BaseRepository baseRepository;
    private String tableName;
    private String columnName;
    private boolean required;
    private boolean shouldBeUnique;

    public IsUniqueValidator(BaseRepository repository) {
        this.baseRepository = repository;
    }

    @Override
    public void initialize(IsUnique constraintAnnotation) {
        tableName = constraintAnnotation.tableName();
        columnName = constraintAnnotation.columnName();
        required = constraintAnnotation.required();
        shouldBeUnique = constraintAnnotation.shouldBeUnique();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String && !required && StringUtils.isBlank(String.valueOf(value))) return true;
        if (!required && isNull(value)) return true;
        return shouldBeUnique && baseRepository.isUnique(tableName, columnName, value);
    }
}
