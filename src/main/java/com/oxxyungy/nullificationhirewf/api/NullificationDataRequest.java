package com.oxxyungy.nullificationhirewf.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record NullificationDataRequest(
        @NotNull LocalDate nullificationDate,
        @NotBlank @Size(max = 20) String positionId,
        UUID signerPersonId,
        @Size(max = 20) String signerEmployeeId
) {
}
