package com.oxxyungy.nullificationhirewf.domain;

public enum RequestStatus {
    PENDING_VALIDATION,
    VALIDATING,
    APPROVED,
    REJECTED,
    EXECUTING,
    COMPLETED,
    FAILED,
    CANCELLED
}
