package com.boeani.expenseTrackerApi.annotations;

import com.boeani.expenseTrackerApi.validators.MaxBytesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxBytesValidator.class)
public @interface MaxBytes {
    String message() default "cannot exceed max byte size of {value} Bytes";

    int value() default 72;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
