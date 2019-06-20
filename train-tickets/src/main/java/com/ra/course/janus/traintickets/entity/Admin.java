package com.ra.course.janus.traintickets.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Admin {

    private Long id;
    private String name;
    private String lastName;
    private String password;

}
