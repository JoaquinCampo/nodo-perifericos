-- Flyway migration to create initial schema matching Prisma schema

-- Clinic table
CREATE TABLE clinic (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- User table (renamed from 'user' to 'user_table' as 'user' is reserved in PostgreSQL)
CREATE TABLE user_table (
    id VARCHAR(255) PRIMARY KEY,
    ci VARCHAR(255),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    address VARCHAR(255),
    date_of_birth TIMESTAMP,
    email_verified TIMESTAMP,
    password VARCHAR(255),
    image VARCHAR(255),
    clinic_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_clinic FOREIGN KEY (clinic_id) REFERENCES clinic(id) ON DELETE CASCADE
);

-- Unique constraints for User
CREATE UNIQUE INDEX unique_email_per_clinic ON user_table(email, clinic_id);
CREATE UNIQUE INDEX unique_ci_per_clinic ON user_table(ci, clinic_id) WHERE ci IS NOT NULL;
CREATE UNIQUE INDEX unique_phone_per_clinic ON user_table(phone, clinic_id) WHERE phone IS NOT NULL;
CREATE INDEX idx_clinic_id ON user_table(clinic_id);

-- Health Worker table
CREATE TABLE health_worker (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_health_worker_user FOREIGN KEY (user_id) REFERENCES user_table(id) ON DELETE CASCADE
);

-- Clinic Admin table
CREATE TABLE clinic_admin (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_clinic_admin_user FOREIGN KEY (user_id) REFERENCES user_table(id) ON DELETE CASCADE
);

-- Speciality table
CREATE TABLE speciality (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Health Worker Speciality join table
CREATE TABLE health_worker_speciality (
    id VARCHAR(255) PRIMARY KEY,
    health_worker_id VARCHAR(255) NOT NULL,
    speciality_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_hw_speciality_health_worker FOREIGN KEY (health_worker_id) REFERENCES health_worker(id) ON DELETE CASCADE,
    CONSTRAINT fk_hw_speciality_speciality FOREIGN KEY (speciality_id) REFERENCES speciality(id) ON DELETE CASCADE
);

-- Clinical Document table
CREATE TABLE clinical_document (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    content TEXT,
    content_type VARCHAR(255),
    content_url VARCHAR(255),
    health_user_ci VARCHAR(255) NOT NULL,
    clinic_id VARCHAR(255) NOT NULL,
    health_worker_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_clinical_document_clinic FOREIGN KEY (clinic_id) REFERENCES clinic(id) ON DELETE CASCADE,
    CONSTRAINT fk_clinical_document_health_worker FOREIGN KEY (health_worker_id) REFERENCES health_worker(id)
);

-- Configuration table
CREATE TABLE configuration (
    id VARCHAR(255) PRIMARY KEY,
    clinic_id VARCHAR(255) NOT NULL UNIQUE,
    portal_title VARCHAR(255) NOT NULL DEFAULT 'Portal de Clínica',
    sidebar_text_color VARCHAR(255) NOT NULL DEFAULT '#111827',
    sidebar_background_color VARCHAR(255) NOT NULL DEFAULT '#F1F5F9',
    background_color VARCHAR(255) NOT NULL DEFAULT '#F8FAFC',
    card_background_color VARCHAR(255) NOT NULL DEFAULT '#FFFFFF',
    card_text_color VARCHAR(255) NOT NULL DEFAULT '#111827',
    icon_text_color VARCHAR(255) NOT NULL DEFAULT '#FFFFFF',
    icon_background_color VARCHAR(255) NOT NULL DEFAULT '#3B82F6',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_configuration_clinic FOREIGN KEY (clinic_id) REFERENCES clinic(id) ON DELETE CASCADE
);

-- Account table (for NextAuth compatibility)
CREATE TABLE account (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    provider VARCHAR(255) NOT NULL,
    provider_account_id VARCHAR(255) NOT NULL,
    refresh_token TEXT,
    access_token TEXT,
    expires_at INTEGER,
    token_type VARCHAR(255),
    scope VARCHAR(255),
    id_token TEXT,
    session_state VARCHAR(255),
    refresh_token_expires_in INTEGER,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES user_table(id) ON DELETE CASCADE,
    CONSTRAINT uk_account_provider UNIQUE (provider, provider_account_id)
);

-- Session table (for NextAuth compatibility)
CREATE TABLE session (
    id VARCHAR(255) PRIMARY KEY,
    session_token VARCHAR(255) NOT NULL UNIQUE,
    user_id VARCHAR(255) NOT NULL,
    expires TIMESTAMP NOT NULL,
    CONSTRAINT fk_session_user FOREIGN KEY (user_id) REFERENCES user_table(id) ON DELETE CASCADE
);

-- Verification Token table (for NextAuth compatibility)
CREATE TABLE verification_token (
    identifier VARCHAR(255) NOT NULL,
    token VARCHAR(255) PRIMARY KEY,
    expires TIMESTAMP NOT NULL,
    CONSTRAINT uk_verification_token UNIQUE (identifier, token)
);

