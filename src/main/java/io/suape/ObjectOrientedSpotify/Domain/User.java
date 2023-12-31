package io.suape.ObjectOrientedSpotify.Domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
//TODO: change these to jakarta
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class User implements Serializable {
    private String userId;
    @NotEmpty(message = "First name can't be empty")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty")
    private String lastName;
    @NotEmpty(message = "Password can't be empty")
    private String password;
    @NotEmpty(message = "email can't be empty")
    @Email(message = "Please enter a valid email")
    private String email;
    private String phoneNumber;
    private boolean isNonLocked;
    private boolean isEnabled;
    private boolean isUsingMFA;
    private String profileImage;
    //did we implement this?
    private LocalDateTime createdAt;


}
