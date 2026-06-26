package pe.edu.cibertec.apitesoreriaservice.config;

import jakarta.servlet.http.HttpServletRequest;
import pe.edu.cibertec.apitesoreriaservice.exception.ReglaNegocioException;

public final class UsuarioHeader {

    private UsuarioHeader() {
    }

    public static Integer obtenerIdUsuario(HttpServletRequest request) {
        String value = request.getHeader("X-Usuario-Id");
        if (value == null || value.isBlank()) {
            throw new ReglaNegocioException("No se recibio el usuario autenticado desde el Gateway.");
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new ReglaNegocioException("El usuario autenticado recibido no es valido.");
        }
    }
}
