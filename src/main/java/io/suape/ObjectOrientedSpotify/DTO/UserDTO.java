package io.suape.ObjectOrientedSpotify.DTO;

import lombok.Data;
import java.time.LocalDateTime;
@Data
public class UserDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean isNonLocked;
    private boolean isEnabled;
    private boolean isUsingMFA;
    private String profileImage;
    private LocalDateTime createdAt;
}
