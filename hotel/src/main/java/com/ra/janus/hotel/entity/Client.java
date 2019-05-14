package com.ra.janus.hotel.entity;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Client {

    private long id;
    private String firstName;
    private String secondName;
    private String lastName;
    private int age;
    private String telephone;
    private String email;
    private Date birthday;

}