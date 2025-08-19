package org.facundosaracho.starwarschallenge.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;

@Slf4j
@Component
@RequiredArgsConstructor
// Intercepta TODAS las peticiones HTTP y verifica si tienen un token JWT válido.
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);

            } catch (IllegalArgumentException e) {
                log.error("No se puede obtener el token JWT");
                throw new BadCredentialsException("Token inválido", e);

            } catch (ExpiredJwtException e) {
                log.error("Token JWT ha expirado");
                throw new CredentialsExpiredException("Token expirado", e);

            } catch (SignatureException | MalformedJwtException e) {
                log.error("Firma JWT inválida");
                throw new BadCredentialsException("Firma JWT inválida", e);
            }

        } else {
            log.warn("Token JWT no empieza con Bearer String");
        }

        // Si tenemos username válido y todavía no hay autenticación en contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
