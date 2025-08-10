CREATE TABLE user_connections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id UUID REFERENCES users(id) NOT NULL,
    friend_id UUID REFERENCES users(id) NOT NULL,
    UNIQUE (user_id, friend_id)
);
