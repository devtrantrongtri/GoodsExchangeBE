package com.uth.BE.Repository;

import com.uth.BE.Entity.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {

}
