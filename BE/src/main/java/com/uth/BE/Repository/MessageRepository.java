package com.uth.BE.Repository;

import com.uth.BE.Entity.ws.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}
