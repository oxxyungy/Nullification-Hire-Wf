package com.oxxyungy.nullificationhirewf.application;

public class DuplicateHireNullificationException extends RuntimeException {
    public DuplicateHireNullificationException(String hireId) {
        super("A nullification workflow already exists for hireId=" + hireId);
    }
}
