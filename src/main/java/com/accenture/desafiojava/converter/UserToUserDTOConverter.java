package com.accenture.desafiojava.converter;

import org.springframework.core.convert.converter.Converter;
import com.accenture.desafiojava.domain.UserDTO;
import com.accenture.desafiojava.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDTOConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convert(User source) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(source.getId());
        userDTO.setCreated(source.getCreated());
        userDTO.setModified(source.getModified());
        userDTO.setLast_login(source.getLast_login());
        userDTO.setToken(source.getToken());

        return userDTO;
    }
}
