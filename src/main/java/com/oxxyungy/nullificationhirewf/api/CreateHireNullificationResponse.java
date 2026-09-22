package com.oxxyungy.nullificationhirewf.api;

import com.oxxyungy.nullificationhirewf.domain.RequestStatus;

import java.time.Instant;
import java.util.UUID;

public record CreateHireNullificationResponse(
        UUID workflowId,
        RequestStatus status,
        Instant createdAt
) {
}
