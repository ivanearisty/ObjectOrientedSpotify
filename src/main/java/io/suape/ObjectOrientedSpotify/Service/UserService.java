package io.suape.ObjectOrientedSpotify.Service;

import io.suape.ObjectOrientedSpotify.DTO.UserDTO;
import io.suape.ObjectOrientedSpotify.Domain.User;

public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);

    void sendVerificationCode(UserDTO userDTO);
}
