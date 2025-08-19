package org.facundosaracho.starwarschallenge.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtService {

    @Value("${jwt.secret:mySecretKey}")
    private String secret;

    // Token válido por 1 hora
    private static final long JWT_TOKEN_VALIDITY = 3600;

    public String getUsernameFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (BadCredentialsException |IllegalArgumentException e) {
            log.error("No se puede obtener el token JWT");
            throw new BadCredentialsException("Token inválido", e);
        } catch (ExpiredJwtException e) {
            log.error("Token JWT ha expirado");
            throw new CredentialsExpiredException("Token expirado", e);
        } catch (SignatureException | MalformedJwtException e) {
            log.error("Firma JWT inválida");
            throw new BadCredentialsException("Firma JWT inválida", e);
        }

    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //Crea un token JWT con expiración de 1 hora
    public String generateToken(String username) {
        return createToken(username);
    }

    private String createToken(String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Verifica si el token es válido y no ha expirado
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
