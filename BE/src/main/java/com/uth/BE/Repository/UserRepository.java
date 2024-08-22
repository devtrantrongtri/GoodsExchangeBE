package com.uth.BE.Repository;


import com.uth.BE.Entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository  extends JpaRepository<User,Integer> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u " +
            "WHERE u.userId IN (" +
            "  SELECT cm.sender.userId FROM ChatMessage cm " +
            "  WHERE cm.sender.userId = :userId OR cm.recipient.userId = :userId " +
            "  UNION " +
            "  SELECT cm.recipient.userId FROM ChatMessage cm " +
            "  WHERE cm.sender.userId = :userId OR cm.recipient.userId = :userId " +
            ") " +
            "AND u.userId != :userId")
    List<User> findUsersWithMessages(@Param("userId") Integer userId);

}
