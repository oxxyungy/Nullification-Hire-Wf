package com.oxxyungy.nullificationhirewf.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hire-nullifications")
public class HireNullificationController {

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateHireNullificationResponse create(@Valid @RequestBody CreateHireNullificationRequest request) {
        return new CreateHireNullificationResponse(
                UUID.randomUUID(),
                request.hireId(),
                "PENDING_VALIDATION"
        );
    }

    public record CreateHireNullificationRequest(
            @NotBlank String hireId,
            @NotBlank String reason,
            @NotBlank String requestedBy
    ) {
    }

    public record CreateHireNullificationResponse(
            UUID workflowId,
            String hireId,
            String status
    ) {
    }
}
