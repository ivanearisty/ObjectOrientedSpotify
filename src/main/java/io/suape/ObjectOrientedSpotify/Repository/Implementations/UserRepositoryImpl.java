package io.suape.ObjectOrientedSpotify.Repository.Implementations;

import io.suape.ObjectOrientedSpotify.Domain.User;
import io.suape.ObjectOrientedSpotify.OOSExceptions.APIException;
import io.suape.ObjectOrientedSpotify.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static io.suape.ObjectOrientedSpotify.Queries.UserQueries.*;
@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {
    private final NamedParameterJdbcTemplate jdbc;
    @Override
    public User create(User user) {
        //check email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0){
            throw new APIException("Email already in use");
        }
        //TODO: get spotify credentials
        String spotifyUserId = "A 22 char string";

        //save new user
        try{
            SqlParameterSource parameters = getSQLParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters);
            user.setUserId(Objects.requireNonNull(spotifyUserId));
        }catch (EmptyResultDataAccessException exception){

        }catch (Exception exception){

        }
        //send verification
        //save url in verification table
        //send email to user with verification url
        //return user
        //if error throw exception with proper message
        return null;
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(String id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }

    private int getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

//    TODO:
    private SqlParameterSource getSQLParameterSource(User user) {
        return null;
    }
}
