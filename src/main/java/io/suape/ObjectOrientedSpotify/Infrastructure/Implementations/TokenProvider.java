package io.suape.ObjectOrientedSpotify.Infrastructure.Implementations;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.suape.ObjectOrientedSpotify.Domain.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;

import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.*;
import static java.lang.System.currentTimeMillis;
@Log4j
@Component
public class TokenProvider {
    public static final long ACCESS_EXPIRATION = 1_800_000;
    public static final long REFRESH_EXPIRATION = 604_800_000;
    public static final String ISSUER = "Object Oriented Spotify";
    public static final String CANNOT_BE_VERIFIED = "Token cannot be verified";
    @Value("${jwt.secret}")
    private String secret;

    public  String createAccessToken (UserPrincipal userPrincipal){
        String [] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(ISSUER)
                .withAudience("")
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim("authorities", claims)
                .withExpiresAt(new Date(currentTimeMillis() + ACCESS_EXPIRATION))
                .sign(HMAC512(secret.getBytes()));
    }

    public  String createRefreshToken (UserPrincipal userPrincipal){
        String [] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(ISSUER)
                .withAudience("")
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(currentTimeMillis() + REFRESH_EXPIRATION))
                .sign(HMAC512(secret.getBytes()));
    }


    private String [] getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    private JWTVerifier getJWTVerifier(){
        JWTVerifier verifier;
        try{
            Algorithm algorithm = HMAC512(secret.getBytes());
            verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        }catch (JWTVerificationException exception){
            throw new JWTVerificationException(CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

    public Authentication getAuthentication(String email, List<GrantedAuthority> authorityList, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, null, authorityList);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }

    public boolean isTokenValid(String email, String token){
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(email) && !isTokenExpired(verifier, token);
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    public String getSubject(String token, HttpServletRequest request){
        JWTVerifier verifier = getJWTVerifier();
        try{
            return verifier.verify(token).getSubject();
        }catch (TokenExpiredException exception){
            request.setAttribute("expiredMessage", exception.getMessage());
        }catch (InvalidClaimException exception){
            request.setAttribute("invalidClaim", exception.getMessage());
        }

        return "";
    }

}









