package com.accenture.desafiojava.service;

import com.accenture.desafiojava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserService extends JpaRepository<User,Long > {

    User getUserByEmail(String email);
    User getUserById(UUID id);

}
