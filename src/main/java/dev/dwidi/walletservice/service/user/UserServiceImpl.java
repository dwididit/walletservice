package dev.dwidi.walletservice.service.user;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.user.UserRequestDTO;
import dev.dwidi.walletservice.dto.user.UserResponseDTO;
import dev.dwidi.walletservice.entity.User;
import dev.dwidi.walletservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public BaseResponseDTO<UserResponseDTO> createUser(UserRequestDTO userRequestDTO) {
        log.info("Processing to create user {}", userRequestDTO);

        // Find if user exist
        if (userRepository.existsByEmail(userRequestDTO.getEmail()) ||
                userRepository.existsByPhoneNumber(userRequestDTO.getPhoneNumber())) {
            throw new RuntimeException("Email or phone number already exist");
        }

        User newuser = new User();
        newuser.setFullName(userRequestDTO.getFullName());
        newuser.setEmail(userRequestDTO.getEmail());
        newuser.setPhoneNumber(userRequestDTO.getPhoneNumber());
        newuser.setLastBalance(BigDecimal.ZERO);

        userRepository.save(newuser);

        // Build the response
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(newuser.getId());
        userResponseDTO.setFullName(newuser.getFullName());
        userResponseDTO.setEmail(newuser.getEmail());
        userResponseDTO.setLastBalance(newuser.getLastBalance());
        userResponseDTO.setCreatedAt(newuser.getCreatedAt());
        userResponseDTO.setUpdatedAt(newuser.getUpdatedAt());

        return new BaseResponseDTO<>(HttpStatus.CREATED.value(), "User successfully created", userResponseDTO);
    }

    @Override
    public BaseResponseDTO<UserResponseDTO> getUserById(Long userId) {
        log.info("Processing to get user with id: {}", userId);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Build the response
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(existingUser.getId());
        userResponseDTO.setFullName(existingUser.getFullName());
        userResponseDTO.setEmail(existingUser.getEmail());
        userResponseDTO.setPhoneNumber(existingUser.getPhoneNumber());
        userResponseDTO.setLastBalance(existingUser.getLastBalance());
        userResponseDTO.setCreatedAt(existingUser.getCreatedAt());
        userResponseDTO.setUpdatedAt(existingUser.getUpdatedAt());

        return new BaseResponseDTO<>(HttpStatus.OK.value(), "User successfully retrieved", userResponseDTO);
    }

    @Override
    public BaseResponseDTO<UserResponseDTO> editUser(Long userId, UserRequestDTO userRequestDTO) {
        log.info("Processing to edit user with id : {}", userId);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFullName(userRequestDTO.getFullName());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPhoneNumber(userRequestDTO.getPhoneNumber());

        userRepository.save(existingUser);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(existingUser.getId());
        userResponseDTO.setFullName(existingUser.getFullName());
        userResponseDTO.setEmail(existingUser.getEmail());
        userResponseDTO.setPhoneNumber(existingUser.getPhoneNumber());
        userResponseDTO.setLastBalance(existingUser.getLastBalance());
        userResponseDTO.setCreatedAt(existingUser.getCreatedAt());
        userResponseDTO.setUpdatedAt(existingUser.getUpdatedAt());

        return new BaseResponseDTO<>(HttpStatus.OK.value(), "User successfully updated", userResponseDTO);
    }

    @Override
    public BaseResponseDTO<UserResponseDTO> deleteUser(Long userId) {
        log.info("Processing to delete user with id: {}", userId);

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(existingUser);

        return new BaseResponseDTO<>(HttpStatus.OK.value(), "User deleted successfully", null);
    }
}

