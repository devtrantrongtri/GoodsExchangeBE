package com.uth.BE.Repository;

import com.uth.BE.Entity.ws.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember,Integer> {
}
