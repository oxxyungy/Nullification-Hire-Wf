package com.oxxyungy.nullificationhirewf.api;

import com.oxxyungy.nullificationhirewf.application.CreateHireNullificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hire-nullifications")
public class HireNullificationController {

    private final CreateHireNullificationService createService;

    public HireNullificationController(CreateHireNullificationService createService) {
        this.createService = createService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateHireNullificationResponse create(@Valid @RequestBody CreateHireNullificationRequest request) {
        return createService.create(request);
    }
}
