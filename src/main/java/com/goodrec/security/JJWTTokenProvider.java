package com.goodrec.security;

import com.goodrec.config.ApplicationProperties;
import com.goodrec.user.domain.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

import static com.goodrec.security.JwtConstants.TOKEN_PREFIX;

@Service
public class JJWTTokenProvider implements TokenProvider {

    private final ApplicationProperties appProperties;

    public JJWTTokenProvider(ApplicationProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public String createToken(UserPrincipal userPrincipal) {

        final Date now = new Date();
        final Date expirationDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(userPrincipal.getUuid().toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    @Override
    public String getJwtFromHeader(String authHeader) {

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.replace(TOKEN_PREFIX, "");
        }

        return "";
    }

    @Override
    public UUID getUserUUIDFromToken(String token) {

        final Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    @Override
    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(token);

            return true;
        } catch (SignatureException ex) {
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty.");
        }

        return false;
    }
}
