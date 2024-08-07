package com.uth.BE.Repository;

import com.uth.BE.Entity.ws.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<Channel, Long> {
}
