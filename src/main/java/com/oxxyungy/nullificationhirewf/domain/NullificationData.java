package com.oxxyungy.nullificationhirewf.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "nullification_data")
public class NullificationData {

    @Id
    @Column(name = "request_id")
    private UUID requestId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "request_id", nullable = false)
    private HireNullificationRequest request;

    @Column(name = "nullification_date", nullable = false)
    private LocalDate nullificationDate;

    @Column(name = "position_id", nullable = false, length = 20)
    private String positionId;

    @Column(name = "signer_person_id")
    private UUID signerPersonId;

    @Column(name = "signer_employee_id", length = 20)
    private String signerEmployeeId;

    protected NullificationData() {
    }

    public NullificationData(
            LocalDate nullificationDate,
            String positionId,
            UUID signerPersonId,
            String signerEmployeeId
    ) {
        this.nullificationDate = nullificationDate;
        this.positionId = positionId;
        this.signerPersonId = signerPersonId;
        this.signerEmployeeId = signerEmployeeId;
    }

    void assignTo(HireNullificationRequest request) {
        this.request = request;
    }

    public LocalDate getNullificationDate() {
        return nullificationDate;
    }

    public String getPositionId() {
        return positionId;
    }

    public UUID getSignerPersonId() {
        return signerPersonId;
    }

    public String getSignerEmployeeId() {
        return signerEmployeeId;
    }
}
