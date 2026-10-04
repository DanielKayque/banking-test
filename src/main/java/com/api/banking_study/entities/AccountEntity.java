package com.api.banking_study.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Getter
public class AccountEntity {
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @Column(name = "amount_cent", nullable = false)
    private Long amountCent;

    public AccountEntity(UUID id, UserEntity user, Long amountCent) {
        this.id = id;
        this.user = user;
        this.amountCent = 0L;
    }

    public Long withdraw(Long amount){
        if (amount == null || amount <= 0 ) throw new IllegalArgumentException("Invalid amount");

        if (this.amountCent < amount) throw new IllegalArgumentException("Insufficient funds");

        this.amountCent -= amount;

        return this.amountCent;
    }

    public Long deposit(Long amount){
        if (amount == null || amount <= 0 ) throw new IllegalArgumentException("Invalid amount");

        this.amountCent += amount;

        return this.amountCent;
    }
}
