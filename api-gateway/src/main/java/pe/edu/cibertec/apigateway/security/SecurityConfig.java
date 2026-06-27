package pe.edu.cibertec.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";
    private static final String TESORERO = "TESORERO";
    private static final String RECEPCIONISTA = "RECEPCIONISTA";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api-UsuarioLogin-service/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api-auth-service/auth/login").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api-auth-service/usuarios").hasRole(ADMIN)
                        .requestMatchers("/api-auth-service/usuarios/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api-patrimonio-service/**")
                            .hasAnyRole(ADMIN, TESORERO, RECEPCIONISTA)
                        .requestMatchers("/api-patrimonio-service/**").hasAnyRole(ADMIN, RECEPCIONISTA)
                        .requestMatchers(HttpMethod.GET, "/api-tesoreria-service/estados-cuenta/**")
                            .hasAnyRole(ADMIN, TESORERO, RECEPCIONISTA)
                        .requestMatchers("/api-tesoreria-service/**").hasAnyRole(ADMIN, TESORERO)
                        .requestMatchers("/api-auditoria-reportes-service/**").hasAnyRole(ADMIN, TESORERO)
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(new FiltroJwtAuth(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
