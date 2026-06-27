package pe.edu.cibertec.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FiltroJwtAuth extends OncePerRequestFilter {

    private final JwtService jwtService;

    public FiltroJwtAuth(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtService.extraerToken(request);
            if (token != null) {
                Claims claims = jwtService.obtenerClaims(token);
                jwtService.generarAuthSecurity(claims);
                filterChain.doFilter(new UsuarioHeaderRequest(request, claims), response);
                return;
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token JWT invalido o expirado.");
        }
    }

    private static class UsuarioHeaderRequest extends HttpServletRequestWrapper {

        private final Map<String, String> headers = new HashMap<>();

        UsuarioHeaderRequest(HttpServletRequest request, Claims claims) {
            super(request);
            Object idUsuario = claims.get("idUsuario");
            if (idUsuario != null) {
                headers.put("X-Usuario-Id", String.valueOf(idUsuario));
            }
            headers.put("X-Usuario-Username", claims.getSubject());
            String rol = claims.get("rol", String.class);
            if (rol != null) {
                headers.put("X-Usuario-Rol", rol);
            }
        }

        @Override
        public String getHeader(String name) {
            String value = headers.get(name);
            return value != null ? value : super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            String value = headers.get(name);
            if (value != null) {
                return Collections.enumeration(List.of(value));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> names = new HashSet<>(headers.keySet());
            Enumeration<String> originalNames = super.getHeaderNames();
            while (originalNames.hasMoreElements()) {
                names.add(originalNames.nextElement());
            }
            return Collections.enumeration(new ArrayList<>(names));
        }
    }
}
