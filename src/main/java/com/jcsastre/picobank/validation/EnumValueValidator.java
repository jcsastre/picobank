package com.jcsastre.picobank.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<Enum, String> {

    private Enum annotation;

    @Override
    public void initialize(Enum annotation)
    {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String valueForValidation, ConstraintValidatorContext constraintValidatorContext) {

        boolean result = false;

        if (valueForValidation == null || valueForValidation.isEmpty()) {
            return false;
        }

        Object[] enumValues = this.annotation.enumClass().getEnumConstants();

        if(enumValues != null) {
            for(Object enumValue : enumValues) {
                if(
                    valueForValidation.equals(enumValue.toString()) ||
                    (this.annotation.ignoreCase() && valueForValidation.equalsIgnoreCase(enumValue.toString()))
                ) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}
