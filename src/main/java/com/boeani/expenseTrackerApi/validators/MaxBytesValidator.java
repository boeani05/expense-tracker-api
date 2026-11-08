package com.boeani.expenseTrackerApi.validators;

import com.boeani.expenseTrackerApi.annotations.MaxBytes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.charset.StandardCharsets;

public class MaxBytesValidator implements ConstraintValidator<MaxBytes, String> {
    private int maxByteSize;

    @Override
    public void initialize(MaxBytes constraintAnnotation) {
        maxByteSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        return value.getBytes(StandardCharsets.UTF_8).length <= maxByteSize;
    }
}
