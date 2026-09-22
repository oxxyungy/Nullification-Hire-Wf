package com.oxxyungy.nullificationhirewf.application;

import java.util.UUID;

public class HireNullificationNotFoundException extends RuntimeException {

    public HireNullificationNotFoundException(UUID workflowId) {
        super("Hire nullification workflow not found: " + workflowId);
    }
}
