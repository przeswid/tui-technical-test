package com.tui.proof.common.validator;

import com.tui.proof.common.i18n.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PilotesQuantityInRangeImpl implements ConstraintValidator<PilotesQuantityInRange, Integer> {

    private final Integer[] availablePilotesQuantity;

    @Autowired
    public PilotesQuantityInRangeImpl(@Value("${pilotes.configuration.availableQuantities}") Integer[] availablePilotesQuantity) {
        this.availablePilotesQuantity = availablePilotesQuantity;
    }

    @Override
    public boolean isValid(Integer pilotes, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = Arrays.asList(availablePilotesQuantity).contains(pilotes);
        setCustomMessageInContext(constraintValidatorContext);

        return isValid;
    }

    private void setCustomMessageInContext(ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(getErrorMessage())
                .addConstraintViolation();
    }

    private String getErrorMessage() {
        return Messages.WRONG_QUANTITY_OF_PILOTES_MSG.replaceFirst("\\{\\}",
                Arrays.stream(availablePilotesQuantity).map(Object::toString).collect(Collectors.joining(",")));
    }

}
