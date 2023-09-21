package io.suape.ObjectOrientedSpotify.Repository.Implementations;

import io.suape.ObjectOrientedSpotify.Domain.User;
import io.suape.ObjectOrientedSpotify.OOSExceptions.APIException;
import io.suape.ObjectOrientedSpotify.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.UUID;

import static io.suape.ObjectOrientedSpotify.Enums.VerificationType.ACCOUNT;
import static io.suape.ObjectOrientedSpotify.Queries.UserQueries.*;
import static java.util.Map.of;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {
    private final NamedParameterJdbcTemplate jdbc;
    //TODO: configure spring security encoder and bean
    private final BCryptPasswordEncoder encoder;
    @Override
    public User create(User user) {
        //check email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0){
            throw new APIException("Email already in use");
        }
        //save new user
        try{
            //TODO: get spotify credentials
            String spotifyUserId = "test";
            //set user id
            user.setUserId(spotifyUserId);
            //insert user
            SqlParameterSource parameterSource = getSQLParameterSource(user);
            jdbc.update(INSERT_USER_QUERY,parameterSource);
            //verification
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            //save url in verification table
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, of("userId", user.getUserId(), "url", verificationUrl));
            //send email with verification url
            //TODO: implement email service
            //emailService.sendVerificationURL(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNonLocked(true);
            //return new user
            return user;
        }catch (Exception exception){
            throw new APIException("Unknown error");
        }
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
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, of("email", email), Integer.class);
    }

    private SqlParameterSource getSQLParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("userId", user.getUserId())
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                // TODO: Add bean for encoder
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type){
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/verify/" + type + "/" + key)
                .toUriString();
    }
}
