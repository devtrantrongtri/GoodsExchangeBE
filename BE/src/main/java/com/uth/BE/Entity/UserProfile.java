package com.uth.BE.Entity;

import com.uth.BE.Entity.model.FileExtension;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private int profileId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
    @Column(name = "first_name")
    private String FirstName ;
    @Column(name = "last_name")
    private String  LastName ;
    @Column(name = "bio")
    @Lob
    private String bio;
    @Column(name = "profile_image_url")
    @Lob
    private String profileImageUrl;
    @Column(name = "created_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


}

