package com.goodrec.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodrec.user.dto.LoginRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.goodrec.security.SecurityConstants.HEADER_STRING;
import static com.goodrec.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            var credentialsString = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    credentialsString.getEmail(),
                    credentialsString.getPassword()
            );

            return this.getAuthenticationManager().authenticate(auth);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String token = tokenProvider.createToken(authResult);
        response.setHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
