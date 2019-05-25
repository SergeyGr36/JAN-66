package com.ra.janus.developersteam.entity;

import java.util.Objects;

public class Manager {

    private long id;
    private String name;
    private String email;
    private String phone;

    public Manager(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Manager(Long id, String name, String email, String phone) {
        this(name, email, phone);
        this.id = id;
    }

    public Manager(Long id) {
        this(null, null, null);
        this.id = id;
    }

    public Manager(Long id, Manager manager) {
        this(manager.getName(), manager.getEmail(), manager.getPhone());
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return id == manager.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
