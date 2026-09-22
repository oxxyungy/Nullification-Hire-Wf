package com.oxxyungy.nullificationhirewf.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "nullification_request")
public class HireNullificationRequest {

    @Id
    private UUID id;

    @Column(name = "person_id", nullable = false, updatable = false)
    private UUID personId;

    @Column(name = "hire_id", nullable = false, updatable = false, length = 100)
    private String hireId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NullificationReason reason;

    @Column(name = "requested_by", nullable = false, updatable = false)
    private UUID requestedBy;

    @Column(length = 2000)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToOne(
            mappedBy = "request",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            optional = false
    )
    private NullificationData nullificationData;

    protected HireNullificationRequest() {
    }

    private HireNullificationRequest(
            UUID id,
            UUID personId,
            String hireId,
            NullificationReason reason,
            UUID requestedBy,
            String comment,
            Instant createdAt
    ) {
        this.id = id;
        this.personId = personId;
        this.hireId = hireId;
        this.reason = reason;
        this.requestedBy = requestedBy;
        this.comment = comment;
        this.status = RequestStatus.PENDING_VALIDATION;
        this.createdAt = createdAt;
    }

    public static HireNullificationRequest create(
            UUID personId,
            String hireId,
            NullificationReason reason,
            UUID requestedBy,
            String comment,
            Instant createdAt
    ) {
        return new HireNullificationRequest(
                UUID.randomUUID(),
                personId,
                hireId,
                reason,
                requestedBy,
                comment,
                createdAt
        );
    }

    public void attachNullificationData(NullificationData data) {
        this.nullificationData = data;
        data.assignTo(this);
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public String getHireId() {
        return hireId;
    }

    public NullificationReason getReason() {
        return reason;
    }

    public UUID getRequestedBy() {
        return requestedBy;
    }

    public String getComment() {
        return comment;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public NullificationData getNullificationData() {
        return nullificationData;
    }
}
