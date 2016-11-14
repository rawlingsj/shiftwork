package com.teammachine.staffrostering.domain.validators;

import com.teammachine.staffrostering.domain.enumeration.DurationUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.EnumSet;

public class AcceptDurationUnitsValidator implements ConstraintValidator<AcceptDurationUnits, DurationUnit> {

    private EnumSet<DurationUnit> allowedUnits;

    @Override
    public void initialize(AcceptDurationUnits units) {
        this.allowedUnits = EnumSet.copyOf(Arrays.asList(units.units()));
    }

    @Override
    public boolean isValid(DurationUnit durationUnit, ConstraintValidatorContext constraintValidatorContext) {
        return allowedUnits.contains(durationUnit);
    }
}
