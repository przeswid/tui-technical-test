package com.tui.proof.common.validator;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PilotesQuantityInRangeTest {

    @Test
    void shouldAccept5Pilotes_whenAvailableQuantitiesAre5_10_15() {
        // Given
        Integer pilotesQuantity = 5;
        Integer[] availablePilotesQuantities = new Integer[]{5, 10, 15};
        PilotesQuantityInRangeImpl validator = new PilotesQuantityInRangeImpl(availablePilotesQuantities);
        // When
        boolean validatorResult = validator.isValid(pilotesQuantity, mockValidationContext());

        // Then
        assertTrue(validatorResult);
    }

    @Test
    void shouldNotAccept7Pilotes_whenAvailableQuantitiesAre5_10_15() {
        // Given
        Integer pilotesQuantity = 7;
        Integer[] availablePilotesQuantities = new Integer[]{5, 10, 15};
        PilotesQuantityInRangeImpl validator = new PilotesQuantityInRangeImpl(availablePilotesQuantities);
        // When
        boolean validatorResult = validator.isValid(pilotesQuantity, mockValidationContext());

        // Then
        assertFalse(validatorResult);
    }

    @Test
    void shouldNotAcceptNoPilotes_whenAvailableQuantitiesAre5_10_15() {
        // Given
        Integer pilotesQuantity = null;
        Integer[] availablePilotesQuantities = new Integer[]{5, 10, 15};
        PilotesQuantityInRangeImpl validator = new PilotesQuantityInRangeImpl(availablePilotesQuantities);

        // When
        boolean validatorResult = validator.isValid(pilotesQuantity, mockValidationContext());

        // Then
        assertFalse(validatorResult);
    }

    @Test
    void shouldNotAccept5Pilotes_whenAvailableQuantitiesAreEmpty() {
        // Given
        Integer pilotesQuantity = 5;
        Integer[] availablePilotesQuantities = new Integer[]{};
        PilotesQuantityInRangeImpl validator = new PilotesQuantityInRangeImpl(availablePilotesQuantities);
        // When
        boolean validatorResult = validator.isValid(pilotesQuantity, mockValidationContext());

        // Then
        assertFalse(validatorResult);
    }


    private ConstraintValidatorContext mockValidationContext() {
        ConstraintValidatorContext mockedConstraintValidator = mock(ConstraintValidatorContext.class);
        when(mockedConstraintValidator.buildConstraintViolationWithTemplate(Mockito.anyString()))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
        return mockedConstraintValidator;
    }
}
