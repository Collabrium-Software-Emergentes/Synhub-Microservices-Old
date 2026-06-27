ALTER TABLE groups
    ADD COLUMN public_id VARCHAR(255);

UPDATE groups
SET public_id = 'default'
WHERE public_id IS NULL;

ALTER TABLE groups
    ALTER COLUMN public_id SET NOT NULL;