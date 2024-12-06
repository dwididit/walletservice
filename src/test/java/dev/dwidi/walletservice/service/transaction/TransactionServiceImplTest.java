package dev.dwidi.walletservice.service.transaction;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.transaction.TransactionRequestDTO;
import dev.dwidi.walletservice.dto.transaction.TransactionResponseDTO;
import dev.dwidi.walletservice.entity.Transaction;
import dev.dwidi.walletservice.entity.User;
import dev.dwidi.walletservice.enums.TransactionCategory;
import dev.dwidi.walletservice.repository.TransactionRepository;
import dev.dwidi.walletservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User user;
    private TransactionRequestDTO transactionRequestDTO;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        // Setup User
        user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("+1234567890");
        user.setLastBalance(BigDecimal.valueOf(1000));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Setup TransactionRequestDTO
        transactionRequestDTO = new TransactionRequestDTO();
        transactionRequestDTO.setAmount(BigDecimal.valueOf(100));

        // Setup Transaction
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setUser(user);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void topUpBalance_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L);
            savedTransaction.setCreatedAt(LocalDateTime.now());
            savedTransaction.setUpdatedAt(LocalDateTime.now());
            return savedTransaction;
        });
        when(userRepository.save(any(User.class))).thenReturn(user);

        BigDecimal initialBalance = user.getLastBalance();

        // Act
        BaseResponseDTO<TransactionResponseDTO> response = transactionService.topUpBalance(1L, transactionRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Top up successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(TransactionCategory.TOPUP, response.getData().getTransactionCategory());
        assertEquals(transactionRequestDTO.getAmount(), response.getData().getAmount());
        assertEquals(initialBalance.add(transactionRequestDTO.getAmount()), response.getData().getLastBalance());

        verify(userRepository).findById(1L);
        verify(transactionRepository).save(any(Transaction.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void topUpBalance_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                transactionService.topUpBalance(1L, transactionRequestDTO)
        );
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void refundBalance_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L);
            savedTransaction.setCreatedAt(LocalDateTime.now());
            savedTransaction.setUpdatedAt(LocalDateTime.now());
            return savedTransaction;
        });
        when(userRepository.save(any(User.class))).thenReturn(user);

        BigDecimal initialBalance = user.getLastBalance();

        // Act
        BaseResponseDTO<TransactionResponseDTO> response = transactionService.refundBalance(1L, transactionRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Refund successfully processed", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(TransactionCategory.REFUND, response.getData().getTransactionCategory());
        assertEquals(transactionRequestDTO.getAmount(), response.getData().getAmount());
        assertEquals(initialBalance.add(transactionRequestDTO.getAmount()), response.getData().getLastBalance());

        verify(userRepository).findById(1L);
        verify(transactionRepository).save(any(Transaction.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void refundBalance_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                transactionService.refundBalance(1L, transactionRequestDTO)
        );
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void billPayment_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L);
            savedTransaction.setCreatedAt(LocalDateTime.now());
            savedTransaction.setUpdatedAt(LocalDateTime.now());
            return savedTransaction;
        });
        when(userRepository.save(any(User.class))).thenReturn(user);

        BigDecimal initialBalance = user.getLastBalance();

        // Act
        BaseResponseDTO<TransactionResponseDTO> response = transactionService.billPayment(1L, transactionRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Bill payment successfully processed", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(TransactionCategory.BILLPAYMENT, response.getData().getTransactionCategory());
        assertEquals(transactionRequestDTO.getAmount(), response.getData().getAmount());
        assertEquals(initialBalance.subtract(transactionRequestDTO.getAmount()), response.getData().getLastBalance());

        verify(userRepository).findById(1L);
        verify(transactionRepository).save(any(Transaction.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void billPayment_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                transactionService.billPayment(1L, transactionRequestDTO)
        );
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void billPayment_InsufficientBalance_ThrowsException() {
        // Arrange
        user.setLastBalance(BigDecimal.valueOf(50)); // Set balance to 50
        transactionRequestDTO.setAmount(BigDecimal.valueOf(100)); // Try to pay 100
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                transactionService.billPayment(1L, transactionRequestDTO)
        );
        assertEquals("Insufficient balance", exception.getMessage());

        verify(userRepository).findById(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(userRepository, never()).save(any(User.class));
    }
}