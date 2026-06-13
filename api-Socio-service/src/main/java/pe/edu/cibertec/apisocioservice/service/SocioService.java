package pe.edu.cibertec.apisocioservice.service;

import pe.edu.cibertec.apisocioservice.dto.SocioResumenDTO;
import pe.edu.cibertec.apisocioservice.entity.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioService {
    List<Socio> listarTodos();

    Optional<Socio> obtenerPorId(Integer id);

    Optional<Socio> buscarPorDni(String dni);

    Socio guardar(Socio socio);

    Optional<Socio> actualizar(Integer id, Socio socio);

    boolean eliminar(Integer id);

    List<SocioResumenDTO> obtenerResumenSocios();
}
