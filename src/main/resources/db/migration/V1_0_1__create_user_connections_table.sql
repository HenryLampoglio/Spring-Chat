CREATE type conn_type as enum
(
    'accepted',
    'pending'
);

CREATE TABLE user_connections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    requester_id UUID REFERENCES users(id) NOT NULL,
    receiver_id UUID REFERENCES users(id) NOT NULL,
    connection_status conn_type NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (requester_id, receiver_id)
);
