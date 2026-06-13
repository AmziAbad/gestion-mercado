package pe.edu.cibertec.apiusuariologinservice.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}