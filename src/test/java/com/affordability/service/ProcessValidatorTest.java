package com.affordability.service;

import com.affordability.exception.AgreementRecentlyVerifiedException;
import com.affordability.service.impl.ProcessValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProcessValidatorTest {

    @Test
    @DisplayName("should throw an exception if same agreement is processed immediately")
    void shouldThrowAnExceptionIfSameAgreementIsProcessedImmediately() {
        var agreementNumber = "ABC123";

        var repository = new ProcessStartedRepositoryMock();
        repository.setupRecordToBeReturnedInFindRecordOrAddNewOneFunction(agreementNumber, Instant.now(), true);

        var processor = new ProcessValidator(repository);

        assertThrows(AgreementRecentlyVerifiedException.class, () -> processor.checkVerificationProcessStarted(agreementNumber));
    }

    @Test
    @DisplayName("should NOT throw an exception if same agreement is processed late in time")
    void shouldNotThrowAnExceptionIfSameAgreementIsProcessedLateInTime() {
        var agreementNumber = "ABC123";

        var repository = new ProcessStartedRepositoryMock();
        repository.setupRecordToBeReturnedInFindRecordOrAddNewOneFunction(agreementNumber, Instant.now().minusSeconds(100), true);

        var processor = new ProcessValidator(repository);

        assertDoesNotThrow(() -> processor.checkVerificationProcessStarted(agreementNumber));
    }

    @Test
    @DisplayName("should NOT throw an exception if different agreements are processed immediately")
    void shouldNotThrowAnExceptionIfDifferentAgreementsAreProcessedImmediately() {
        var repository = new ProcessStartedRepositoryMock();
        var agreementNumberOne = "ABC";
        var agreementNumberTwo = "DEF";

        var processor = new ProcessValidator(repository);

        repository.setupRecordToBeReturnedInFindRecordOrAddNewOneFunction(agreementNumberOne, Instant.now(), false);

        assertDoesNotThrow(() -> processor.checkVerificationProcessStarted(agreementNumberOne));

        repository.setupRecordToBeReturnedInFindRecordOrAddNewOneFunction(agreementNumberTwo, Instant.now(), false);

        assertDoesNotThrow(() -> processor.checkVerificationProcessStarted(agreementNumberTwo));
    }
}
