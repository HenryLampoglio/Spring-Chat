CREATE TYPE chat_type AS ENUM ('personal', 'group');
CREATE TYPE access_type AS ENUM ('public', 'private', 'invite_only');

CREATE TABLE chats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    chat_type chat_type NOT NULL,
    chat_name VARCHAR(150),
    access_type access_type NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL, -- Recomendo adicionar REFERENCES users(id) aqui também
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE user_chats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id), -- Adicionado o tipo UUID
    chat_id UUID NOT NULL REFERENCES chats(id), -- Adicionada a vírgula faltante
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, chat_id)
);