package dev.dwidi.walletservice.repository;

import dev.dwidi.walletservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
}
