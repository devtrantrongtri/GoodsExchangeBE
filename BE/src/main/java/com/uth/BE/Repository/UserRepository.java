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
    @Query("SELECT DISTINCT CASE WHEN cm.sender.userId = :userId THEN cm.recipient ELSE cm.sender END " +
            "FROM ChatMessage cm " +
            "WHERE cm.sender.userId = :userId OR cm.recipient.userId = :userId")
    List<User> findUsersWithMessages(@Param("userId") Integer userId);
}
