package pe.edu.cibertec.apiusuariologinservice.service;

import pe.edu.cibertec.apiusuariologinservice.dto.LoginRequest;
import pe.edu.cibertec.apiusuariologinservice.dto.LoginResponse;
import pe.edu.cibertec.apiusuariologinservice.dto.UsuarioRequest;
import pe.edu.cibertec.apiusuariologinservice.dto.UsuarioResponse;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    LoginResponse autenticar(LoginRequest request);
    List<UsuarioResponse> listar();
    Optional<UsuarioResponse> obtener(Integer id);
    UsuarioResponse crear(UsuarioRequest request);
    Optional<UsuarioResponse> actualizar(Integer id, UsuarioRequest request);
    boolean eliminar(Integer id);
}
