package com.cardealer.repo;

import com.cardealer.model.Users;
import com.cardealer.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    List<Users> findAllByRole(Role role);
}