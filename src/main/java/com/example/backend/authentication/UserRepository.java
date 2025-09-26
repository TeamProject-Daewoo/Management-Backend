package com.example.backend.authentication;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUsername(String username);
    
    List<User> findByRoleAndApprovalStatusIn(Role role, List<ApprovalStatus> statuses);

    List<User> findByRoleIn(List<Role> roles);
}
