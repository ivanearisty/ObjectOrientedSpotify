package io.suape.ObjectOrientedSpotify.Repository;

import io.suape.ObjectOrientedSpotify.DTO.UserDTO;
import io.suape.ObjectOrientedSpotify.Domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface UserRepository <T extends User>{
    /* Basic Crud Operations */
    T create (T data);
    Collection<T> list(int page, int pageSize);
    T get(String id);
    T update(T data);
    Boolean delete(String id);
    /* Basic Crud Operations */

    /* Additional Operations */
    public User getUserByEmail(String email);

    void sendVerificationCode(UserDTO userDTO);

    User verifyCode(String email, String code);

    /* Additional Operations */

}
