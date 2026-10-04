ALTER TABLE account
    ADD CONSTRAINT uk_account_user UNIQUE (user_id);