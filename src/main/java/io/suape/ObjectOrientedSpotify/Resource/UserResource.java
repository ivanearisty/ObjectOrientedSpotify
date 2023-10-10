package io.suape.ObjectOrientedSpotify.Resource;

import io.suape.ObjectOrientedSpotify.DTO.UserDTO;
import io.suape.ObjectOrientedSpotify.Domain.HttpResponse;
import io.suape.ObjectOrientedSpotify.Domain.User;
import io.suape.ObjectOrientedSpotify.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping("/hello")
    public ResponseEntity<HttpResponse> sayHi(){
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Hello!")
                        .status(HttpStatus.ACCEPTED)
                        .statusCode(HttpStatus.ACCEPTED.value())
                        .build()
        );
    }
    //@Valid makes sure that the user completes all the required fields in Domain
    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user){
        UserDTO userDTO = userService.createUser(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDTO)) //TODO: Remove after testing
                        .message("User Created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    //TODO: this is going to have to get re-written so that we can support multiple URIs
    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/user/get/<userId>)") //TODO: Create a method that returns the URI of the resource (user) when you pass get and the userID
                .toUriString());
    }
}
