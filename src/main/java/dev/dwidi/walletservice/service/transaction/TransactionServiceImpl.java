package dev.dwidi.walletservice.service.transaction;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.transaction.TransactionRequestDTO;
import dev.dwidi.walletservice.dto.transaction.TransactionResponseDTO;
import dev.dwidi.walletservice.entity.Transaction;
import dev.dwidi.walletservice.entity.User;
import dev.dwidi.walletservice.enums.TransactionCategory;
import dev.dwidi.walletservice.repository.TransactionRepository;
import dev.dwidi.walletservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public BaseResponseDTO<TransactionResponseDTO> topUpBalance(Long userId, TransactionRequestDTO transactionRequestDTO) {
        log.info("Processing top up balance from user with ID: {}", userId);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction topUp = new Transaction();
        topUp.setUser(existingUser);
        topUp.setAmount(transactionRequestDTO.getAmount());
        topUp.setTransactionCategory(TransactionCategory.TOPUP);

        // Update balance on user table
        BigDecimal newBalance = existingUser.getLastBalance().add(transactionRequestDTO.getAmount());
        existingUser.setLastBalance(newBalance);

        // Save to database
        transactionRepository.save(topUp);
        userRepository.save(existingUser);

        // Build the response
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setId(topUp.getId());
        transactionResponseDTO.setTransactionCategory(TransactionCategory.TOPUP);
        transactionResponseDTO.setAmount(transactionRequestDTO.getAmount());
        transactionResponseDTO.setLastBalance(existingUser.getLastBalance());
        transactionResponseDTO.setCreatedAt(topUp.getCreatedAt());
        transactionResponseDTO.setUpdatedAt(topUp.getUpdatedAt());

        return new BaseResponseDTO<>(HttpStatus.OK.value(), "Top up successfully", transactionResponseDTO);
    }

    @Override
    public BaseResponseDTO<TransactionResponseDTO> refundBalance(Long userId, TransactionRequestDTO transactionRequestDTO) {
        log.info("Processing refund balance to user with ID: {}", userId);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction refund = new Transaction();
        refund.setUser(existingUser);
        refund.setAmount(transactionRequestDTO.getAmount());
        refund.setTransactionCategory(TransactionCategory.REFUND);

        // Update balance
        BigDecimal newBalance = existingUser.getLastBalance().add(transactionRequestDTO.getAmount());
        existingUser.setLastBalance(newBalance);

        // Save to database
        transactionRepository.save(refund);
        userRepository.save(existingUser);

        // Build the response
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setId(refund.getId());
        transactionResponseDTO.setTransactionCategory(TransactionCategory.REFUND);
        transactionResponseDTO.setAmount(transactionRequestDTO.getAmount());
        transactionResponseDTO.setLastBalance(existingUser.getLastBalance());
        transactionResponseDTO.setCreatedAt(refund.getCreatedAt());
        transactionResponseDTO.setUpdatedAt(refund.getUpdatedAt());

        return new BaseResponseDTO<>(HttpStatus.OK.value(), "Refund successfully processed", transactionResponseDTO);
    }

    @Override
    public BaseResponseDTO<TransactionResponseDTO> billPayment(Long userId, TransactionRequestDTO transactionRequestDTO) {
        log.info("Processing bill payment to user with ID: {}", userId);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (existingUser.getLastBalance().compareTo(transactionRequestDTO.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Transaction billPayment = new Transaction();
        billPayment.setUser(existingUser);
        billPayment.setAmount(transactionRequestDTO.getAmount());
        billPayment.setTransactionCategory(TransactionCategory.BILLPAYMENT);

        // Update balance
        BigDecimal newBalance = existingUser.getLastBalance().subtract(transactionRequestDTO.getAmount());
        existingUser.setLastBalance(newBalance);

        // Save to database
        transactionRepository.save(billPayment);
        userRepository.save(existingUser);

        // Build the response
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setId(billPayment.getId());
        transactionResponseDTO.setTransactionCategory(TransactionCategory.BILLPAYMENT);
        transactionResponseDTO.setAmount(transactionRequestDTO.getAmount());
        transactionResponseDTO.setLastBalance(existingUser.getLastBalance());
        transactionResponseDTO.setCreatedAt(billPayment.getCreatedAt());
        transactionResponseDTO.setUpdatedAt(billPayment.getUpdatedAt());

        return new BaseResponseDTO<>(HttpStatus.OK.value(), "Bill payment successfully processed", transactionResponseDTO);
    }
}
