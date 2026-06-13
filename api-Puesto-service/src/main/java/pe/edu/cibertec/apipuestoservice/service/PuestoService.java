package pe.edu.cibertec.apipuestoservice.service;

import pe.edu.cibertec.apipuestoservice.dto.ActualizarTitularRequest;
import pe.edu.cibertec.apipuestoservice.entity.Puesto;

import java.util.List;
import java.util.Optional;

public interface PuestoService {
    List<Puesto> listar();

    List<Puesto> listarOcupados();

    List<Puesto> listarPorSocio(Integer idSocio);

    List<Puesto> listarPorPabellon(String nombre);

    Optional<Puesto> obtener(Integer id);

    Puesto crear(Puesto puesto);

    Optional<Puesto> actualizar(Integer id, Puesto puesto);

    Optional<Puesto> actualizarTitular(Integer id, ActualizarTitularRequest request);

    boolean eliminar(Integer id);
}
