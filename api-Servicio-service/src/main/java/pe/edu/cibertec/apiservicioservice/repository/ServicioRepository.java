package pe.edu.cibertec.apiservicioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.apiservicioservice.entity.Servicio;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByActivoTrue();
}
