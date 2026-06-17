package pe.edu.cibertec.apiusuariologinservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apiusuariologinservice.config.JwtUtil;
import pe.edu.cibertec.apiusuariologinservice.dto.LoginRequest;
import pe.edu.cibertec.apiusuariologinservice.dto.LoginResponse;
import pe.edu.cibertec.apiusuariologinservice.entity.Usuario;
import pe.edu.cibertec.apiusuariologinservice.repository.UsuarioRepository;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse autenticar(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRol().name());

        return new LoginResponse(
                "Autenticación exitosa",
                token,
                usuario.getUsername(),
                usuario.getRol().name(),
                usuario.getActivo()
        );
    }
}