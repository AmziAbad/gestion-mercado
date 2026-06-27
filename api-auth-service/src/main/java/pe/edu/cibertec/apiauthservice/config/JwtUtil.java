package pe.edu.cibertec.apiauthservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.apiauthservice.entity.Usuario;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long expirationSeconds;

    public JwtUtil(@Value("${jwt.secret}") String jwtSecret,
                   @Value("${jwt.expiration-seconds}") Long expirationSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
    }

    public String generarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idUsuario", usuario.getIdUsuario());
        claims.put("rol", usuario.getRol().name());
        claims.put("nombreCompleto", usuario.getNombreCompleto());

        long ahora = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getUsername())
                .issuedAt(new Date(ahora))
                .expiration(new Date(ahora + expirationSeconds * 1000))
                .signWith(secretKey)
                .compact();
    }

    public Long getExpirationSeconds() {
        return expirationSeconds;
    }
}
