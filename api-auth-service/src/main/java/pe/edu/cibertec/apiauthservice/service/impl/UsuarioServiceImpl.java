package pe.edu.cibertec.apiauthservice.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.apiauthservice.config.JwtUtil;
import pe.edu.cibertec.apiauthservice.dto.LoginRequest;
import pe.edu.cibertec.apiauthservice.dto.LoginResponse;
import pe.edu.cibertec.apiauthservice.dto.UsuarioActualizarRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioCrearRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioEstadoRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioPasswordRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioResponse;
import pe.edu.cibertec.apiauthservice.entity.Usuario;
import pe.edu.cibertec.apiauthservice.exception.ConflictoException;
import pe.edu.cibertec.apiauthservice.exception.RecursoNoEncontradoException;
import pe.edu.cibertec.apiauthservice.exception.ReglaNegocioException;
import pe.edu.cibertec.apiauthservice.mapper.UsuarioMapper;
import pe.edu.cibertec.apiauthservice.repository.UsuarioRepository;
import pe.edu.cibertec.apiauthservice.service.UsuarioService;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder,
                              JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new ReglaNegocioException("Credenciales invalidas."));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ReglaNegocioException("El usuario se encuentra desactivado.");
        }

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new ReglaNegocioException("Credenciales invalidas.");
        }

        return new LoginResponse(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getNombreCompleto(),
                usuario.getRol().name(),
                jwtUtil.generarToken(usuario),
                jwtUtil.getExpirationSeconds()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Integer idUsuario) {
        return UsuarioMapper.toResponse(obtenerUsuario(idUsuario));
    }

    @Override
    @Transactional
    public UsuarioResponse registrar(UsuarioCrearRequest request) {
        validarNuevo(request.username(), request.dni(), request.correo());

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .nombreCompleto(request.nombreCompleto())
                .dni(request.dni())
                .correo(request.correo())
                .telefono(request.telefono())
                .rol(request.rol())
                .activo(true)
                .build();

        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse actualizar(Integer idUsuario, UsuarioActualizarRequest request) {
        Usuario usuario = obtenerUsuario(idUsuario);
        validarDuplicadosEdicion(usuario, request.dni(), request.correo());

        usuario.setNombreCompleto(request.nombreCompleto());
        usuario.setDni(request.dni());
        usuario.setCorreo(request.correo());
        usuario.setTelefono(request.telefono());
        usuario.setRol(request.rol());
        usuario.setActivo(request.activo());

        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse cambiarEstado(Integer idUsuario, UsuarioEstadoRequest request) {
        Usuario usuario = obtenerUsuario(idUsuario);
        usuario.setActivo(request.activo());
        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse restablecerPassword(Integer idUsuario, UsuarioPasswordRequest request) {
        Usuario usuario = obtenerUsuario(idUsuario);
        usuario.setPassword(passwordEncoder.encode(request.password()));
        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    private Usuario obtenerUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado."));
    }

    private void validarNuevo(String username, String dni, String correo) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new ConflictoException("Ya existe un usuario con ese username.");
        }
        if (usuarioRepository.existsByDni(dni)) {
            throw new ConflictoException("Ya existe un usuario con ese DNI.");
        }
        if (correo != null && usuarioRepository.existsByCorreo(correo)) {
            throw new ConflictoException("Ya existe un usuario con ese correo.");
        }
    }

    private void validarDuplicadosEdicion(Usuario usuario, String dni, String correo) {
        usuarioRepository.findAll().stream()
                .filter(actual -> !actual.getIdUsuario().equals(usuario.getIdUsuario()))
                .filter(actual -> actual.getDni().equals(dni) || (correo != null && correo.equals(actual.getCorreo())))
                .findFirst()
                .ifPresent(actual -> {
                    throw new ConflictoException("DNI o correo ya registrado por otro usuario.");
                });
    }
}
