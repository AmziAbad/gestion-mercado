package pe.edu.cibertec.apiservicioservice.service;

import pe.edu.cibertec.apiservicioservice.entity.Servicio;

import java.util.List;
import java.util.Optional;

public interface ServicioService {
    List<Servicio> listar();

    List<Servicio> listarActivos();

    Optional<Servicio> obtener(Integer id);

    Servicio crear(Servicio servicio);

    Optional<Servicio> actualizar(Integer id, Servicio servicio);

    Optional<Servicio> activar(Integer id);

    Optional<Servicio> desactivar(Integer id);

    boolean eliminar(Integer id);
}
