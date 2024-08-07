package com.uth.BE.Entity.ws;

import com.uth.BE.Entity.User;
import com.uth.BE.Entity.model.ChannelMemberId;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "channelMembers")
@IdClass(ChannelMemberId.class) // Composite key class
public class ChannelMember {

    @Id
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "joined_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedAt = new Date();

    // Getters and Setters

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }
}

