package com.uth.BE.Entity.ws;
import com.uth.BE.Entity.model.ChannelType;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private int channelId;

    @Column(name = "channel_name", nullable = false, columnDefinition = "VARCHAR(255) default 'NoName'")
    private String channelName = "NoName";

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "ENUM('PUBLIC', 'PRIVATE', 'GROUP') default 'PUBLIC'")
    private ChannelType type;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private Set<ChannelMember> members = new HashSet<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private Set<Message> messages = new HashSet<>();

        // gettter and setter

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ChannelMember> getMembers() {
        return members;
    }

    public void setMembers(Set<ChannelMember> members) {
        this.members = members;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}


