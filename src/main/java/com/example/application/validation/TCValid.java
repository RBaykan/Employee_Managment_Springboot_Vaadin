package com.example.application.validation;
import com.example.application.validation.impl.TCValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TCValidator.class)
public @interface TCValid {

    String message() default "Invalid TCKN";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
