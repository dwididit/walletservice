package dev.dwidi.walletservice.service.user;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.user.UserRequestDTO;
import dev.dwidi.walletservice.dto.user.UserResponseDTO;
import dev.dwidi.walletservice.entity.User;
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
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO userRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup UserRequestDTO
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFullName("John Doe");
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setPhoneNumber("+1234567890");

        // Setup User
        user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("+1234567890");
        user.setLastBalance(BigDecimal.ZERO);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            savedUser.setCreatedAt(LocalDateTime.now());
            savedUser.setUpdatedAt(LocalDateTime.now());
            return savedUser;
        });

        // Act
        BaseResponseDTO<UserResponseDTO> response = userService.createUser(userRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals("User successfully created", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1L, response.getData().getId());
        assertEquals(userRequestDTO.getFullName(), response.getData().getFullName());
        assertEquals(userRequestDTO.getEmail(), response.getData().getEmail());
        assertNotNull(response.getData().getCreatedAt());
        assertNotNull(response.getData().getUpdatedAt());

        verify(userRepository).existsByEmail(userRequestDTO.getEmail());
        verify(userRepository).existsByPhoneNumber(userRequestDTO.getPhoneNumber());
        verify(userRepository).save(any(User.class));
    }


    @Test
    void createUser_ExistingEmail_ThrowsException() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.createUser(userRequestDTO)
        );
        assertEquals("Email or phone number already exist", exception.getMessage());

        verify(userRepository).existsByEmail(userRequestDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        BaseResponseDTO<UserResponseDTO> response = userService.getUserById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("User successfully retrieved", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(user.getId(), response.getData().getId());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.getUserById(1L)
        );
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(1L);
    }

    @Test
    void editUser_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        BaseResponseDTO<UserResponseDTO> response = userService.editUser(1L, userRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("User successfully updated", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(userRequestDTO.getFullName(), response.getData().getFullName());
        assertEquals(userRequestDTO.getEmail(), response.getData().getEmail());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void editUser_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.editUser(1L, userRequestDTO)
        );
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        // Act
        BaseResponseDTO<UserResponseDTO> response = userService.deleteUser(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("User deleted successfully", response.getMessage());
        assertNull(response.getData());

        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.deleteUser(1L)
        );
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(1L);
        verify(userRepository, never()).delete(any(User.class));
    }
}