CREATE TABLE requests (
    id BIGSERIAL PRIMARY KEY,

    description TEXT NOT NULL,

    request_type VARCHAR(50) NOT NULL,

    request_status VARCHAR(50) NOT NULL,

    task_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_request_type
        CHECK (
            request_type IN (
                'SUBMISSION',
                'MODIFICATION',
                'EXPIRED'
            )
        ),

    CONSTRAINT chk_request_status
        CHECK (
            request_status IN (
                'PENDING',
                'APPROVED',
                'REJECTED'
            )
        ),

    CONSTRAINT chk_task_id_positive
        CHECK (task_id > 0)
);

CREATE INDEX idx_requests_task_id
    ON requests(task_id);

CREATE INDEX idx_requests_request_status
    ON requests(request_status);

CREATE INDEX idx_requests_request_type
    ON requests(request_type);