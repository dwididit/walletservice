package dev.dwidi.walletservice.entity;

import dev.dwidi.walletservice.enums.TransactionCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionCategory transactionCategory;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        validateBalance();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        validateBalance();
    }

    private void validateBalance() {
        if (this.user != null && this.amount != null) {
            BigDecimal currentBalance = this.user.getLastBalance();

            if (transactionCategory == TransactionCategory.BILLPAYMENT) {
                BigDecimal endBalance = currentBalance.subtract(this.amount);

                if (endBalance.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalStateException("Insufficient balance");
                }
            }
        }
    }
}
