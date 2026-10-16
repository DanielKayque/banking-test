CREATE TABLE transactions(
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount_cent BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL ,
    description VARCHAR(255),

    CONSTRAINT chk_type CHECK ( type IN ('DEPOSIT', 'WITHDRAW') ),
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account(id),
    CONSTRAINT chk_amount CHECK ( amount_cent > 0 )
)