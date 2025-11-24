-- Platform-specific data files are used:
-- * data-h2.sql       -> used by default (spring.sql.init.platform=h2)
-- * data-postgres.sql -> used by integration tests (BasePostgresIT sets platform=postgres)
-- This file must contain at least one harmless statement to avoid Spring's
-- SQL initializer failing when a file is present but empty. Do NOT add
-- Postgres-specific statements (like ON CONFLICT) here.

SELECT 1;