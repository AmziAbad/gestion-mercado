package pe.edu.cibertec.apiusuariologinservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apiusuariologinservice.config.JwtUtil;
import pe.edu.cibertec.apiusuariologinservice.dto.LoginRequest;
import pe.edu.cibertec.apiusuariologinservice.dto.LoginResponse;
import pe.edu.cibertec.apiusuariologinservice.dto.UsuarioRequest;
import pe.edu.cibertec.apiusuariologinservice.dto.UsuarioResponse;
import pe.edu.cibertec.apiusuariologinservice.entity.Usuario;
import pe.edu.cibertec.apiusuariologinservice.repository.UsuarioRepository;
import pe.edu.cibertec.apiusuariologinservice.service.UsuarioService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

    @Override
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Optional<UsuarioResponse> obtener(Integer id) {
        return usuarioRepository.findById(id).map(this::toResponse);
    }

    @Override
    public UsuarioResponse crear(UsuarioRequest request) {
        validarRequestCrear(request);

        Usuario usuario = new Usuario();
        aplicarDatos(usuario, request, true);

        return toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public Optional<UsuarioResponse> actualizar(Integer id, UsuarioRequest request) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    validarRequestActualizar(id, request);
                    aplicarDatos(usuario, request, false);
                    return toResponse(usuarioRepository.save(usuario));
                });
    }

    @Override
    public boolean eliminar(Integer id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setActivo(false);
                    usuarioRepository.save(usuario);
                    return true;
                })
                .orElse(false);
    }

    private void validarRequestCrear(UsuarioRequest request) {
        validarCamposObligatorios(request, true);

        if (usuarioRepository.existsByUsername(request.getUsername().trim())) {
            throw new RuntimeException("Ya existe un usuario con ese username.");
        }
        if (usuarioRepository.existsByDni(request.getDni().trim())) {
            throw new RuntimeException("Ya existe un usuario con ese DNI.");
        }
        if (tieneTexto(request.getCorreo())
                && usuarioRepository.existsByCorreo(request.getCorreo().trim())) {
            throw new RuntimeException("Ya existe un usuario con ese correo.");
        }
    }

    private void validarRequestActualizar(Integer id, UsuarioRequest request) {
        validarCamposObligatorios(request, false);

        if (usuarioRepository.existsByUsernameAndIdUsuarioNot(request.getUsername().trim(), id)) {
            throw new RuntimeException("Ya existe un usuario con ese username.");
        }
        if (usuarioRepository.existsByDniAndIdUsuarioNot(request.getDni().trim(), id)) {
            throw new RuntimeException("Ya existe un usuario con ese DNI.");
        }
        if (tieneTexto(request.getCorreo())
                && usuarioRepository.existsByCorreoAndIdUsuarioNot(request.getCorreo().trim(), id)) {
            throw new RuntimeException("Ya existe un usuario con ese correo.");
        }
    }

    private void validarCamposObligatorios(UsuarioRequest request, boolean requierePassword) {
        if (request == null) {
            throw new RuntimeException("Debe enviar los datos del usuario.");
        }
        if (!tieneTexto(request.getUsername())) {
            throw new RuntimeException("Debe indicar el username.");
        }
        if (requierePassword && !tieneTexto(request.getPassword())) {
            throw new RuntimeException("Debe indicar la contraseña.");
        }
        if (!tieneTexto(request.getNombreCompleto())) {
            throw new RuntimeException("Debe indicar el nombre completo.");
        }
        if (!tieneTexto(request.getDni())) {
            throw new RuntimeException("Debe indicar el DNI.");
        }
        if (request.getRol() == null) {
            throw new RuntimeException("Debe indicar el rol.");
        }
    }

    private void aplicarDatos(Usuario usuario, UsuarioRequest request, boolean crear) {
        usuario.setUsername(request.getUsername().trim());
        usuario.setNombreCompleto(request.getNombreCompleto().trim());
        usuario.setDni(request.getDni().trim());
        usuario.setCorreo(tieneTexto(request.getCorreo()) ? request.getCorreo().trim() : null);
        usuario.setTelefono(tieneTexto(request.getTelefono()) ? request.getTelefono().trim() : null);
        usuario.setRol(request.getRol());
        usuario.setActivo(request.getActivo() != null ? request.getActivo() : true);

        if (crear || tieneTexto(request.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombreCompleto())
                .dni(usuario.getDni())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .build();
    }

    private boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }
}
