package com.ra.janus.hotel.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Client implements Serializable {

    static final Long serialVersionUID = 1L;

    private long id;
    private String fullName;
    private String telephone;
    private String email;
    private Date birthday;

    public Client(long id, String fullName, String telephone, String email, Date birthday) {
        this.id = id;
        this.fullName = fullName;
        this.telephone = telephone;
        this.email = email;
        this.birthday = birthday;
    }

    public Client(String fullName, String telephone, String email, Date birthday) {
        this.fullName = fullName;
        this.telephone = telephone;
        this.email = email;
        this.birthday = birthday;
    }

    public Client() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(fullName, client.fullName) &&
                Objects.equals(telephone, client.telephone) &&
                Objects.equals(email, client.email) &&
                Objects.equals(birthday, client.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, telephone, email, birthday);
    }
}
