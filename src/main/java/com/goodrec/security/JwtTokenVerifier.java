package com.goodrec.security;

import com.goodrec.user.domain.UserDetailsServiceImpl;
import com.goodrec.user.domain.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.goodrec.security.JwtConstants.HEADER_STRING;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenVerifier.class);

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenVerifier(TokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = request.getHeader(HEADER_STRING);
            String jwt = tokenProvider.getJwtFromHeader(header);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

                UUID uuid = tokenProvider.getUserUUIDFromToken(jwt);
                UserPrincipal principal = userDetailsService.loadUserByUUID(uuid);

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        principal.getUsername(),
                        principal.getPassword(),
                        principal.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context");
        }
        filterChain.doFilter(request, response);
    }
}
