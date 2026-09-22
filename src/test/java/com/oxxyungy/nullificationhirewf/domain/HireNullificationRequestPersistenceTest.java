package com.oxxyungy.nullificationhirewf.domain;

import com.oxxyungy.nullificationhirewf.infrastructure.HireNullificationRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class HireNullificationRequestPersistenceTest {

    @Autowired
    private HireNullificationRequestRepository repository;

    @Test
    void savesRequestWithOneToOneNullificationData() {
        var request = HireNullificationRequest.create(
                UUID.randomUUID(),
                "HIRE-2026-001245",
                NullificationReason.CANDIDATE_WITHDREW,
                UUID.randomUUID(),
                "Candidate withdrew consent",
                Instant.now()
        );
        request.attachNullificationData(new NullificationData(
                LocalDate.of(2026, 9, 22),
                "37654321",
                null,
                null
        ));

        var saved = repository.saveAndFlush(request);
        var restored = repository.findById(saved.getId()).orElseThrow();

        assertThat(restored.getStatus()).isEqualTo(RequestStatus.PENDING_VALIDATION);
        assertThat(restored.getNullificationData().getNullificationDate()).isEqualTo(LocalDate.of(2026, 9, 22));
    }
}
