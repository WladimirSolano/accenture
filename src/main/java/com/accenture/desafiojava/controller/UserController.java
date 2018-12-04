package com.accenture.desafiojava.controller;


import com.accenture.desafiojava.converter.UserToUserDTOConverter;
import com.accenture.desafiojava.domain.LoginUserDTO;
import com.accenture.desafiojava.domain.MessageDTO;
import com.accenture.desafiojava.entity.User;
import com.accenture.desafiojava.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

import static com.accenture.desafiojava.constants.Constants.*;

@RestController
public class UserController {

    @Autowired
    IUserService iUserService;

    @Autowired
    UserToUserDTOConverter userToUserDTOConverter;

    @PostMapping(value = "/registro")
    @ResponseBody
    public ResponseEntity<?> saveUser (@RequestBody @Valid User user){

        User registeredUser = iUserService.getUserByEmail(user.getEmail());

        if (registeredUser != null) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMensaje(USER_EXIST_MESSAGE);

            return new ResponseEntity<>(messageDTO, HttpStatus.CONFLICT);
        } else {

            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();

            user.setCreated(new Date());
            user.setModified(new Date());
            user.setLast_login(new Date());
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            user.setToken(token);
            User userSaved = iUserService.save(user);

            return new ResponseEntity<>(userToUserDTOConverter.convert(userSaved), HttpStatus.OK);
        }

    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<?> login (@RequestBody LoginUserDTO loginUserDTO, HttpServletResponse response) {

        User registeredUser = iUserService.getUserByEmail(loginUserDTO.getEmail());

        if (registeredUser != null && BCrypt.checkpw(loginUserDTO.getPassword(), registeredUser.getPassword())) {

            registeredUser.setLast_login(new Date());
            registeredUser.setModified(new Date());

            if (System.currentTimeMillis() > registeredUser.getLast_login().getTime() + EXPIRATION_TIME){
                String token = Jwts.builder()
                        .setSubject(registeredUser.getEmail())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS512, SECRET)
                        .compact();
                registeredUser.setToken(token);
            }
            iUserService.save(registeredUser);
            response.addHeader(HEADER_STRING, registeredUser.getToken());
            return new ResponseEntity<>(userToUserDTOConverter.convert(registeredUser), HttpStatus.OK);
        } else {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMensaje(INVALID_DATA_MESSAGE);

            return new ResponseEntity<>(messageDTO, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/perfil-usuario/{id}")
    @ResponseBody
    public ResponseEntity<?> login (@PathVariable UUID id, HttpServletRequest request, HttpServletResponse response) {

        User registeredUser = iUserService.getUserById(id);
        MessageDTO messageDTO = new MessageDTO();
        String token = request.getHeader("token");

        if (registeredUser != null && token != null && token.equals(registeredUser.getToken())) {

            if (System.currentTimeMillis() > registeredUser.getLast_login().getTime() + EXPIRATION_TIME){
                messageDTO.setMensaje(INVALID_SESSION_MESSAGE);
                return new ResponseEntity<>(messageDTO, HttpStatus.UNAUTHORIZED);
            } else {
                response.addHeader(HEADER_STRING, registeredUser.getToken());
                return new ResponseEntity<>(userToUserDTOConverter.convert(registeredUser), HttpStatus.OK);
            }
        } else {
            messageDTO.setMensaje(UNAUTHORIZED_MESSAGE);
            return new ResponseEntity<>(messageDTO, HttpStatus.UNAUTHORIZED);
        }
    }
}