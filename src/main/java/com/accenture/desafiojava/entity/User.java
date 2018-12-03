package com.accenture.desafiojava.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name= "USERS")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @NotBlank(message = "name can't be blank")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "email can't be blank")
    @Email(message = "invalid format")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "password can't be blank")
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Phone> phones;

    @Column(name = "created")
    @Type(type="timestamp")
    private Date created;

    @Column(name = "modified")
    @Type(type="timestamp")
    private Date modified;

    @Column(name = "last_login")
    @Type(type="timestamp")
    private Date last_login;

    @Column(name = "token")
    private String token;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getLast_login() {
        return last_login;
    }

    public void setLast_login(Date last_login) {
        this.last_login = last_login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
