package io.suape.ObjectOrientedSpotify.RowMapper;

import io.suape.ObjectOrientedSpotify.Domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .userId(resultSet.getString("user_id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .phoneNumber(resultSet.getString("phone_number"))
                .isNonLocked(resultSet.getBoolean("is_non_locked"))
                .isEnabled(resultSet.getBoolean("is_enabled"))
                .isUsingMFA(resultSet.getBoolean("is_using_mfa"))
                .profileImage(resultSet.getString("profile_image"))
                .createdAt(resultSet.getTimestamp("created_date").toLocalDateTime())
                .build();
    }
}
