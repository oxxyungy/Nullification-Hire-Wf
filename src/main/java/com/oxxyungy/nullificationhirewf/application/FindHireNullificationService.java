package com.oxxyungy.nullificationhirewf.application;

import com.oxxyungy.nullificationhirewf.api.HireNullificationResponse;
import com.oxxyungy.nullificationhirewf.api.NullificationDataResponse;
import com.oxxyungy.nullificationhirewf.domain.HireNullificationRequest;
import com.oxxyungy.nullificationhirewf.domain.NullificationData;
import com.oxxyungy.nullificationhirewf.infrastructure.HireNullificationRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FindHireNullificationService {

    private final HireNullificationRequestRepository repository;

    public FindHireNullificationService(HireNullificationRequestRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public HireNullificationResponse findById(UUID workflowId) {
        var request = repository.findById(workflowId)
                .orElseThrow(() -> new HireNullificationNotFoundException(workflowId));

        return toResponse(request);
    }

    private HireNullificationResponse toResponse(HireNullificationRequest request) {
        var data = request.getNullificationData();

        return new HireNullificationResponse(
                request.getId(),
                request.getHireId(),
                request.getPersonId(),
                request.getReason(),
                request.getRequestedBy(),
                request.getComment(),
                request.getStatus(),
                request.getCreatedAt(),
                toDataResponse(data)
        );
    }

    private NullificationDataResponse toDataResponse(NullificationData data) {
        return new NullificationDataResponse(
                data.getNullificationDate(),
                data.getPositionId(),
                data.getSignerPersonId(),
                data.getSignerEmployeeId()
        );
    }
}
