package dev.dwidi.walletservice.service.transaction;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.transaction.TransactionRequestDTO;
import dev.dwidi.walletservice.dto.transaction.TransactionResponseDTO;

public interface TransactionService {
    BaseResponseDTO<TransactionResponseDTO> topUpBalance(Long userId, TransactionRequestDTO transactionRequestDTO);
    BaseResponseDTO<TransactionResponseDTO> refundBalance(Long userId, TransactionRequestDTO transactionRequestDTO);
    BaseResponseDTO<TransactionResponseDTO> billPayment(Long userId, TransactionRequestDTO transactionRequestDTO);
}
