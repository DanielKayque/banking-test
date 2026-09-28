CREATE TABLE account(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    amount_cent BIGINT NOT NULL,

    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT ck_amount_cent CHECK (amount_cent >= 0)
);