package dev.dwidi.walletservice.dto.transaction;

import dev.dwidi.walletservice.enums.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDTO {
    private Long id;
    private TransactionCategory transactionCategory;
    private BigDecimal amount;
    private BigDecimal lastBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
