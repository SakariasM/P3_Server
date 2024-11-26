package com.p3.Server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);    // Look in users table for row with username

    @Query("SELECT u FROM User u WHERE u.user_id = :user_id")
    Optional<User> findByUser_id(int user_id);

}
