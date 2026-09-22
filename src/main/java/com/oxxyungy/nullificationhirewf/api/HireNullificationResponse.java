package com.oxxyungy.nullificationhirewf.api;

import com.oxxyungy.nullificationhirewf.domain.NullificationReason;
import com.oxxyungy.nullificationhirewf.domain.RequestStatus;

import java.time.Instant;
import java.util.UUID;

public record HireNullificationResponse(
        UUID workflowId,
        String hireId,
        UUID personId,
        NullificationReason reason,
        UUID requestedBy,
        String comment,
        RequestStatus status,
        Instant createdAt,
        NullificationDataResponse nullificationData
) {
}
