CREATE TABLE nullification_request (
    id UUID PRIMARY KEY,
    person_id UUID NOT NULL,
    hire_id VARCHAR(100) NOT NULL,
    reason VARCHAR(50) NOT NULL,
    requested_by UUID NOT NULL,
    comment VARCHAR(2000),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uq_nullification_request_hire_id UNIQUE (hire_id),
    CONSTRAINT chk_nullification_request_status CHECK (
        status IN ('PENDING_VALIDATION', 'VALIDATING', 'APPROVED', 'REJECTED', 'EXECUTING', 'COMPLETED', 'FAILED', 'CANCELLED')
    ),
    CONSTRAINT chk_nullification_request_reason CHECK (
        reason IN ('CANDIDATE_WITHDREW', 'DUPLICATE_HIRE', 'DATA_ERROR', 'BUSINESS_CANCELLATION', 'OTHER')
    )
);

CREATE INDEX idx_nullification_request_person_id ON nullification_request (person_id);
CREATE INDEX idx_nullification_request_status ON nullification_request (status);
CREATE INDEX idx_nullification_request_created_at ON nullification_request (created_at DESC);

CREATE TABLE nullification_data (
    request_id UUID PRIMARY KEY,
    nullification_date DATE NOT NULL,
    position_id VARCHAR(20) NOT NULL,
    signer_person_id UUID NULL,
    signer_employee_id VARCHAR(20) NULL,
    CONSTRAINT fk_nullification_data_request
        FOREIGN KEY (request_id)
        REFERENCES nullification_request (id)
        ON DELETE CASCADE
);

COMMENT ON TABLE nullification_request IS 'Workflow заявки на аннулирование найма. Мастер-данные не дублируются.';
COMMENT ON COLUMN nullification_request.hire_id IS 'Внешний идентификатор процесса найма в мастер-системе.';
COMMENT ON COLUMN nullification_request.person_id IS 'Внешний идентификатор персоны в мастер-системе.';
COMMENT ON COLUMN nullification_request.requested_by IS 'Идентификатор пользователя, инициировавшего workflow.';
COMMENT ON TABLE nullification_data IS 'Ключевые данные аннулирования, принадлежащие workflow заявке.';
COMMENT ON COLUMN nullification_data.signer_person_id IS 'Персона подписанта; заполняется асинхронно при генерации документа.';
COMMENT ON COLUMN nullification_data.signer_employee_id IS 'Табельный номер подписанта; заполняется асинхронно при генерации документа.';
