package dev.dwidi.walletservice.controller;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.user.UserRequestDTO;
import dev.dwidi.walletservice.dto.user.UserResponseDTO;
import dev.dwidi.walletservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private BaseResponseDTO<UserResponseDTO> baseResponse;
    private BaseResponseDTO<UserResponseDTO> errorResponse;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFullName("John Doe");
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setPhoneNumber("+1234567890");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setFullName("John Doe");
        userResponseDTO.setEmail("john.doe@example.com");
        userResponseDTO.setPhoneNumber("+1234567890");
        userResponseDTO.setLastBalance(BigDecimal.ZERO);
        userResponseDTO.setCreatedAt(LocalDateTime.now());
        userResponseDTO.setUpdatedAt(LocalDateTime.now());

        baseResponse = new BaseResponseDTO<>();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Success");
        baseResponse.setData(userResponseDTO);

        errorResponse = new BaseResponseDTO<>();
        errorResponse.setStatusCode(400);
        errorResponse.setMessage("Bad Request");
        errorResponse.setData(null);
    }

    @Test
    void createUser_Success() throws Exception {
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(baseResponse);

        mockMvc.perform(post("/api/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"John Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"+1234567890\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).createUser(any(UserRequestDTO.class));
    }




    @Test
    void getUserById_Success() throws Exception {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(baseResponse);

        mockMvc.perform(get("/api/v1/user/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/user/invalid"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUserById(any());
    }

    @Test
    void getUserById_UserNotFound_ShouldReturnNotFound() throws Exception {
        Long userId = 999L;
        when(userService.getUserById(userId)).thenReturn(errorResponse);

        mockMvc.perform(get("/api/v1/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Bad Request"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void editUser_Success() throws Exception {
        Long userId = 1L;
        when(userService.editUser(eq(userId), any(UserRequestDTO.class))).thenReturn(baseResponse);

        mockMvc.perform(put("/api/v1/user/edit/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"John Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"+1234567890\"}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).editUser(eq(userId), any(UserRequestDTO.class));
    }

    @Test
    void editUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Long userId = 1L;
        mockMvc.perform(put("/api/v1/user/edit/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid-email\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).editUser(any(), any());
    }

    @Test
    void editUser_UserNotFound_ShouldReturnNotFound() throws Exception {
        Long userId = 999L;
        when(userService.editUser(eq(userId), any(UserRequestDTO.class))).thenReturn(errorResponse);

        mockMvc.perform(put("/api/v1/user/edit/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"John Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"+1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Bad Request"));

        verify(userService, times(1)).editUser(eq(userId), any());
    }

    @Test
    void deleteUser_Success() throws Exception {
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn(baseResponse);

        mockMvc.perform(delete("/api/v1/user/delete/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUser_UserNotFound_ShouldReturnNotFound() throws Exception {
        Long userId = 999L;
        when(userService.deleteUser(userId)).thenReturn(errorResponse);

        mockMvc.perform(delete("/api/v1/user/delete/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Bad Request"));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUser_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/user/delete/invalid"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).deleteUser(any());
    }
}