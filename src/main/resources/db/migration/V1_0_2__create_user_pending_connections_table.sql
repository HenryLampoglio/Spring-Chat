CREATE TABLE user_pending_connections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    initiator_user_id UUID REFERENCES users(id) NOT NULL,
    requested_user_id UUID REFERENCES users(id) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (initiator_user_id, requested_user_id)
);
