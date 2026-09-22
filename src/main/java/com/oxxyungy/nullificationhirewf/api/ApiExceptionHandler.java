package com.oxxyungy.nullificationhirewf.api;

import com.oxxyungy.nullificationhirewf.application.DuplicateHireNullificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DuplicateHireNullificationException.class)
    ProblemDetail handleDuplicate(DuplicateHireNullificationException exception) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problem.setTitle("Hire nullification already exists");
        problem.setType(URI.create("https://nullification-hire-wf/errors/duplicate-hire-nullification"));
        return problem;
    }
}
