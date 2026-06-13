package pe.edu.cibertec.apiusuariologinservice.service;

import pe.edu.cibertec.apiusuariologinservice.dto.LoginRequest;
import pe.edu.cibertec.apiusuariologinservice.dto.LoginResponse;

public interface UsuarioService {
    LoginResponse autenticar(LoginRequest request);
}
