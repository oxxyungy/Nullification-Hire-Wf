package com.oxxyungy.nullificationhirewf.application;

import com.oxxyungy.nullificationhirewf.api.CreateHireNullificationRequest;
import com.oxxyungy.nullificationhirewf.api.CreateHireNullificationResponse;
import com.oxxyungy.nullificationhirewf.domain.HireNullificationRequest;
import com.oxxyungy.nullificationhirewf.domain.NullificationData;
import com.oxxyungy.nullificationhirewf.infrastructure.HireNullificationRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class CreateHireNullificationService {

    private final HireNullificationRequestRepository repository;
    private final Clock clock;

    public CreateHireNullificationService(HireNullificationRequestRepository repository) {
        this.repository = repository;
        this.clock = Clock.systemUTC();
    }

    @Transactional
    public CreateHireNullificationResponse create(CreateHireNullificationRequest command) {
        if (repository.existsByHireId(command.hireId())) {
            throw new DuplicateHireNullificationException(command.hireId());
        }

        var request = HireNullificationRequest.create(
                command.personId(),
                command.hireId(),
                command.reason(),
                command.requestedBy(),
                command.comment(),
                clock.instant()
        );

        var data = new NullificationData(
                command.nullificationData().nullificationDate(),
                command.nullificationData().positionId(),
                command.nullificationData().signerPersonId(),
                command.nullificationData().signerEmployeeId()
        );
        request.attachNullificationData(data);

        var saved = repository.save(request);
        return new CreateHireNullificationResponse(saved.getId(), saved.getStatus(), saved.getCreatedAt());
    }
}
