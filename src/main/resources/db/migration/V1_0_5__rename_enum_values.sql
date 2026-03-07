ALTER TYPE chat_auth.chat_type RENAME VALUE 'personal' TO 'PERSONAL';
ALTER TYPE chat_auth.chat_type RENAME VALUE 'group' TO 'GROUP';

ALTER TYPE chat_auth.access_type RENAME VALUE 'public' TO 'PUBLIC';
ALTER TYPE chat_auth.access_type RENAME VALUE 'private' TO 'PRIVATE';
ALTER TYPE chat_auth.access_type RENAME VALUE 'invite_only' TO 'INVITE_ONLY';

ALTER TYPE chat_auth.conn_type RENAME VALUE 'accepted' TO 'ACCEPTED';
ALTER TYPE chat_auth.conn_type RENAME VALUE 'pending' TO 'PENDING';