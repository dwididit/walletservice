package dev.dwidi.walletservice.service.user;

import dev.dwidi.walletservice.dto.BaseResponseDTO;
import dev.dwidi.walletservice.dto.user.UserRequestDTO;
import dev.dwidi.walletservice.dto.user.UserResponseDTO;

public interface UserService {
    BaseResponseDTO<UserResponseDTO> createUser(UserRequestDTO userRequestDTO);
    BaseResponseDTO<UserResponseDTO> getUserById(Long userId);
    BaseResponseDTO<UserResponseDTO> editUser(Long userId, UserRequestDTO userRequestDTO);
    BaseResponseDTO<UserResponseDTO> deleteUser(Long userId);
}
