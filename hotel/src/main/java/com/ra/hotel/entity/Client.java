package com.ra.hotel.entity;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Client implements Serializable {

    static final Long serialVersionUID = 1L;

    private long id;
    private String fullName;
    private String telephone;
    private String email;
    private Date birthday;

    public Client(String fullName, String telephone, String email, Date birthday) {
        this.fullName = fullName;
        this.telephone = telephone;
        this.email = email;
        this.birthday = birthday;
    }
}