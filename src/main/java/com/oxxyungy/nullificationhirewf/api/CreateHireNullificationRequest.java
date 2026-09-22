package com.oxxyungy.nullificationhirewf.api;

import com.oxxyungy.nullificationhirewf.domain.NullificationReason;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateHireNullificationRequest(
        @NotBlank @Size(max = 100) String hireId,
        @NotNull UUID personId,
        @NotNull NullificationReason reason,
        @NotNull UUID requestedBy,
        @Size(max = 2000) String comment,
        @NotNull @Valid NullificationDataRequest nullificationData
) {
}
