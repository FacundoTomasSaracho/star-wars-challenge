package org.facundosaracho.starwarschallenge.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Intercepta todas las requests y, si hay JWT válido, setea Authentication en el contexto.
 * Si el JWT es inválido/expirado, devuelve 401 con ErrorDto a través del AuthenticationEntryPoint.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtUtil;
    private final AuthenticationEntryPoint entryPoint; // nuestro JwtAuthenticationEntryPoint

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        final String header = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        try {
            // Extrae el token si llega con Bearer.
            if (header != null && header.startsWith("Bearer ")) {
                jwtToken = header.substring(7);
                // getUsernameFromToken puede lanzar ExpiredJwtException, SignatureException, etc.
                username = jwtUtil.getUsernameFromToken(jwtToken);
            } else {
                log.debug("Authorization ausente o sin 'Bearer '");
            }

            // 2) Autenticar si corresponde
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwtToken, user.getUsername())) {
                    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    // Token no válido aunque tenga username
                    throw new BadCredentialsException("Token inválido");
                }
            }

            // 3) Continuar cadena si todo OK (o si no había token)
            chain.doFilter(request, response);

            // ======== Mapeos específicos útiles para logs claros ========
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado", e);
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new CredentialsExpiredException("Token has expired", e));

        } catch (SignatureException | MalformedJwtException e) {
            log.warn("Firma/estructura JWT inválida", e);
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new BadCredentialsException("JWT signature is invalid", e));

        } catch (IllegalArgumentException e) {
            log.warn("Token ausente o malformado", e);
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new BadCredentialsException("Token is missing or invalid.", e));

        } catch (AuthenticationException e) {
            // Cubre CredentialsExpiredException/BadCredentialsException que lance tu JwtService
            log.warn("Fallo de autenticación", e);
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, e);

        } catch (JwtException e) {
            // Cubre cualquier otra excepción de JJWT no contemplada arriba (UnsupportedJwtException, etc.)
            log.warn("JWT inválido. Excepción genérica.", e);
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new BadCredentialsException("Invalid token.", e));
        }
    }
}
