package dev.dwidi.walletservice.controller;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.transaction.TransactionRequestDTO;
import dev.dwidi.walletservice.dto.transaction.TransactionResponseDTO;
import dev.dwidi.walletservice.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/topup/{userId}")
    public BaseResponseDTO<TransactionResponseDTO> topUp(@PathVariable Long userId, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        log.info("Handling request to top up balance for user with ID: {}", userId);
        return transactionService.topUpBalance(userId, transactionRequestDTO);
    }

    @PostMapping("/refund/{userId}")
    public BaseResponseDTO<TransactionResponseDTO> refund(@PathVariable Long userId, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        log.info("Handling request to refund balance for user with ID: {}", userId);
        return transactionService.refundBalance(userId, transactionRequestDTO);
    }

    @PostMapping("/bill/{userId}")
    public BaseResponseDTO<TransactionResponseDTO> billPayment(@PathVariable Long userId, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        log.info("Handling request to make bill payment from user with ID: {}", userId);
        return transactionService.billPayment(userId, transactionRequestDTO);
    }
}
