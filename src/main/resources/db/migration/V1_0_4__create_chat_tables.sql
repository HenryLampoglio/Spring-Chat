CREATE type chat_type as enum
(
    'personal',
    'group'
);

create type access_type as enum
(
    'public',
    'private',
    'invite_only'
);

CREATE TABLE chats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    chat_type chat_type NOT NULL,
    name VARCHAR(150) NULL,
    access_type access_type NOT NULL,
    description VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE user_chats(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id REFERENCES users(id) NOT NULL,
    chat_id UUID REFERENCES chats(id) NOT NULL
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, chat_id)
);