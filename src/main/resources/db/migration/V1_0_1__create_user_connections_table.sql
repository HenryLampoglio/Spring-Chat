CREATE type conn_type as enum
(
    'accepted',
    'pending',
    'blocked'
);

CREATE TABLE user_connections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id UUID REFERENCES users(id) NOT NULL,
    friend_id UUID REFERENCES users(id) NOT NULL,
    connection_status conn_type NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, friend_id)
);
