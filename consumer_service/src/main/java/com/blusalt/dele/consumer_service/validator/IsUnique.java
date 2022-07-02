package com.blusalt.dele.consumer_service.validator;

import com.blusalt.dele.consumer_service.validator.implementations.IsUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IsUniqueValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IsUnique {

    String tableName();

    String columnName();

    boolean shouldBeUnique() default true;

    boolean required() default true;

    String message() default "In use already";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
