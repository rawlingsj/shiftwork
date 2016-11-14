package com.teammachine.staffrostering.domain.validators;

import com.teammachine.staffrostering.domain.enumeration.DurationUnit;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AcceptDurationUnitsValidator.class)
public @interface AcceptDurationUnits {

    DurationUnit[] units() default {};

    String message() default "Must be one of: {units}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

