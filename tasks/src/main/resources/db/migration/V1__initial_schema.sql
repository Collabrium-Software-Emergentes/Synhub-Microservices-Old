CREATE TABLE members (
                         id BIGSERIAL PRIMARY KEY,

                         group_id BIGINT NULL,

                         created_at TIMESTAMP NOT NULL DEFAULT now(),
                         updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,

                       title VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,

                       status VARCHAR(50) NOT NULL,

                       due_date TIMESTAMP WITH TIME ZONE NOT NULL,

                       member_id BIGINT NULL,

                       group_id BIGINT NULL,

                       times_rearranged INTEGER NOT NULL DEFAULT 0,

                       time_passed BIGINT NOT NULL DEFAULT 0,

                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                       updated_at TIMESTAMP NOT NULL DEFAULT now(),

                       CONSTRAINT fk_tasks_member
                           FOREIGN KEY (member_id)
                               REFERENCES members(id)
                               ON DELETE SET NULL,

                       CONSTRAINT chk_task_status
                           CHECK (
                               status IN (
                                          'ON_HOLD',
                                          'IN_PROGRESS',
                                          'COMPLETED',
                                          'DONE',
                                          'EXPIRED'
                                   )
                               )
);
