package pe.edu.cibertec.apiusuariologinservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apiusuariologinservice.dto.LoginRequest;
import pe.edu.cibertec.apiusuariologinservice.dto.LoginResponse;
import pe.edu.cibertec.apiusuariologinservice.entity.Usuario;
import pe.edu.cibertec.apiusuariologinservice.repository.UsuarioRepository;
import pe.edu.cibertec.apiusuariologinservice.service.UsuarioService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public LoginResponse autenticar(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        // Validacion temporal con texto plano; luego puede reemplazarse por BCrypt.
        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new LoginResponse(
                "Autenticación exitosa",
                usuario.getUsername(),
                usuario.getRol().name(),
                usuario.getActivo()
        );
    }
}
