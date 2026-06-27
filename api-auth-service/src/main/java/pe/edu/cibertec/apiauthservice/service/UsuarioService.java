package pe.edu.cibertec.apiauthservice.service;

import pe.edu.cibertec.apiauthservice.dto.LoginRequest;
import pe.edu.cibertec.apiauthservice.dto.LoginResponse;
import pe.edu.cibertec.apiauthservice.dto.UsuarioActualizarRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioCrearRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioEstadoRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioPasswordRequest;
import pe.edu.cibertec.apiauthservice.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {

    LoginResponse login(LoginRequest request);

    List<UsuarioResponse> listar();

    UsuarioResponse buscarPorId(Integer idUsuario);

    UsuarioResponse registrar(UsuarioCrearRequest request);

    UsuarioResponse actualizar(Integer idUsuario, UsuarioActualizarRequest request);

    UsuarioResponse cambiarEstado(Integer idUsuario, UsuarioEstadoRequest request);

    UsuarioResponse restablecerPassword(Integer idUsuario, UsuarioPasswordRequest request);
}
