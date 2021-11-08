package com.tui.proof.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( {FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PilotesQuantityInRangeImpl.class)
@Documented
public @interface PilotesQuantityInRange {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
