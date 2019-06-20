package com.ra.course.janus.traintickets.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;

    public User (Long id, User user) {
        this (id, user.getName(), user.getEmail(), user.getPassword());
    }
}
