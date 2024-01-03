package io.suape.ObjectOrientedSpotify.Repository.Implementations;

import io.suape.ObjectOrientedSpotify.DTO.UserDTO;
import io.suape.ObjectOrientedSpotify.Domain.User;
import io.suape.ObjectOrientedSpotify.Domain.UserPrincipal;
import io.suape.ObjectOrientedSpotify.OOSExceptions.APIException;
import io.suape.ObjectOrientedSpotify.Repository.UserRepository;
import io.suape.ObjectOrientedSpotify.RowMapper.UserRowMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import static io.suape.ObjectOrientedSpotify.Enums.VerificationType.ACCOUNT;
import static io.suape.ObjectOrientedSpotify.Queries.UserQueries.*;
import static java.util.Map.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {
    private final NamedParameterJdbcTemplate jdbc;

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
            //TODO: implement email service. 
            // NOTE: Implementation of how emails actually get sent should be in emailRepository or emailInfrastructure;
            // However, bubbling up to emailService and then having emailService communicate with the implementation is NECESSARY.
            // USER REPOSITORY SHOULD ONLY KNOW HOW TO DO THINGS FOR A USER, AND LEARNING HOW TO SEND EMAILS IS NOT ONE OF THEM
            //emailService.sendVerificationURL(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(true); //this should be false on prod
            user.setNonLocked(true);
            log.info("Created new user " + user.getFirstName());
            //return new user
            return user;
        }catch (Exception exception){
            log.error(exception.getMessage());
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);
        if(user == null){
            log.error("Repository loadUserByUsername did not find user email: " + email);
            throw new UsernameNotFoundException("User not found");
        }else{
            log.info("Repository loadUserByUsername found user email: " + email);
            return new UserPrincipal(user);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try{
            User user = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());
            log.info("Repository getUserByEmail found user email: " + email);
            return user;
        }catch (EmptyResultDataAccessException exception){
            throw new APIException("No users found");
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new APIException("Unknown error");
        }
    }


    /**
     * This method sends a verification code to a user.
     * First, it deletes any code that the user might have already received from the database.
     * Then, it generates a new code and sends it to the user utilizing a sendSMS service
     * @param userDTO dto representing the user that will be receiving the message
    */
    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        String expirationDate =  format(addDays(new Date(),1), "yyyy-MM-dd hh:mm:ss");
        String verificationCode = randomAlphabetic(8).toUpperCase();
        try{
            jdbc.update(DELETE_VERIFICATION_CODE_BY_USER_ID, of("userId", userDTO.getUserId()));
            jdbc.update(INSERT_VERIFICATION_CODE_QUERY, of("userId", userDTO.getUserId(), "code", verificationCode, "expirationDate", expirationDate));
//            sendSMS(userDTO.getPhoneNumber(), "From: Object Oriented Spotify \nYour Verification Code is:\n" + verificationCode);
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new APIException("Verification Code for MFA Failed");
        }
    }

    @Override
    public User verifyCode(String email, String code) {
        try {
            User userByCode = jdbc.queryForObject(SELECT_USER_BY_USER_CODE_QUERY, of("code", code), new UserRowMapper());
            User userByEmail = jdbc.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());

            if(userByCode.getEmail().equalsIgnoreCase(userByEmail.getEmail())) return userByCode;
            else throw new APIException("Invalid Code");
        }catch (EmptyResultDataAccessException exception){
            throw new APIException("Unable to find records");
        }catch (Exception e){
            throw new APIException("An error occurred when trying to verify user");
        }
    }
}
