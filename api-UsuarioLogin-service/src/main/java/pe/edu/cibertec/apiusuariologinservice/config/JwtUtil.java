package pe.edu.cibertec.apiusuariologinservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Llave secreta segura para firmar los tokens
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // El token expirará en 24 horas
    private final long EXPIRATION_TIME = 86400000;

    public String generarToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol); // Agregamos el rol del usuario administrativo al token

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
}