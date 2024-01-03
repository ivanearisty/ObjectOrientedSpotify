package io.suape.ObjectOrientedSpotify.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
@Slf4j
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final User user;
    //Typically we will have a Role class to execute granted authority checks, but for now we don't need to mind that.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("Executed Granted Authority");
//
        return new HashSet<GrantedAuthority>();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { return this.user.isEnabled(); }

    //TODO: Here we can add different user tiers perhaps, and give different authorities that way. Also, we could just make different types of users for the app
}
