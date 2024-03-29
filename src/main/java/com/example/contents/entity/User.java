package com.example.contents.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "user_table")
@EqualsAndHashCode
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Setter
    private Long id;

    @Column(unique = true)
    private String username;
    private String email;
    @Setter
    private String phone;
    @Setter
    private String bio;
    @Setter
    private String avatar;

    public User(String username, String email, String phone, String bio) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
    }
}
