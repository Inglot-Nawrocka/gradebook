package com.gradebook.data.repository;

import com.gradebook.data.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    UserCredential getByUsername(String name);
}
