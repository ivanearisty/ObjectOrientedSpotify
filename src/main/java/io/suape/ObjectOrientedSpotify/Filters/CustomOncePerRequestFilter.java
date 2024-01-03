package io.suape.ObjectOrientedSpotify.Filters;

import io.suape.ObjectOrientedSpotify.Infrastructure.Implementations.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Map.*;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOncePerRequestFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String[] PUBLIC_ROUTES = {"/user/login", "/user/register"};
    private final TokenProvider tokenProvider;
    protected static final String TOKEN_KEY = "token";
    protected static final String EMAIL_KEY = "email";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            Map<String, String> values = getRequestValues(request);
            String token = getToken(request);

            if(tokenProvider.isTokenValid(values.get(EMAIL_KEY), token)){
//                List<GrantedAuthority> authorityList
                Authentication authentication = tokenProvider.getAuthentication(values.get(EMAIL_KEY), null, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                //Remove everything to do with this thread because the request is not valid (no valid token)
                SecurityContextHolder.clearContext();
            }
            //Call other filters in the filter-chain so that other filters process the request
            filterChain.doFilter(request,response);
        }catch (Exception e){
            log.error(e.getMessage());
            processError(request,response,e);
        }
    }

    //TODO: implement error
    private void processError(HttpServletRequest request, HttpServletResponse response, Exception e) {
    }

    private String getToken(HttpServletRequest request) {
        return ofNullable(request.getHeader(AUTHORIZATION))
                .filter(header -> header.startsWith(TOKEN_PREFIX))
                .map(token -> token.replace(TOKEN_PREFIX, EMPTY)).get();
    }

    private Map<String, String> getRequestValues(HttpServletRequest request) {
        return of(EMAIL_KEY, tokenProvider.getSubject(getToken(request),request), TOKEN_KEY, getToken(request));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(AUTHORIZATION) == null ||
                !request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) ||
                request.getMethod().equalsIgnoreCase("OPTIONS") ||
                asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }
}
