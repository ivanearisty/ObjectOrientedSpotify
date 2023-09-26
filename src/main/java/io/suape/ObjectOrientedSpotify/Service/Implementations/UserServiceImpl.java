package io.suape.ObjectOrientedSpotify.Service.Implementations;

import io.suape.ObjectOrientedSpotify.DTO.DTOMapper;
import io.suape.ObjectOrientedSpotify.DTO.UserDTO;
import io.suape.ObjectOrientedSpotify.Domain.User;
import io.suape.ObjectOrientedSpotify.Repository.UserRepository;
import io.suape.ObjectOrientedSpotify.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userUserRepository;
    @Override
    public UserDTO createUser(User user) {
        return DTOMapper.fromUser(userUserRepository.create(user));
    }
}