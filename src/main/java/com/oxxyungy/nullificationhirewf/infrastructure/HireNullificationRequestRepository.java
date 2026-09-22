package com.oxxyungy.nullificationhirewf.infrastructure;

import com.oxxyungy.nullificationhirewf.domain.HireNullificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HireNullificationRequestRepository extends JpaRepository<HireNullificationRequest, UUID> {
    boolean existsByHireId(String hireId);
}
