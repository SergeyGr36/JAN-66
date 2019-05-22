package com.ra.janus.developersteam.entity;

import java.util.Objects;

//@Entity
//@Table(name="MANAGERS")
public class Manager {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String phone;

    public Manager(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Manager() {
    }

    public Manager(Long id) {
        this.id = id;
    }

    public Manager(Long id, Manager manager) {
        this.id = id;
        this.name = manager.getName();
        this.email = manager.getEmail();
        this.phone = manager.getPhone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manager)) return false;
        Manager manager = (Manager) o;
        return Objects.equals(getId(), manager.getId()) &&
                Objects.equals(getName(), manager.getName()) &&
                Objects.equals(getEmail(), manager.getEmail()) &&
                Objects.equals(getPhone(), manager.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getPhone());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
