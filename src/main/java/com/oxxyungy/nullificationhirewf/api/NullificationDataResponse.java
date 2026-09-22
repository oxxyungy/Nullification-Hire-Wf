package com.oxxyungy.nullificationhirewf.api;

import java.time.LocalDate;
import java.util.UUID;

public record NullificationDataResponse(
        LocalDate nullificationDate,
        String positionId,
        UUID signerPersonId,
        String signerEmployeeId
) {
}
