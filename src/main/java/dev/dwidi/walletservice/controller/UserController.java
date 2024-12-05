package dev.dwidi.walletservice.controller;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.user.UserRequestDTO;
import dev.dwidi.walletservice.dto.user.UserResponseDTO;
import dev.dwidi.walletservice.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public BaseResponseDTO<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        log.info("Handling request to create user {}", userRequestDTO);
        return userService.createUser(userRequestDTO);
    }

    @GetMapping("/{userId}")
    public BaseResponseDTO<UserResponseDTO> getUserById(@PathVariable Long userId) {
        log.info("Handling request to get user by ID {}", userId);
        return userService.getUserById(userId);
    }

    @PutMapping("/edit/{userId}")
    public BaseResponseDTO<UserResponseDTO> editUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Handling request to edit user {}", userId);
        return userService.editUser(userId, userRequestDTO);
    }

    @DeleteMapping("/delete/{userId}")
    public BaseResponseDTO<UserResponseDTO> deleteUser(@PathVariable Long userId) {
        log.info("Handling request to delete user id {}", userId);
        return userService.deleteUser(userId);
    }
}
