package pe.edu.cibertec.apiauthservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.apiauthservice.dto.UsuarioActualizarRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioCrearRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioEstadoRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioPasswordRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioResponse;
import pe.edu.cibertec.apiauthservice.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(usuarioService.buscarPorId(idUsuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioCrearRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(request));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Integer idUsuario,
                                                      @Valid @RequestBody UsuarioActualizarRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(idUsuario, request));
    }

    @PatchMapping("/{idUsuario}/estado")
    public ResponseEntity<UsuarioResponse> cambiarEstado(@PathVariable Integer idUsuario,
                                                         @Valid @RequestBody UsuarioEstadoRequest request) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(idUsuario, request));
    }

    @PutMapping("/{idUsuario}/password")
    public ResponseEntity<UsuarioResponse> restablecerPassword(@PathVariable Integer idUsuario,
                                                               @Valid @RequestBody UsuarioPasswordRequest request) {
        return ResponseEntity.ok(usuarioService.restablecerPassword(idUsuario, request));
    }
}
