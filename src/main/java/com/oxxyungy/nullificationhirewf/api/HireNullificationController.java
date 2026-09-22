package com.oxxyungy.nullificationhirewf.api;

import com.oxxyungy.nullificationhirewf.application.CreateHireNullificationService;
import com.oxxyungy.nullificationhirewf.application.FindHireNullificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hire-nullifications")
public class HireNullificationController {

    private final CreateHireNullificationService createService;
    private final FindHireNullificationService findService;

    public HireNullificationController(
            CreateHireNullificationService createService,
            FindHireNullificationService findService
    ) {
        this.createService = createService;
        this.findService = findService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateHireNullificationResponse create(
            @Valid @RequestBody CreateHireNullificationRequest request
    ) {
        return createService.create(request);
    }

    @GetMapping("/{workflowId}")
    public HireNullificationResponse findById(@PathVariable UUID workflowId) {
        return findService.findById(workflowId);
    }
}
